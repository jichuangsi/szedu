package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.CourseClassRelation;
import cn.com.szedu.entity.IntermediateTable.CoursePushResourceRelation;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.entity.IntermediateTable.StudentCourseRelation;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.model.teacher.MyAllLessionModel;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.IClassCourseRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ICoursePushWareRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentCourseScoreRepository;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudentLessonService {

    @Resource
    private IAttendanceRepository attendanceRepository;
    @Resource
    private IClassCourseRelationRepository classCourseRelationRepository;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private IStudentClassRelationRepository studentClassRelationRepository;
    @Resource
    private IStudentCourseScoreRepository studentCourseScoreRepository;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Resource
    private IntegralRecordRepository integralRecordRepository;
    @Resource
    private IIntegralRuleRepository integralRuleRepository;
    @Resource
    private ICourseRepository courseRepository;
    @Resource
    private ICoursePushWareRelationRepository coursePushWareRelationRepository;
    @Resource
    private ICourseWareRespository courseWareRespository;
    @Resource
    private MessageRepository messageRepository;

    /**
     * 课堂签到
     * @param info
     * @param model
     */
    public void addAttendace(UserInfoForToken info, AttendanceModel model)throws TecherException {
        if (StringUtils.isEmpty(info)||StringUtils.isEmpty(model)){throw  new TecherException(ResultCode.PARAM_MISS_MSG);}
        List<CourseClassRelation> cclist=classCourseRelationRepository.findByCourseId(model.getCourseId());//根据courseId查班级
        for (CourseClassRelation cr:cclist ) {//根据班级和学生查找班级
            StudentClassRelation scr=studentClassRelationRepository.findByClassIdAndStudentId(cr.getClassId(),model.getSid());
            if (scr!=null){
                ClassInfo cinso=classInfoRepository.findExistById(scr.getClassId());
                model.setClassId(scr.getClassId());
                model.setClassName(cinso.getClassName());
            }
        }
        AttendanceInClass aclass=MappingEntity3ModelCoverter.CONVERTERFROMBACKATTENDANCEMODEL(info, model);
        attendanceRepository.save(aclass);

        IntegralRule integralRule=integralRuleRepository.findByRoleAndType("学生","上课签到");
        //上课签到5积分
        StudentInfo studentInfo=new StudentInfo();
        StudentInfo infos=studentInfoRespository.findFirstByid(info.getUserId());
        //studentInfo.setId(info.getUserId());
        infos.setIntegral(infos.getIntegral()+integralRule.getIntegral());
        studentInfoRespository.save(infos);
        IntegralRecord integralRecord=new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                "签到","上课签到",studentInfo.getId(),infos.getName(),
                integralRule.getIntegral(),new Date().getTime());
        integralRecordRepository.save(integralRecord);
    }

    /**
     * 课堂评分
     * @param info
     * @param score
     * @param courseId
     */
    public void addscore(UserInfoForToken info,Integer score,String courseId)throws UserServiceException {
        if (StringUtils.isEmpty(info) ||StringUtils.isEmpty(score) ||StringUtils.isEmpty(courseId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        StudentCourseRelation studentCourseScore=new StudentCourseRelation();
        studentCourseScore.setCourseId(courseId);
        studentCourseScore.setStudentId(info.getUserId());
        studentCourseScore.setScorse(score);
        studentCourseScoreRepository.save(studentCourseScore);
        StudentInfo studentInfo=studentInfoRespository.findFirstByid(info.getUserId());
        if (studentInfo.getIntegral()==null){
            studentInfo.setIntegral(0);
        }
        IntegralRule integralRule=integralRuleRepository.findByRoleAndType("老师","评分");
        if (score>=integralRule.getCount()){//大于integralRule.getCount()分
            studentInfo.setIntegral(studentInfo.getIntegral()+integralRule.getIntegral());
            //积分记录
            IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                    "评分", "学生课堂评分", info.getUserId(), info.getUserName(),
                    integralRule.getIntegral(), System.currentTimeMillis());
            integralRecordRepository.save(integralRecord);
        }
    }

    /**
     * 我的课堂
     *
     * @param userInfo
     * @return
     * @throws TecherException
     */
    public Page<Course> getAllLesson(UserInfoForToken userInfo, MyAllLessionModel mmodel) throws TecherException {
       /* if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(mmodel)) {
            throw new TecherException(ResultCode.TOKEN_MISS_MSG);userInfo.getUserId()
        }*/
        List<StudentClassRelation>  classRelations=studentClassRelationRepository.findByStudentId(userInfo.getUserId());
        List<String> classId=new ArrayList<String>();
        List<String> courseId=new ArrayList<String>();
        for (StudentClassRelation s:classRelations) {
            classId.add(s.getClassId());
        }
        List<CourseClassRelation> courseClassRelations=classCourseRelationRepository.findByClassIdIn(classId);//班级课堂

        for (CourseClassRelation c:courseClassRelations) {
            courseId.add(c.getCourseId());
        }
        if (courseId.size()<=0){
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }
       // List<Course> list=null;
      /*  List<Course> list=courseRepository.findBySubjectIdAndLessonTypeNameAndStartTimeAndIdIn(mmodel.getSubjectId(),
                mmodel.getLessionType(),mmodel.getTime(),courseId);*/
        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "startTime"));
        Pageable pageable=new PageRequest(mmodel.getPageNum()-1,mmodel.getPageSize(),sort);
        Page<Course> course=courseRepository.findAll((Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            Path<Object> path = root.get("id");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (String s:courseId) {
                in.value(s);
            }
            predicateList.add(criteriaBuilder.and(criteriaBuilder.and(in)));
            if(!StringUtils.isEmpty(mmodel.getSubjectId())){
                predicateList.add(criteriaBuilder.equal(root.get("subjectId").as(Integer.class),mmodel.getSubjectId()));
            }
            if(!StringUtils.isEmpty(mmodel.getLessionType())){
                predicateList.add(criteriaBuilder.equal(root.get("lessonTypeName").as(String.class),mmodel.getLessionType()));
            }
            if(mmodel.getTime()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("startTime").as(long.class),mmodel.getTime()));
            }

            //predicateList.add(criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startTime"))));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return course;
    }

    /**
     * 学生端(学习资料)
     * @param userInfo
     * @param model
     * @return
     */
   public List<CourseWare> getStudy(UserInfoForToken userInfo, MyAllLessionModel model){
       // List<StudentCourseRelation>  courseRelations=studentCourseScoreRepository.findByStudentId("402881836f171029016f1710f27f0000");
       List<StudentClassRelation> courseRelations=studentClassRelationRepository.findByStudentId(userInfo.getUserId());
        List<String> classId=new ArrayList<String>();
       for (StudentClassRelation c:courseRelations ) {
           classId.add(c.getClassId());
       }
       List<CourseClassRelation> courseClassRelations=classCourseRelationRepository.findByClassIdIn(classId);
       List<String> courseId=new ArrayList<String>();
       for (CourseClassRelation cc: courseClassRelations) {
           courseId.add(cc.getCourseId());
       }
       List<CoursePushResourceRelation> coursePushResourceRelations=coursePushWareRelationRepository.findByCourseIdInAndPushAndPushTime(courseId,"Y",model.getTime());
        if (coursePushResourceRelations.size()<=0){
            return null;
        }
       List<String> resourseId=new ArrayList<String>();
       for (CoursePushResourceRelation cp: coursePushResourceRelations) {
            resourseId.add(cp.getPushresourceid());
       }
       List<CourseWare> list=courseWareRespository.findBySubjectAndLabelAndIdIn(model.getSubjectId(),model.getLessionType(),resourseId);
       return list;
    }

    /**
     * 根据id查看资源详情
     * @param userInfo
     * @param id
     * @return
     */
    public CourseWare getCourseWareDetail(UserInfoForToken userInfo,String id){
           CourseWare courseWare=courseWareRespository.findByid(id);
           StudentInfo studentInfo=studentInfoRespository.findById(userInfo.getUserId());//查询学生
            Integer count=qiandao(userInfo.getUserId(),"查看资料");
        IntegralRule integralRule=integralRuleRepository.findByRoleAndType("学生","查看资料");
            if (count<integralRule.getCount()){
                studentInfo.setIntegral(studentInfo.getIntegral()+integralRule.getIntegral());//加积分
                studentInfoRespository.save(studentInfo);
                //积分记录
                IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                        "查看资料", "学生查看资料", userInfo.getUserId(), userInfo.getUserName(),
                        integralRule.getIntegral(), System.currentTimeMillis());
                integralRecordRepository.save(integralRecord);
                //系统信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String s = df.format(new Date());// new Date()为获取当前系统时间

                String messages = "您今日查看资料获得"+integralRule.getIntegral()+"积分。" + s;
                Message message = new Message(studentInfo.getId(), studentInfo.getName(), messages, "N");
                messageRepository.save(message);
            }
           return courseWare;
    }

    /**
     * 看学习资料10分钟
     * @param userInfo
     */
    public void studyTime(UserInfoForToken userInfo){
        StudentInfo studentInfo=studentInfoRespository.findById(userInfo.getUserId());//查询学生
        Integer count=qiandao(userInfo.getUserId(),"查看学习资料10分钟");
        IntegralRule integralRule=integralRuleRepository.findByRoleAndType("学生","查看资料10分钟");
        if (count<integralRule.getCount()){
            studentInfo.setIntegral(studentInfo.getIntegral()+integralRule.getIntegral());//加积分
            studentInfoRespository.save(studentInfo);
            //积分记录
            IntegralRecord integralRecord = new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                    "查看学习资料10分钟", "学生查看学习资料10分钟", userInfo.getUserId(), userInfo.getUserName(),
                    integralRule.getIntegral(), System.currentTimeMillis());
            integralRecordRepository.save(integralRecord);
            //系统信息
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String s = df.format(new Date());// new Date()为获取当前系统时间

            String messages = "您今日查看学习资料10分钟获得"+integralRule.getIntegral()+"积分。" + s;
            Message message = new Message(studentInfo.getId(), studentInfo.getName(), messages, "N");
            messageRepository.save(message);
        }
    }

    /**
     * 判断积分次数
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



}
