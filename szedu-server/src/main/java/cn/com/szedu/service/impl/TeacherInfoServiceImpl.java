package cn.com.szedu.service.impl;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.LoginMap;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.*;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.IDNameModel;
import cn.com.szedu.model.SandMessageModel;
import cn.com.szedu.model.student.StudentIntegralModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.ClassModel;
import cn.com.szedu.model.teacher.MessageModel;
import cn.com.szedu.model.teacher.TeacherInfoModel;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.*;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.TeacherInfoService;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TeacherInfoServiceImpl implements TeacherInfoService {

    @Resource
    private ITeacherInfoRepository teacherInfoRepository;
    @Resource
    private BackTokenService backTokenService;
    @Resource
    private IResourceTeacherInfoRelationRepository resourceTeacherInfoRelationRepository;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private IntegralRecordRepository integralRecordRepository;
    @Resource
    private IMessageUserRelationRepository messageUserRelationRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private IIntegralRuleRepository integralRuleRepository;
    @Resource
    private IClassInfoMapper classInfoMapper;
    @Resource
    private ICourseUserRelationRepository courseUserRelationRepository;
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Resource
    private IStudentClassRelationRepository srelationRepository;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private IStudentInfoRespository studentRespository;
    @Resource
    private TecherClassRelationRepository techerClassRelationRepository;
    @Resource
    private IMessageFeedbackRepository messageFeedbackRepository;
    @Resource
    private SystemMessageRepository systemMessageRepository;
    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;
    //记录登录用户


    @Override
    /**
     * 老师登录
     */
    public TeacherModel loginTeacher(TeacherModel model) throws UserServiceException {
        TeacherModel model1 = null;
        if (StringUtils.isEmpty(model.getAcount()) || StringUtils.isEmpty(model.getPassword())) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        TeacherInfo userInfo = teacherInfoRepository.findByAccountAndPwd(model.getAcount(), Md5Util.encodeByMd5(model.getPassword()));
        if (userInfo == null) {
            throw new UserServiceException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        List<CourseUserRelation> tcr = courseUserRelationRepository.findAllByUid(userInfo.getId());//老师课堂
        List<ResourceTeacherInfoRelation> trr = resourceTeacherInfoRelationRepository.findByTeacherId(userInfo.getId());//老师资源
        String user = JSONObject.toJSONString(MappingEntity3ModelCoverter.CONVERTERFROMBACKUSERUSER(userInfo));
        try {
            String acc = backTokenService.createdToken(user);
            model1 = MappingEntity3ModelCoverter.CONVERTERFROMBACKTEACHERINFO(userInfo);
            model1.setAccessToken(acc);
            model1.setCountCourse(tcr.size());
            model1.setCountResource(trr.size());
            Integer integral = qiandao(userInfo.getId(), "签到");
            if (integral > 0) {
                model1.setSignin(false);//不能签
            } else {
                model1.setSignin(true);//能签
            }

          Map<String,Long> p=new HashMap<>();
            p.put(userInfo.getId(), System.currentTimeMillis() / 1000);
            LoginMap.setMap(p);
            return model1;
        } catch (UnsupportedEncodingException e) {
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
        //修改老师积分
        TeacherInfo teacherInfo = teacherInfoRepository.findExsitById(userInfo.getUserId());
        if (teacherInfo.getIntegral() == null) {
            teacherInfo.setIntegral(0);
        }
        Integer integral = qiandao(userInfo.getUserId(), "签到");
        if (integral <= 0) {
            teacherInfo.setIntegral(teacherInfo.getIntegral() + 1);//老师等级积分
            teacherInfoRepository.save(teacherInfo);
            //积分记录UUID.randomUUID().toString().replaceAll("-", ""),
            IntegralRecord integralRecord = new IntegralRecord(
                    "签到", "老师登录签到", userInfo.getUserId(), userInfo.getUserName(),
                    1, System.currentTimeMillis());
            addintegral(userInfo,integralRecord);
            //integralRecordRepository.save(integralRecord);
            //系统信息
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String s = df.format(new Date());// new Date()为获取当前系统时间

            String messages = "您今日已成功签到获得1积分。" + s;
           // Message message = new Message(teacherInfo.getId(), teacherInfo.getName(), messages, "N");
            MessageModel message=new MessageModel(teacherInfo.getId(), teacherInfo.getName(), messages, "N");
            addMessage(userInfo,message);
            //messageRepository.save(message);
        }/*else {
            throw new UserServiceException(ResultCode.SIGNIN_IS_EXIST);
        }*/
        TeacherInfo teacherInfo1 = teacherInfoRepository.findExsitById(userInfo.getUserId());
        return teacherInfo1.getIntegral();
    }

    /**
     * 查看老师积分
     *
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     * @throws UserServiceException
     */
    @Override
    public Page<IntegralRecord> getTeacherIntegral(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException {
        pageNum = pageNum - 1;
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<IntegralRecord> records = integralRecordRepository.findAll((Root<IntegralRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("operatorId"), userInfo.getUserId()));
                /*if(!StringUtils.isEmpty(userInfo.getUserName())){
                    predicateList.add(criteriaBuilder.equal(root.get("operatorName"),userInfo.getUserName()));
                }*/
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return records;
    }

    /**
     * 查看系统消息
     *
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Message> getTeacherMessage(UserInfoForToken userInfo, int pageNum, int pageSize) {
       /* pageNum = pageNum - 1;
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "time"));
        Pageable pageable = new PageRequest(pageNum, pageSize,sort);
        Page<Message> records = messageRepository.findAll((Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("recipientId"), userInfo.getUserId()));
            predicateList.add(criteriaBuilder.equal(root.get("send"), "Y"));//已发送
            predicateList.add(criteriaBuilder.equal(root.get("senderid"), null));//系统
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);*/
       List<Message> list=classInfoMapper.getTeacherMessage(userInfo.getUserId(),(pageNum-1)*pageSize,pageSize);
        return list;
    }

    /**
     * 未读消息数(根据老师)
     *
     * @param userInfo
     * @return
     */
    @Override
    public Integer getTeacherMessageCount(UserInfoForToken userInfo)throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        Integer count=messageRepository.countByRecipientIdAndAlreadyRead(userInfo.getUserId(),"false");
        TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
        if (StringUtils.isEmpty(teacherInfo.getSchoolId())){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        Integer count2=systemMessageRepository.countByAlreadyReadAndSchoolIdAndExamine("false",teacherInfo.getSchoolId(),2);
        return count+count2;
    }

    /**
     * 30分钟加积分
     *
     * @param
     * @throws UserServiceException
     */
   /* @Override
    public void getUserLogin(UserInfoForToken userInfo) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        long end = System.currentTimeMillis();
        //修改老师积分
        TeacherInfo teacherInfo = teacherInfoRepository.findExsitById(userInfo.getUserId());
        if (userInfo.getLoginTime() + 30 >= end) {
            System.out.println("40秒");
            Integer integral = qiandao(userInfo.getUserId(), "在线");
            if (integral <= 0) {
                teacherInfo.setIntegral(teacherInfo.getIntegral() + 5);//老师等级积分
                teacherInfoRepository.save(teacherInfo);
                //积分记录
                IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                        "在线", "老师在线30分钟", userInfo.getUserId(), userInfo.getUserName(),
                        5, System.currentTimeMillis());
                integralRecordRepository.save(integralRecord);
            }
        }
    }*/

    /**
     * 在线30分钟积分记录
     *
     * @param userId
     */
    @Override
    public void getUserLogin(String userId) {
        IntegralRule integralRule = integralRuleRepository.findByRoleAndType("老师", "在线");
        if (integralRule==null){
            return;
        }
        //修改老师积分
        TeacherInfo teacherInfo = teacherInfoRepository.findExsitById(userId);
        Integer integral = qiandao(userId, "在线");
        if (integral <= 0) {
            teacherInfo.setIntegral(teacherInfo.getIntegral() + integralRule.getIntegral());//老师等级积分
            teacherInfoRepository.save(teacherInfo);
            //积分记录
            IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                    "在线", "老师在线30分钟", userId, teacherInfo.getName(),
                    integralRule.getIntegral(), System.currentTimeMillis());
            integralRecordRepository.save(integralRecord);
        }
    }

    /**
     * 已读系统信息
     *
     * @param userInfo
     * @throws UserServiceException
     */
    @Override
    public boolean updateTeacherMessage(UserInfoForToken userInfo,Integer id) throws UserServiceException {
       // Message message = messageRepository.findByRecipientIdAndSenderidAndId(userInfo.getUserId(), null,id);//系统信息
        Message message = messageRepository.findByIdIs(id);
        message.setAlreadyRead("true");
        messageRepository.save(message);//已读
        return true;
    }


    /**
     * 积分规则
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<IntegralRule> integralRule(int pageNum, int pageSize) {
        pageNum = pageNum - 1;
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<IntegralRule> records = integralRuleRepository.findAll((Root<IntegralRule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("role"),"老师"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return records;
    }

    /**
     * 给老师发送系统信息(定时30秒查一次)
     *
     * @param userInfo
     * @return
     */
    @Override
    public Integer sendMessageByTeacher(UserInfoForToken userInfo) {
        if (StringUtils.isEmpty(userInfo)){
            return 0;
        }
        Integer count = messageRepository.countByRecipientIdAndSendAndSenderid(userInfo.getUserId(), "N", null);
        List<Message> message = messageRepository.findByRecipientIdAndSendAndSenderid(userInfo.getUserId(), "N", null);
      for (Message m:message){
          Message message1=messageRepository.findByIdIs(m.getId());
          message1.setSend("Y");//修改为发送状态
          messageRepository.save(message1);
      }
        return count;//返回发送消息数
    }

    /**
     * 判断签到次数
     *
     * @param userId
     * @param name
     * @return
     */
    private Integer qiandao(String userId, String name) {
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

    /**
     * 获取老师信息
     *
     * @param userInfo
     * @return
     */
    private TeacherModel getTeacherDetail(TeacherInfo userInfo) {
        TeacherModel model1 = null;
        List<CourseUserRelation> tcr = courseUserRelationRepository.findAllByUid(userInfo.getId());//老师课堂
        //List<ResourceTeacherInfoRelation> trr = resourceTeacherInfoRelationRepository.findByTeacherId(userInfo.getId());//老师资源
        //List<CourseWare> trr=
        model1 = MappingEntity3ModelCoverter.CONVERTERFROMBACKTEACHERINFO(userInfo);
        model1.setCountCourse(tcr.size());
        // model1.setCountResource(trr.size());
        return model1;
    }

    /**
     * 获取互动消息
     *
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
   /* @Override
    public Page<Message> getTeacherInteractionMessage(UserInfoForToken userInfo, int pageNum, int pageSize) {
        pageNum = pageNum - 1;
        Pageable pageable = new PageRequest(pageNum, pageSize);
        Page<Message> records = messageRepository.findAll((Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("recipientId"), userInfo.getUserId()));
            predicateList.add(criteriaBuilder.equal(root.get("send"),"Y"));//已发送
            predicateList.add(criteriaBuilder.notEqual(root.get("senderid"),null));//互动
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return records;
    }*/

    /**
     * 获取互动消息(根据用户，不分发送接收者)
     *
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Message> getTeacherInteractionMessage(UserInfoForToken userInfo, int pageNum, int pageSize) {
        List<Message> messages = classInfoMapper.getMessageByUser(userInfo.getUserId(), (pageNum - 1) * pageSize, pageSize);
        return messages;
    }

    /**
     * 发送互动信息
     *
     * @param userInfo
     * @param
     */
    @Override
    public boolean addInteractionMessage(UserInfoForToken userInfo, SandMessageModel model) {
        for (IDNameModel i : model.getRecipient()) {
            model.setSend("Y");
            Message message = MappingEntity3ModelCoverter.CONVERTERFROMBACKMESSAGEMODEL(userInfo, model);
            message.setRecipientId(i.getId());//接收者
            message.setRecipientName(i.getName());
            Message message1 = messageRepository.save(message);//保存信息
            MessageUserRelation mur = new MessageUserRelation();//信息id和用户id,一条信息对应多个用户
            mur.setmId(message1.getId());
            mur.setuId(i.getId());//接收者
            messageUserRelationRepository.save(mur);
            MessageUserRelation mur2 = new MessageUserRelation();//信息id和用户id,一条信息对应多个用户
            mur2.setmId(message1.getId());
            mur2.setuId(userInfo.getUserId());//发送者
            messageUserRelationRepository.save(mur2);
        }
        if (model.getClassId()!=null){
            List<String> studentId=new ArrayList<>();
            List<StudentClassRelation> studentClassRelations=srelationRepository.findByClassIdIn(model.getClassId());
            for (StudentClassRelation s:studentClassRelations){
                studentId.add(s.getStudentId());
            }
            List<StudentInfo> studentInfos=studentInfoRespository.findByIdIn(studentId);
            for (StudentInfo info:studentInfos){
                model.setSend("Y");
                Message message = MappingEntity3ModelCoverter.CONVERTERFROMBACKMESSAGEMODEL(userInfo, model);
                message.setRecipientId(info.getId());//接收者
                message.setRecipientName(info.getName());
                Message message1 = messageRepository.save(message);//保存信息
                MessageUserRelation mur = new MessageUserRelation();//信息id和用户id,一条信息对应多个用户
                mur.setmId(message1.getId());
                mur.setuId(info.getId());//接收者
                messageUserRelationRepository.save(mur);
                MessageUserRelation mur2 = new MessageUserRelation();//信息id和用户id,一条信息对应多个用户
                mur2.setmId(message1.getId());
                mur2.setuId(userInfo.getUserId());//发送者
                messageUserRelationRepository.save(mur2);
            }
        }
        return true;
    }

    /**
     * 修改积分规则(积分内容，积分，次数)
     *
     * @param userInfo
     * @param integralRule
     */
    @Override
    public void updateintegralRule(UserInfoForToken userInfo, IntegralRule integralRule) {
        IntegralRule integralRule1 = integralRuleRepository.findFirstById(integralRule.getId());
        if (integralRule1 != null) {
            integralRule1.setContent(integralRule.getContent());//类容
            integralRule1.setCount(integralRule.getCount());//次数
            integralRule1.setIntegral(integralRule.getIntegral());//积分
            integralRuleRepository.save(integralRule1);
        }

    }

    /**
     * 获取除了自己以外的所有老师
     *
     * @param userInfo
     * @return
     */
    @Override
    public List<TeacherModel> getAllTeacher(UserInfoForToken userInfo) {
        List<TeacherModel> modelList = new ArrayList<TeacherModel>();
        TeacherInfo teacher=teacherInfoRepository.findExsitById(userInfo.getUserId());
        List<TeacherInfo> teacherInfo = teacherInfoRepository.findByIdNotAndSchoolId(userInfo.getUserId(),teacher.getSchoolId());//除了自己
        for (TeacherInfo t : teacherInfo) {
            TeacherModel teacherModel = MappingEntity3ModelCoverter.CONVERTERFROMBACKTEACHERINFO(t);
            modelList.add(teacherModel);
        }
        return modelList;
    }
    @Override
    public List<ClassModel> getAllStudent(UserInfoForToken userInfo) {
        List<ClassModel> models=new ArrayList<ClassModel>();
        ClassModel classModel=new ClassModel();
        List<StudentIntegralModel> modelList = new ArrayList<StudentIntegralModel>();
        StudentIntegralModel model=new StudentIntegralModel();
        List<String> classId=new ArrayList<String>();
        List<String> studentId=new ArrayList<String>();
        List<StudentClassRelation> studentClassRelations=srelationRepository.findByStudentId(userInfo.getUserId());//同班学生
        for (StudentClassRelation s:studentClassRelations){
            classId.add(s.getClassId());
        }
        List<ClassInfo> list=classInfoRepository.getClassInfoByIdIn(null,classId);
        for (ClassInfo c:list){
            classModel.setClassId(c.getId());
            classModel.setClassName(c.getClassName());
            models.add(classModel);
        }
       /* List<StudentClassRelation> all=srelationRepository.findByStudentIdIsNotAndClassIdIn(userInfo.getUserId(),classId);
        for (StudentClassRelation sc:studentClassRelations){
            studentId.add(sc.getStudentId());
        }
        //TeacherInfo teacher=teacherInfoRepository.findExsitById(userInfo.getUserId());
        List<StudentInfo> studentInfos = studentInfoRespository.findByIdIn(studentId);
      for (StudentInfo s:studentInfos){
          model.setStudentId(s.getId());
          model.setStudentName(s.getName());
          modelList.add(model);
      }*/
        //return modelList;
        return models;
    }

    @Override
    public void deleteMessage(Integer id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void deleteInteractionMessage(UserInfoForToken userInfo,Integer id) {
        //messageRepository.deleteById(id);
        messageUserRelationRepository.deleteByMIdAndUId(id,userInfo.getUserId());
    }

    @Override
    public void addMessage(UserInfoForToken userInfo, MessageModel message) {
        Message message1=MappingEntity3ModelCoverter.CONVERTERFROMBACKMESSAGE(userInfo,message);
        messageRepository.save(message1);
    }

    /***
     * 积分
     * @param userInfo
     * @param integralRecord
     */
    @Override
    public void addintegral(UserInfoForToken userInfo, IntegralRecord integralRecord) {
        integralRecord.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        integralRecordRepository.save(integralRecord);
    }

    /**
     * 根据用户查询聊天
     * @param userInfo
     * @param sendId
     * @param pageNum
     * @param pageSize
     * @returnFF
     * @throws UserServiceException
     */
    @Override
    public List<Message> getMessageBysenderid(UserInfoForToken userInfo,String sendId, int pageNum, int pageSize) throws UserServiceException {
    List<Message> list=classInfoMapper.getMessageBysenderid(userInfo.getUserId(),sendId,(pageNum-1)*pageSize,pageSize);

        return list;
    }

    /**
     * 系统未读
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    @Override
    public Integer getSystemMessageCount(UserInfoForToken userInfo) throws UserServiceException {
        Integer count=messageRepository.countBySenderidIsNullAndAlreadyReadIsTrue();
        return count;
    }

    /**
     * 互动未读
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    @Override
    public Integer gethuMessageCount(UserInfoForToken userInfo) throws UserServiceException {
        Integer count=messageRepository.countBySenderidIsNotNullAndAlreadyRead("ture");
        return count;
    }

    /**
     * 老师个人中心
     * @param userInfo
     * @param teacherId
     * @return
     * @throws UserServiceException
     */
    @Override
    public TeacherInfoModel teacherinfo(UserInfoForToken userInfo,String teacherId) throws UserServiceException {
        TeacherInfoModel model=new TeacherInfoModel();
        ClassModel classModel=new ClassModel();
        List<ClassModel> cmodel=new ArrayList<ClassModel>();
        TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(teacherId);//查询老师信息
        if (teacherInfo==null){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        SchoolInfo schoolInfos =schoolInfoRepository.findFirstById(teacherInfo.getSchoolId());
        List<ClassRelation> classRelation=techerClassRelationRepository.findByTecherId(teacherId);
        if (classRelation.size()<=0){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        for (ClassRelation c:classRelation){
            ClassInfo classInfo=classInfoRepository.findExistById(c.getClassId());
            if (classInfo!=null){
                classModel.setClassId(classInfo.getId());
                classModel.setClassName(classInfo.getClassName());
                cmodel.add(classModel);
            }

        }
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd"); //设置格式
        String timeText="";
        if (!StringUtils.isEmpty(teacherInfo.getBirthday())){
            timeText=format.format(teacherInfo.getBirthday());
        }
        model.setTeacherBirthday(timeText);
        model.setTeacherInfo(teacherInfo);//老师基本信息
        model.setClassModels(cmodel);
        //model.setSchoolIdCov(schoolInfos.get);//学校封面
        return model;
    }

    /**
     * 修改老师信息
     * @param userInfo
     * @param teacherInfo
     * @throws UserServiceException
     */
    @Override
    public boolean updateteacherinfo(UserInfoForToken userInfo, TeacherInfo teacherInfo) throws UserServiceException {
        TeacherInfo teacherInfo1=teacherInfoRepository.findExsitById(teacherInfo.getId());
        TeacherInfo count=teacherInfoRepository.findByAccountAndPwd(teacherInfo.getAccount(),teacherInfo.getPwd());
            if (count!=null){//存在
                throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
            }
            teacherInfo1.setName(teacherInfo.getName());
            // teacherInfo1.setAccount(teacherInfo.getAccount());
            teacherInfo1.setAddress(teacherInfo.getAddress());
            teacherInfo1.setSex(teacherInfo.getSex());
            teacherInfo1.setPhone(teacherInfo.getPhone());
            teacherInfo1.setBirthday(teacherInfo.getBirthday());
            teacherInfoRepository.save(teacherInfo1);
        /*if (userInfo.getUserName().equalsIgnoreCase(teacherInfo.getName())) {//修改了老师姓名
            integralRecordRepository.updateOperatorName(userInfo.getUserId(), teacherInfo.getName());
        }*/
        return true;
    }

    /**
     * 上传老师头像
     * @param file
     * @param userInfo
     * @param teacherId
     * @throws IOException
     */
    @Override
    public Boolean teacherinfoPic(MultipartFile file, UserInfoForToken userInfo, String teacherId)throws IOException {
        if (StringUtils.isEmpty(teacherId)){//
            throw new IOException(ResultCode.PARAM_MISS_MSG);
        }
        TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(teacherId);
      /*  teacherInfo.setTeacherPic(file.getBytes());
        System.out.println(file.getBytes());*/

        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>3){
            throw new IOException("图片过大！");
        }
        //重新生成文件名
        fileName =UUID.randomUUID()+suffixName;
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        teacherInfo.setPortrait(uri+fileName);
        teacherInfoRepository.save(teacherInfo);
        return true;
    }

    @Override
    public List<ClassModel> getAllClass(UserInfoForToken userInfo) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG); }
        List<ClassModel> classModels=new ArrayList<ClassModel>();
        List<ClassModel> classModels2=new ArrayList<ClassModel>();
        ClassModel classi=null;
        classModels=classInfoMapper.getTeacherIdById(userInfo.getUserId());
        for (ClassModel info:classModels) {
            List<StudentClassRelation> sclist=srelationRepository.findAllByClassId(info.getClassId());//查询班级所有学生
            info.setStudent(sclist.size());
            classModels2.add(info);
        }
        return classModels2;
    }

    @Override
    public List<StudentModel> getStudentByClassId(UserInfoForToken userInfo, String classId) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(classId)){
            throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo classInfo=classInfoRepository.findExistById(classId);
        List<StudentClassRelation> relation=srelationRepository.findAllByClassId(classId);
        List<StudentModel> studentModels=new ArrayList<StudentModel>();
        List<StudentModel> models=new ArrayList<StudentModel>();
        if (relation!=null){
            for (StudentClassRelation s:relation) {
                StudentInfo info=studentRespository.findFirstByid(s.getStudentId());
                if (info!=null){
                    StudentModel model=MappingEntity3ModelCoverter.CONVERTERFROMSTUDENTMODEL(info);
                    model.setSpecialtity(classInfo.getSpeciality());
                    studentModels.add(model);
                }
            }
            models=classInfoMapper.getByClassId(classId);

            return models;
        }
        return null;
    }

    @Override
    public List<TeacherModel> getStudentTeacher(UserInfoForToken userInfo) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        List<TeacherModel> models=new ArrayList<TeacherModel>();
        TeacherModel teacherModel=new TeacherModel();
        List<StudentIntegralModel> modelList = new ArrayList<StudentIntegralModel>();
        StudentIntegralModel model=new StudentIntegralModel();
        List<String> classId=new ArrayList<String>();
        List<String> teacherId=new ArrayList<String>();
        List<StudentClassRelation> studentClassRelations=srelationRepository.findByStudentId(userInfo.getUserId());//同班学生
        for (StudentClassRelation s:studentClassRelations){
            classId.add(s.getClassId());
        }
        List<ClassRelation> teacher=techerClassRelationRepository.findByClassIdIn(classId);
        for (ClassRelation c:teacher){
            teacherId.add(c.getTecherId());
        }
        List<TeacherInfo> teacherInfos=teacherInfoRepository.findByIdIn(teacherId);
        if (teacherInfos.size()<=0){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        for (TeacherInfo t:teacherInfos){
            teacherModel.setTeacherId(t.getId());
            teacherModel.setTeacherName(t.getName());
            teacherModel.setAcount(t.getAccount());
            models.add(teacherModel);
        }
        return models;
    }

    @Override
    public boolean teacherMessage(UserInfoForToken userInfo, MessageFeedback feedback) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) && StringUtils.isEmpty(feedback)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
            feedback.setTeacherId(userInfo.getUserId());
        messageFeedbackRepository.save(feedback);
        return true;
    }
    @Override
    public Integer countSystemMessage(UserInfoForToken userInfo,String schoolid)throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(schoolid)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
        if (StringUtils.isEmpty(teacherInfo)){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
       Integer count= systemMessageRepository.countByAlreadyReadAndSchoolIdAndExamine("false",teacherInfo.getSchoolId(),2);
        return count;
    }


}

