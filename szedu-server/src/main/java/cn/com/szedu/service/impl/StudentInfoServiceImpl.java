package cn.com.szedu.service.impl;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.student.StudentClassInfoModel;
import cn.com.szedu.model.student.StudentInfoModel;
import cn.com.szedu.model.student.StudentIntegralModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.MessageModel;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.SystemUserRepository;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.StudentInfoService;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudentInfoServiceImpl implements StudentInfoService {

    @Resource
    private BackTokenService backTokenService;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Resource
    private IntegralRecordRepository integralRecordRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private IIntegralRuleRepository integralRuleRepository;
    @Resource
    private IClassInfoMapper classInfoMapper;
    @Resource
    private IStudentClassRelationRepository studentClassRelationRepository;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private SystemMessageRepository systemMessageRepository;
    @Resource
    private SystemUserRepository systemUserRepository;

    /**
     * 学生登录(根据账号和手机号)
     * @param model
     * @return
     * @throws UserServiceException
     */
    @Override
    public StudentInfoModel loginStudent(StudentInfoModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        StudentInfoModel model1=null;
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPhone())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        StudentInfo userInfo=studentInfoRespository.findByAccountAndPhone(model.getAccount(),model.getPhone());
        if (userInfo==null){
            throw new UserServiceException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }

        String user=JSONObject.toJSONString(MappingEntity3ModelCoverter.CONVERTERFROMBACKSTUDENTINFO(userInfo));
        try {
            String acc=backTokenService.createdToken(user);
            model1=MappingEntity3ModelCoverter.CONVERTERFROMBACKSTUDENTMODEL(userInfo);
            model1.setAccessToken(acc);
            Integer integral = qiandao(userInfo.getId(), "签到");
            if (integral > 0) {
                model1.setSignin(false);//不能签
            } else {
                model1.setSignin(true);//能签
            }
            return model1;
        }catch (UnsupportedEncodingException e){
            throw new UserServiceException(e.getMessage());
        }
    }

    /**
     * 签到积分
     *
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    @Override
    public Integer signin(UserInfoForToken userInfo) throws UserServiceException {
        //修改学生积分
        StudentInfo studentInfo = studentInfoRespository.findFirstByid(userInfo.getUserId());
        IntegralRule integralRule=integralRuleRepository.findByRoleAndType("学生","签到");
        if (studentInfo.getIntegral() == null) {
            studentInfo.setIntegral(0);
        }
        Integer integral = qiandao(userInfo.getUserId(), "签到");
        if (integral <integralRule.getCount()) {
            studentInfo.setIntegral(studentInfo.getIntegral() +integralRule.getIntegral());//学生签到积分
            studentInfoRespository.save(studentInfo);
            //积分记录
            IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                    "签到", "学生登录签到", userInfo.getUserId(), userInfo.getUserName(),
                    integralRule.getIntegral(), System.currentTimeMillis());
            integralRecordRepository.save(integralRecord);
            //系统信息
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String s = df.format(new Date());// new Date()为获取当前系统时间

            String messages = "您今日已成功签到获得"+integralRule.getIntegral()+"积分。" + s;
            Message message = new Message(studentInfo.getId(), studentInfo.getName(), messages, "N");
            messageRepository.save(message);
        }
        StudentInfo teacherInfo1 = studentInfoRespository.findFirstByid(userInfo.getUserId());
        return teacherInfo1.getIntegral();
    }

    /**
     * 留言
     * @param userInfo
     * @param message
     * @throws UserServiceException
     */
    @Override
    public Boolean LeavingMessage(UserInfoForToken userInfo, MessageModel message) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
       messageRepository.save(MappingEntity3ModelCoverter.CONVERTERFROMBACKMESSAGEMODELLEAVE(userInfo, message));
        return true;
    }

    /**
     * 获取留言
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    @Override
    public  List<Message> getLeavingMessage(UserInfoForToken userInfo) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        List<Message> message=messageRepository.findBySenderidAndRecipientId(userInfo.getUserId(),"1");
        return message;
    }
    //学生个人中心
    @Override
    public StudentInfo studentInfo(UserInfoForToken userInfo, String studentId) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(studentId)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        StudentInfo studentInfo=studentInfoRespository.findFirstByid(studentId);
        return studentInfo;
    }

    @Override
    public StudentClassInfoModel getstudentInfo(UserInfoForToken userInfo, String studentId) throws UserServiceException {
       // StudentClassInfoModel model=new StudentClassInfoModel();
        StudentInfo studentInfo=studentInfoRespository.findFirstByid(studentId);
        StudentClassInfoModel studentClassInfoModel=MappingEntity3ModelCoverter.CONVERTERFROMBACKSTUDENT(studentInfo);

        if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(studentId)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        List<StudentClassRelation> scr=studentClassRelationRepository.findByStudentId(studentId);
        if (scr.size()<=0){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        List<String> classId=new ArrayList<String>();
        for (StudentClassRelation s:scr){
            classId.add(s.getClassId());
        }
        List<ClassInfo> classInfos=classInfoRepository.findByIdIn(classId);
        List<String> className=new ArrayList<String>();
        for (ClassInfo c:classInfos){
            className.add(c.getClassName());
        }
        studentClassInfoModel.setClassName(className);
        return studentClassInfoModel;
    }
    //修改学生信息
    @Override
    public Boolean updateStudent(UserInfoForToken userInfo, StudentModel model) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(model)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        StudentInfo studentInfo=studentInfoRespository.findFirstByid(model.getId());
        if (studentInfo!=null){
            Integer ss=0;
            if (!(studentInfo.getPhone().equalsIgnoreCase(model.getPhone()))){//手机号不等
                ss=studentInfoRespository.countByAccountAndPhone(model.getAccount(),model.getPhone());
            }
            if (ss>0){//存在相同手机号
                throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
            }
            studentInfo.setName(model.getName());
            studentInfo.setSex(model.getSex());
            studentInfo.setBirthday(model.getBirthday());
            studentInfo.setAddress(model.getAddress());
            studentInfo.setPhone(model.getPhone());
            studentInfoRespository.save(studentInfo);
        }
        return  true;
    }

    /**
     * 学生积分规则
     * @param pageNum
     * @param pageSize
     * @return
     * @throws UserServiceException
     */
    @Override
    public Page<IntegralRule> integralRule(UserInfoForToken userInfo,int pageNum, int pageSize) throws UserServiceException {
        pageNum = pageNum - 1;
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<IntegralRule> records = integralRuleRepository.findAll((Root<IntegralRule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("role"),"学生"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return records;
    }

    /**
     * 学生积分排名
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     * @throws UserServiceException
     */
    @Override
    public List<StudentIntegralModel> integralRanking(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException {

       int count=0;
        StudentIntegralModel model=new StudentIntegralModel();
        List<StudentIntegralModel> modelList=new ArrayList<StudentIntegralModel>();
        List<StudentIntegralModel> models=classInfoMapper.findIntegeralRank();
        if (models.size()<=0){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}

        for (int i=0;i<models.size();i++){//前10名
            if (i<10){
                modelList.add( models.get(i));
            }
        }
        for (StudentIntegralModel s:models){//我的排名
            count++;
            if (s.getStudentId().equals(userInfo.getUserId())){
                model=new StudentIntegralModel(s.getPic(),s.getStudentId(),
                        s.getAcount(),s.getStudentName(),s.getIntegral() ,model.getMyrank(),s.getMyrank());
                model.setMyrank(count);
                modelList.add(model);
            }
        }
        return modelList;
    }

    /**
     * 学生系统信息
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     * @throws UserServiceException
     */
    @Override
    public List<Message> getStudentMessage(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException {
      /*  List<StudentClassRelation> scr=studentClassRelationRepository.findByStudentId(userInfo.getUserId());
        List<String> classId=new ArrayList<String>();
        for (StudentClassRelation s:scr){
            classId.add(s.getClassId());
        }*/
        List<Message> list=classInfoMapper.getStudentMessage(userInfo.getUserId(),(pageNum-1)*pageSize,pageSize);
        return list;
    }

    /**
     * 判断签到次数
     *
     * @param userId
     * @param name
     * @return
     */
    private Integer qiandao(String userId, String name){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(new Date().getTime());
        calendar1.set(Calendar.HOUR_OF_DAY, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        Integer integral = integralRecordRepository.countByOperatorIdAndFunctionAndCreateTimeGreaterThanAndCreateTimeLessThan(userId, name, calendar.getTimeInMillis(), calendar1.getTimeInMillis());
        return integral;
    }

    @Override
    public Integer countSystemMessage(UserInfoForToken userInfo,String schoolid)throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(schoolid)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        StudentInfo studentInfo=studentInfoRespository.findFirstById(userInfo.getUserId());
        if (StringUtils.isEmpty(studentInfo)){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        Integer count=systemUserRepository.countByAlreadyReadAndUid("false",studentInfo.getSchoolId());
       // Integer count= systemMessageRepository.countByAlreadyReadAndSchoolIdAndExamine("false",studentInfo.getSchoolId(),2);
        return count;
    }

}
