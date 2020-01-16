package cn.com.szedu.service;

import cn.com.szedu.constant.CourseStatus;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.CourseClassRelation;
import cn.com.szedu.entity.IntermediateTable.CoursePushResourceRelation;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.entity.IntermediateTable.StudentCourseRelation;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.StudentCourseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.*;
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

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.text.ParseException;
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
   /* @Resource
    private MessageRepository messageRepository;*/
    @Resource
    private TeacherInfoService teacherInfoService;
    @Resource
    private IAbsenceFromDutyRepository absenceFromDutyRepository;
    @Resource
    private IClassInfoMapper classInfoMapper;


    /**
     * 课堂签到
     * @param info
     * @param model
     */
    public boolean addAttendace(UserInfoForToken info, AttendanceModel model)throws TecherException {
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
        long nowtime=System.currentTimeMillis()/1000;
        Course course=courseRepository.findOneById(model.getCourseId());
        long sarttime=course.getStartTime()/1000;
        String time=timeStamp2Date(course.getStartTime(),null);
        IntegralRule integralRule=integralRuleRepository.findByRoleAndType("学生","上课签到");
        if(nowtime<=sarttime){//正常
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

            String messages = "你已在课堂上签到，获得"+integralRule.getIntegral()+" 积分。课堂信息："+course.getCourseTitle()+"；" +
                    "科目-"+course.getSubject()+"；\n" +
                    "类型-"+course.getLessonTypeName()+"；开课时间-"+time+"；课堂时长-"+course.getCourseTimeLength()+"分钟" ;
               /* Message message = new Message(studentInfo.getId(), studentInfo.getName(), messages, "N");
                messageRepository.save(message);*/
            MessageModel messageModel=new MessageModel(info.getUserId(), info.getUserName(), messages, "N");
            teacherInfoService.addMessage(info,messageModel);
            return true;
        }else {//迟到
            String messages = "你已在课堂上签到，课堂信息："+course.getCourseTitle()+"；" +
                    "科目-"+course.getSubject()+"；\n" +
                    "类型-"+course.getLessonTypeName()+"；开课时间-"+time+"；课堂时长-"+course.getCourseTimeLength()+"分钟" ;
               /* Message message = new Message(studentInfo.getId(), studentInfo.getName(), messages, "N");
                messageRepository.save(message);*/
            MessageModel messageModel=new MessageModel(info.getUserId(), info.getUserName(), messages, "N");
            teacherInfoService.addMessage(info,messageModel);
            return true;
        }
        //return true;//签到(不能再签到了)
    }

    /**
     * 课堂评分
     * @param info
     * @param score
     * @param courseId
     */
    public boolean addscore(UserInfoForToken info,Integer score,String courseId)throws UserServiceException {
        if (StringUtils.isEmpty(info) ||StringUtils.isEmpty(score) ||StringUtils.isEmpty(courseId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        StudentCourseRelation count= studentCourseScoreRepository.findByCourseIdAndStudentId(courseId,info.getUserId());//判断改学生有没有评论过次课堂
        if (count==null){//没评论过
            StudentCourseRelation studentCourseScore=new StudentCourseRelation();
            studentCourseScore.setCourseId(courseId);
            studentCourseScore.setStudentId(info.getUserId());
            studentCourseScore.setScorse(score);
            studentCourseScoreRepository.save(studentCourseScore);
        }else {//评论过，修改
            count.setCourseId(courseId);
            count.setStudentId(info.getUserId());
            count.setScorse(score);
            studentCourseScoreRepository.save(count);
        }
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
        return true;
    }

    /**
     * 我的课堂
     *
     * @param userInfo
     * @return
     * @throws TecherException
     */
    public Page<Course> getAllLesson(UserInfoForToken userInfo, MyAllLessionModel mmodel) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(mmodel)) {
            throw new TecherException(ResultCode.TOKEN_MISS_MSG);
        }
        Integer subjectId = null;
        if(mmodel.getSubjectId()!=null){
            subjectId = Integer.valueOf(mmodel.getSubjectId());
        }
       /* Calendar calendar = Calendar.getInstance();
        if (!StringUtils.isEmpty(mmodel.getTimeName())) {

            if (mmodel.getTimeName().equals("7")) {//一周
                //过去七天
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -7);
            } else if (mmodel.getTimeName().equals("1")) {//一个月
                //过去一个月
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -1);
            }
            if (mmodel.getTimeName().equals("3")) {//三个月
                //过去三个月
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -3);
            }
        }*/

        List<Course> list=new ArrayList<Course>();
        StudentCourseModel model=null;
        List<StudentCourseModel> models=new ArrayList<StudentCourseModel>();
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

        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "startTime"));
        Pageable pageable=new PageRequest(mmodel.getPageNum()-1,mmodel.getPageSize(),sort);
        Page<Course> course=courseRepository.findAll((Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            Path<Object> path = root.get("id");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (String s:courseId) {
                in.value(s);
            }
            if (courseId.size()>0) {
                predicateList.add(criteriaBuilder.and(criteriaBuilder.and(in)));
            }
            if(!StringUtils.isEmpty(mmodel.getSubjectId()) && !StringUtils.isEmpty(mmodel.getSubjectId())){
                predicateList.add(criteriaBuilder.equal(root.get("subjectId").as(Integer.class),mmodel.getSubjectId()));
              /* Integer subjectIds = Integer.valueOf(mmodel.getSubjectId());
                predicateList.add(criteriaBuilder.equal(root.get("subjectId").as(Integer.class),subjectIds));*/
            }
            if(!StringUtils.isEmpty(mmodel.getLessionType())){
                predicateList.add(criteriaBuilder.equal(root.get("lessonTypeName").as(String.class),mmodel.getLessionType()));
            }
            if(mmodel.getTime()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("startTime").as(long.class),mmodel.getTime()));
            }
            if (!StringUtils.isEmpty(mmodel.getTime())) {
                predicateList.add(criteriaBuilder.between(root.get("startTime"),mmodel.getTime(), System.currentTimeMillis()));
            }
            predicateList.add(criteriaBuilder.notEqual(root.get("status"),CourseStatus.UNPUBLISH.getName()));
            //predicateList.add(criteriaBuilder.(root.get("status"),"N"));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
       /* int count=classInfoMapper.countNum(subjectId,mmodel.getLessionType(),mmodel.getTime(), System.currentTimeMillis()
                ,userInfo.getUserId());*/
       /* for (Course c:course) {
            model=MappingEntity3ModelCoverter.CONVERTERFRONBACKCOURSESTUDENT(c);
          Integer count=  attendanceRepository.countByCourseIdAndStudentId(c.getId(),userInfo.getUserId());//判断是否签到
            if (count<=0){//不在考勤表
                Integer count2=absenceFromDutyRepository.countByCourseIdAndStudentId(c.getId(),userInfo.getUserId());//缺勤
                if (count2<=0){//(不在缺勤表)
                    model.setQiandao(true);//未签到
                }else {//学生缺勤
                    model.setQiandao(false);//签到
                }
            }else {
                model.setQiandao(false);//签到
            }
            list=courseRepository.findByIdInAndStatusIsNot(courseId,CourseStatus.UNPUBLISH.getName());
            if (list.size()<=0) {
                throw new TecherException(ResultCode.SELECT_NULL_MSG);
            }

            model.setCount(list.size());
            models.add(model);
        }
        System.out.println();
        PageInfo<StudentCourseModel> pageInfo=new PageInfo<StudentCourseModel>();
        pageInfo.setTotal(list.size());
        pageInfo.setList(models);
        pageInfo.setPageNum(mmodel.getPageNum());
        pageInfo.setPageSize(mmodel.getPageSize());
        int totalPage= list.size() % mmodel.getPageSize() == 0 ? list.size() / mmodel.getPageSize() : list.size() / mmodel.getPageSize() + 1;
      *//*  pageInfo.setPages((models.size()+mmodel.getPageSize()-1)/mmodel.getPageSize());
        pageInfo.setSize(mmodel.getPageSize());*//*
        pageInfo.setPages(totalPage);
        return pageInfo;*/
        return course;
    }


//课堂总数
    public Integer allCount(UserInfoForToken userInfo)throws UserServiceException{
        if (StringUtils.isEmpty(userInfo.getUserId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Integer count=classInfoMapper.countStudentLesson(userInfo.getUserId());
        return count;
    }
    /**
     * 学生端(学习资料)
     * @param userInfo
     * @param model
     * @return
     */
   public Page<CourseWare> getStudy(UserInfoForToken userInfo, MyAllLessionModel model)throws TecherException{

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

       List<String> resourseId=new ArrayList<String>();
       for (CoursePushResourceRelation cp: coursePushResourceRelations) {
            resourseId.add(cp.getPushresourceid());
       }
      // Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "create_time"));
       Pageable pageable=new PageRequest(model.getPageNum()-1,model.getPageSize());
       Page<CourseWare> course=courseWareRespository.findAll((Root<CourseWare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
           List<Predicate> predicateList = new ArrayList<>();
           Path<Object> path = root.get("id");
           CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
           for (String s:resourseId) {
               in.value(s);
           }
           if (resourseId.size()>0){
               predicateList.add(criteriaBuilder.and(criteriaBuilder.and(in)));
           }
           if(!StringUtils.isEmpty(model.getSubjectId())){
               predicateList.add(criteriaBuilder.equal(root.get("subject").as(Integer.class),model.getSubjectId()));
              /* Integer subjectIds = Integer.valueOf(mmodel.getSubjectId());
                predicateList.add(criteriaBuilder.equal(root.get("subjectId").as(Integer.class),subjectIds));*/
           }
           if(!StringUtils.isEmpty(model.getLessionType())){
               predicateList.add(criteriaBuilder.equal(root.get("type").as(String.class),model.getLessionType()));
           }

           return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
       },pageable);
       return course;
    }


    /**
     * 课堂详情
     *
     * @param userInfo
     * @param courseId
     * @return
     */
    public TeacherLessonModel getCourseDetail(UserInfoForToken userInfo, String courseId) throws TecherException {
        ClassModel classModel = new ClassModel();
        List<CoursePushResourceRelation> pushcourseWares=coursePushWareRelationRepository.findByCourseIdAndPush(courseId,"Y");;//推送资源资源
        if (pushcourseWares.size()<=0) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }
        Course course2 = courseRepository.findFirstById(courseId);//课堂详情
        /*if (StringUtils.isEmpty(course2)) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }*/
        TeacherLessonModel model = MappingEntity3ModelCoverter.CONVERTERFROMBACKLESSONMODEL(course2);
        List<String> wareId=new ArrayList<String>();
        for (CoursePushResourceRelation p:pushcourseWares ) {
            wareId.add(p.getPushresourceid());
        }
        //资源
        List<CourseWare> pushcourseWares2 = courseWareRespository.findByIdIn(wareId);//推送资源资源
       // model.setCourseWares(courseWares);
      /*  if (pushcourseWares2.size()<=0) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }*/
        model.setPushcourseWares(pushcourseWares2);

        if (course2.getStatus().equalsIgnoreCase(CourseStatus.FINISH.getName())) {//结束显示课堂评分
            String detaTime = timeStamp2Date(course2.getEndTime(),null);
            Boolean ok = isYesToday(detaTime);
            if (ok) {//当前时间为结束时间的后一天
                Integer sum = studentCourseScoreRepository.countByCourseId(courseId);
                Double count = classInfoMapper.getCourseAvg(courseId);
                StudentCourseScoreModel model1 = new StudentCourseScoreModel();
                model1.setScoure1(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 1));//一颗心
                model1.setScoure2(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 2));//两颗心
                model1.setScoure3(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 3));//三颗心
                model1.setScoure4(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 4));//四颗心
                model1.setScoure5(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 5));//五颗心
                model1.setAvg(count);
                model1.setSum(sum);//总人数
            }

        }
        return model;
    }










    /**
     * 根据id查看资源详情
     * @param userInfo
     * @param id
     * @return
     */
    public CourseWare getCourseWareDetail(UserInfoForToken userInfo,String id){
           CourseWare courseWare=courseWareRespository.findByid(id);
           StudentInfo studentInfo=studentInfoRespository.findFirstById(userInfo.getUserId());//查询学生
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
               /* Message message = new Message(studentInfo.getId(), studentInfo.getName(), messages, "N");
                messageRepository.save(message);*/
                MessageModel messageModel=new MessageModel(studentInfo.getId(), studentInfo.getName(), messages, "N");
                teacherInfoService.addMessage(userInfo,messageModel);
            }
           return courseWare;
    }

    /**
     * 看学习资料10分钟
     * @param userInfo
     */
    public Boolean studyTime(UserInfoForToken userInfo){
        StudentInfo studentInfo=studentInfoRespository.findFirstById(userInfo.getUserId());//查询学生
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
           /* Message message = new Message(studentInfo.getId(), studentInfo.getName(), messages, "N");
            messageRepository.save(message);*/
            MessageModel messageModel=new MessageModel(studentInfo.getId(), studentInfo.getName(), messages, "N");
            teacherInfoService.addMessage(userInfo,messageModel);
            return true;
        }
        return false;
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


    /**
     * 时间戳转换成日期格式字符串
     * @param second 精确到秒的字符串
     * @param
     * @return
     */
    public static String timeStamp2Date(long second,String format) {
        String seconds=String.valueOf(second);
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    //是不是昨天
    public static boolean isYesToday(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = new Date(System.currentTimeMillis());
        date2.setHours(0);
        date2.setMinutes(0);
        date2.setSeconds(0);
        int tmp = 86400000;

        long day1 = date.getTime() / tmp;
        long day2 = date2.getTime() / tmp;

        if (day2 - day1 >= 1) {
            return true;
        } else {
            return false;
        }
    }
}
