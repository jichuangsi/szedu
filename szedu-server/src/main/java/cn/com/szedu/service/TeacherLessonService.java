package cn.com.szedu.service;

import cn.com.szedu.constant.CourseStatus;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.*;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.CourseResouceModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.*;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.*;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TeacherLessonService {
    @Resource
    private IAttendanceRepository attendanceRepository;//考勤
    /*  @Resource
      private ITeacherCouseRelationRepository teacherCouseRelationRepository;*/
    @Resource
    private ICourseRepository courseRepository;//课堂
    @Resource
    private IClassCourseRelationRepository classCourseRelationRepository;//班级课堂
    @Resource
    private IClassInfoRepository classInfoRepository;//班级
    @Resource
    private IStudentClassRelationRepository studentClassRelationRepository;//学生班级
    @Resource
    private IStudentInfoRespository studentInfoRespository;//学生
    @Resource
    private IAbsenceFromDutyRepository absenceFromDutyRepository;//缺勤
    @Resource
    private ICoursePushWareRelationRepository coursePushWareRelationRepository;//课堂推送资源
    @Resource
    private ICourseWareRelationRepository courseWareRelationRepository;//课堂资源
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private IStudentCourseScoreRepository studentCourseScoreRepository;//学生课堂评分
    @Resource
    private ICourseWareRespository courseWareRespository;//资源
    @Resource
    private IClassInfoMapper classInfoMapper;//班级
    @Resource
    private MenuRepository menuRepository;//科目章节
    @Resource
    private IIntegralRuleRepository integralRuleRepository;//积分规则
    @Resource
    private ICourseUserRelationRepository courseUserRelationRepository;//老师课堂
    @Resource
    private TeacherInfoService teacherInfoService;
    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;
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
       /* Calendar c = Calendar.getInstance();
        if (!StringUtils.isEmpty(mmodel.getTimeName())) {
            if (mmodel.getTimeName().equals("7")) {//一周
                //过去七天
                c.setTime(new Date());
                c.add(Calendar.DATE, -7);
            } else if (mmodel.getTimeName().equals("1")) {//一个月
                //过去七天
                c.setTime(new Date());
                c.add(Calendar.MONTH, -1);
            }
            if (mmodel.getTimeName().equals("3")) {//三个月
                //过去七天
                c.setTime(new Date());
                c.add(Calendar.MONTH, -3);
            }
        }*/
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "startTime"));
        Pageable pageable = new PageRequest(mmodel.getPageNum() - 1, mmodel.getPageSize(), sort);
        Page<Course> course = courseRepository.findAll((Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("teacherId").as(String.class), userInfo.getUserId()));
            if (!StringUtils.isEmpty(mmodel.getSubjectId())) {
                predicateList.add(criteriaBuilder.equal(root.get("subjectId").as(Integer.class), mmodel.getSubjectId()));
            }
            if (!StringUtils.isEmpty(mmodel.getLessionType())) {
                predicateList.add(criteriaBuilder.equal(root.get("lessonTypeName").as(String.class), mmodel.getLessionType()));
            }
           /* if(mmodel.getTime()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("startTime").as(long.class),mmodel.getTime()));
            }*/
            if (!StringUtils.isEmpty(mmodel.getTimeName())) {
                predicateList.add(criteriaBuilder.between(root.get("startTime"), mmodel.getTime(), System.currentTimeMillis()));
            }
            //predicateList.add(criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startTime"))));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return course;
    }

    public Integer allCount(UserInfoForToken userInfo) {
        Integer count = courseRepository.countByTeacherId(userInfo.getUserId());
        return count;
    }

    /**
     * 新增课堂
     *
     * @param userInfo
     * @param model
     * @throws TecherException
     */
    public void addLesson(UserInfoForToken userInfo, TeacherLessonModel model) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(model)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        Menu menus=menuRepository.findFirstById(model.getSubjectId());//科目
        Menu menu=menuRepository.findFirstById(model.getChapterId());//章节
        if (StringUtils.isEmpty(model.getSubjectName())){
            model.setSubjectName(menus.getTitle());
        }
        if (StringUtils.isEmpty(model.getChapter())){
            model.setChapter(menu.getTitle());
        }
        Course course = courseRepository.save(MappingEntity3ModelCoverter.CONVERTERFROMBACKLESSON(userInfo, model));


        List<CourseClassRelation> courceClass = new ArrayList<CourseClassRelation>();
       /* TeacherCourseRelation teacherCourseRelation=new TeacherCourseRelation();//老师课堂
        teacherCourseRelation.setCourseId(course.getId());
        teacherCourseRelation.setTecherId(userInfo.getUserId());
        teacherCouseRelationRepository.save(teacherCourseRelation);*/
        //中间表
        CourseUserRelation courseUserRelation = new CourseUserRelation(course.getId(), userInfo.getUserId());
        courseUserRelationRepository.save(courseUserRelation);
        if (model.getClassModelList().size() > 0) {//班级课堂
            model.getClassModelList().forEach(c -> {
                CourseClassRelation relation = new CourseClassRelation();
                relation.setClassId(c.getClassId());
                relation.setCourseId(course.getId());
                courceClass.add(relation);
            });
            classCourseRelationRepository.saveAll(courceClass);
        }
        List<CourseResourceRelation> courceware = new ArrayList<CourseResourceRelation>();
        if (model.getCourseWares().size() > 0) {
            model.getCourseWares().forEach(c -> {//上课资源
                CourseResourceRelation crr = new CourseResourceRelation();
                crr.setCourseId(course.getId());
                crr.setResourceid(c.getId());
                courceware.add(crr);
            });
            courseWareRelationRepository.saveAll(courceware);
        }
        List<CoursePushResourceRelation> courcewarepush = new ArrayList<CoursePushResourceRelation>();
        if (model.getPushcourseWares().size() > 0) {
            model.getPushcourseWares().forEach(cc -> {//推送资源
                CoursePushResourceRelation cprr = new CoursePushResourceRelation();
                cprr.setCourseId(course.getId());
                cprr.setPushresourceid(cc.getId());
                courcewarepush.add(cprr);
            });

            coursePushWareRelationRepository.saveAll(courcewarepush);
        }
        OpLog opLog = new OpLog(userInfo.getUserName(), "添加", "添加课堂");
        opLogRepository.save(opLog);

    }

    /**
     * 删除课堂
     *
     * @param userInfo
     * @param lessionId
     */
    public void deleteLession(UserInfoForToken userInfo, String lessionId) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(lessionId)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        courseRepository.deleteById(lessionId);
        coursePushWareRelationRepository.deleteByCourseId(lessionId);
        courseWareRelationRepository.deleteByCourseId(lessionId);
        classCourseRelationRepository.deleteByCourseId(lessionId);
        //teacherCouseRelationRepository.deleteByCourseIdAndTecherId(lessionId, userInfo.getUserId());
        courseUserRelationRepository.deleteByCourseId(lessionId);
        OpLog opLog = new OpLog(userInfo.getUserName(), "删除", "删除课堂");
        opLogRepository.save(opLog);
    }

    /**
     * 复制课堂
     *
     * @return
     */
    public void copyLession(UserInfoForToken userInfo, String id) throws TecherException {
        if (StringUtils.isEmpty(userInfo)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        Course course = courseRepository.findFirstById(id);
        Course course1 = (Course) course.clone();//复制一门一样的课
        course1.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        Course cc = courseRepository.save(course1);
        List<CourseResourceRelation> courseWare = courseWareRelationRepository.findByCourseId(id);//上课资源
        List<CoursePushResourceRelation> cpw = coursePushWareRelationRepository.findByCourseId(id);//推送资源
        List<CourseClassRelation> ccr = classCourseRelationRepository.findByCourseId(id);//课堂班级
        CourseUserRelation cur = courseUserRelationRepository.findByCourseId(id);//课堂老师
        if (cur != null) {
            //中间表
            CourseUserRelation courseUserRelation = new CourseUserRelation(course1.getId(), course1.getFounderId());
            courseUserRelationRepository.save(courseUserRelation);
        }
        if (courseWare.size() > 0) {
            for (CourseResourceRelation cw : courseWare) {
                CourseResourceRelation c = new CourseResourceRelation();
                c.setCourseId(cc.getId());
                c.setResourceid(cw.getResourceid());
                courseWareRelationRepository.save(c);
            }
        }
        if (cpw.size() > 0) {
            for (CoursePushResourceRelation cpw1 : cpw) {
                CoursePushResourceRelation c = new CoursePushResourceRelation();
                c.setCourseId(cc.getId());
                c.setPushresourceid(cpw1.getPushresourceid());
                coursePushWareRelationRepository.save(c);
            }
            if (ccr.size() > 0) {
                for (CourseClassRelation cr : ccr) {
                    CourseClassRelation c = new CourseClassRelation();
                    c.setCourseId(cc.getId());
                    c.setClassId(cr.getClassId());
                    classCourseRelationRepository.save(c);
                }
            }
        }
    }

    /**
     * 编辑课堂
     *
     * @param userInfo
     * @param course
     */
    public void updateCourse(UserInfoForToken userInfo, TeacherLessonModel course) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(course)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        Course course1 = courseRepository.findFirstById(course.getLessonId());
        course1.setContent(course.getLessonContent());
        course1.setCourseTitle(course.getLessonName());
        course1.setTeachAddress(course.getTeachAddress());
        course1.setStartTime(course.getTeachTime());
        course1.setCourseTimeLength(course.getTeachTimeLength());
        courseRepository.save(course1);
    }

    /**
     * 改变课堂状态
     *
     * @param userInfo
     * @param couseId
     * @param staus
     * @return
     */
    public void updateCourseStatus(UserInfoForToken userInfo, String couseId, String staus) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(couseId) || StringUtils.isEmpty(staus)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        Course course1 = courseRepository.findFirstById(couseId);
        if (StringUtils.isEmpty(course1)) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }
        if (staus.equalsIgnoreCase(CourseStatus.FINISH.getName())) {//结束课堂
            course1.setEndTime(System.currentTimeMillis());
        }
        long time = System.currentTimeMillis() / 1000;
        if (staus.equalsIgnoreCase(CourseStatus.HAVING.getName())) {//上课
            long stime = course1.getStartTime() / 1000;
            if (time > stime) {
                throw new TecherException(ResultCode.COURSE_IS_INVALID);
            }
        }
        course1.setStatus(staus.toUpperCase());
        courseRepository.save(course1);
        if (staus.equalsIgnoreCase(CourseStatus.PUBLISH.getName())) {//发布课堂====系统消息
            Menu menu = menuRepository.findFirstById(course1.getSubjectId());
            String staertTime = getDateToString(course1.getStartTime());
            String message = "您刚发布了 1 门新的课堂。课堂信息：" + course1.getCourseTitle() + "；科目-" + menu.getTitle() + "；类\n" +
                    ":" + course1.getLessonTypeName() + "；开课时间-" + staertTime + "；课堂时长-" + course1.getCourseTimeLength() + " 分钟。" +
                    "同时，已向指定班级学生发送上课通知";

            MessageModel messageModel = new MessageModel(userInfo.getUserId(), userInfo.getUserName(), message, "N");
            teacherInfoService.addMessage(userInfo, messageModel);

            TeacherInfo teacherInfo = classInfoMapper.getTeacherByCourse(couseId);//根据课堂查出老师

            List<ClassInfo> classInfos = classInfoMapper.getClassByCourse(couseId);
            if (classInfos.size() <= 0) {
            }
            for (ClassInfo s : classInfos) {//班级
                String messagec = teacherInfo.getName() + "老师刚发布了 1 门新的课堂。课堂信息：" + course1.getCourseTitle() + "；<br />科目-" + menu.getTitle() + "；类\n" +
                        ":" + course1.getLessonTypeName() + "；开课时间-" + staertTime + "；课堂时长-" + course1.getCourseTimeLength() + " 分钟。";

                MessageModel messageModel1 = new MessageModel(s.getId(), s.getClassName(), messagec, "N");
                teacherInfoService.addMessage(userInfo, messageModel1);
            }

            coursePushWareRelationRepository.updateByCourseId(couseId);//把推送资源改成已推送
        }
        if (staus.equalsIgnoreCase(CourseStatus.FINISH.getName())) {
            List<String> classId = new ArrayList<String>();
            List<String> said = new ArrayList<String>();//
            AbsenceFromDuty aduty = new AbsenceFromDuty();
            List<AttendanceInClass> alist = attendanceRepository.findByCourseId(couseId);
            for (AttendanceInClass ac : alist) {
                said.add(ac.getSid());
            }
            List<CourseClassRelation> cclist = classCourseRelationRepository.findByCourseId(couseId);//根据courseId查班级
            for (CourseClassRelation cc : cclist) {
                classId.add(cc.getClassId());
            }
            List<StudentClassRelation> sclist = studentClassRelationRepository.findByClassIdIn(classId);//根据classid查学生
            for (StudentClassRelation sc : sclist) {
                if (!said.contains(sc.getStudentId())) {
                    StudentInfo info = studentInfoRespository.findFirstByid(sc.getStudentId());
                    ClassInfo cinfo = classInfoRepository.findExistById(sc.getClassId());
                    aduty.setCourseId(couseId);
                    aduty.setClassName(course1.getCourseTitle());
                    aduty.setStudentId(info.getStudentId());//学号
                    aduty.setSid(sc.getStudentId());//学生id
                    aduty.setClassName(cinfo.getClassName());
                    absenceFromDutyRepository.save(aduty);//添加缺勤学生
                }
            }
        }
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
        List<ClassModel> classModelList = new ArrayList<ClassModel>();//班级
       /* List<CourseWare> courseWares=new ArrayList<CourseWare>();//上课资源
        List<CourseWare> pushcourseWares=new ArrayList<CourseWare>();//推送资源资源*/
        List<CourseClassRelation> acr = classCourseRelationRepository.findByCourseId(courseId);//根据课堂Id查询班级
        for (CourseClassRelation course : acr) {
            ClassInfo classInfo = classInfoRepository.findExistById(course.getClassId());
            classModel.setClassId(course.getClassId());
            classInfo.setClassName(classInfo.getClassName());
            classModelList.add(classModel);
        }
        Course course2 = courseRepository.findFirstById(courseId);
        TeacherLessonModel model = MappingEntity3ModelCoverter.CONVERTERFROMBACKLESSONMODEL(course2);
        model.setClassModelList(classModelList);
        //资源
        List<CourseWare> courseWares = getResoureByCourse(userInfo, courseId);//上课资源
        List<CourseWare> pushcourseWares = getPushResoureByCourse(userInfo, courseId);//推送资源资源
        model.setCourseWares(courseWares);
        model.setPushcourseWares(pushcourseWares);

        if (course2.getStatus().equalsIgnoreCase(CourseStatus.FINISH.getName())) {//结束显示课堂评分
            String detaTime = getDateToString(course2.getEndTime());
            Boolean ok = isYesToday(detaTime);
            if (ok) {//当前时间为结束时间的后一天
                //List<StudentCourseScore> scores=studentCourseScoreRepository.findByCourseId(courseId);
                Integer sum = studentCourseScoreRepository.countByCourseId(courseId);
                Double count = classInfoMapper.getCourseAvg(courseId);
                StudentCourseScoreModel model1 = new StudentCourseScoreModel();
                model1.setScoure1(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 1));//一颗心
                model1.setScoure2(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 2));//两颗心
                model1.setScoure3(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 3));//三颗心
                model1.setScoure4(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 4));//四颗心
                model1.setScoure5(studentCourseScoreRepository.countByCourseIdAndScorse(courseId, 5));//五颗心
         /*   Double count= Double.valueOf(((model1.getScoure1()*1+model1.getScoure2()*2+model1.getScoure3()*3+model1.getScoure4()*4
                    +model1.getScoure5()*5)/sum));*/
                model1.setAvg(count);
                // List<StudentCourseRelation> list=studentCourseScoreRepository.findByCourseId(courseId);
                model1.setSum(sum);//总人数
            }

        }
        return model;
    }


    /**
     * 学生考勤
     *
     * @return
     */
    public PageInfo<AttendanceCourseModel> getAttendanceByCourse(UserInfoForToken userInfo, String couseId, int pageNum1, int pageSize1) throws TecherException {
        int pageNum = pageNum1 == 0 ? 1 : pageNum1;
        int pageSize = pageSize1 == 0 ? 100 : pageSize1;
        List<String> classId = new ArrayList<String>();
        List<AttendanceCourseModel> models = new ArrayList<AttendanceCourseModel>();
        AttendanceCourseModel model = new AttendanceCourseModel();
        List<AttendanceInClass> list = attendanceRepository.findByCourseId(couseId);//根据课堂查询签到学生
        List<AbsenceFromDuty> dutyList = absenceFromDutyRepository.findByCourseId(couseId);//根据课堂缺勤学生
        for (AttendanceInClass ac : list) {//签到学生
            model.setStudentName(ac.getStudentName());
            model.setStudentId(ac.getStudentId());
            model.setClassName(ac.getClassName());
            model.setStaus("Y");//到课状态
            model.setAtime(ac.getAtime());
            models.add(model);
        }
        for (AbsenceFromDuty ad : dutyList) {//缺勤学生
            model.setStudentName(ad.getStudentName());
            model.setStudentId(ad.getStudentId());
            model.setClassName(ad.getClassName());
            model.setStaus("N");//缺勤
            model.setAtime(ad.getAtime());
            models.add(model);
        }
        model.setSum(list.size() + dutyList.size());
        model.setDutystudent(dutyList.size());
        model.setTrueto(list.size());
        models.add(model);

        PageInfo<AttendanceCourseModel> pageinfo = new PageInfo<>();
        pageinfo.setTotal(models.size());
        pageinfo.setPageSize(pageSize);
        pageinfo.setPageNum(pageNum);
        pageinfo.setList(models);
        return pageinfo;
    }


    /**
     * 上传课程图片
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String addCourseImg(MultipartFile file) throws IOException {
        Course course = new Course();
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
        course.setPortrait(uri+fileName);
       /* course.setCoursePic(file.getBytes());*/
        Course course2 = courseRepository.save(course);
        return course2.getId();
    }

    /**
     * 上课资源
     *
     * @param userInfo
     * @param couseId
     * @return
     * @throws TecherException
     */
    public List<CourseWare> getResoureByCourse(UserInfoForToken userInfo, String couseId) throws TecherException {
        List<String> wareId = new ArrayList<String>();
        List<CourseWare> list = null;
        List<CourseResourceRelation> resourceRelation = courseWareRelationRepository.findByCourseId(couseId);
        for (CourseResourceRelation c : resourceRelation) {
            wareId.add(c.getResourceid());
        }
        if (wareId.size() > 0) {
            list = courseWareRespository.findByidIn(wareId);
        }
        return list;
    }

    /**
     * 推送资源
     *
     * @param userInfo
     * @param couseId
     * @return
     * @throws TecherException
     */
    public List<CourseWare> getPushResoureByCourse(UserInfoForToken userInfo, String couseId) throws TecherException {
        List<String> wareId = new ArrayList<String>();
        List<CourseWare> list = null;
        List<CoursePushResourceRelation> resourceRelation = coursePushWareRelationRepository.findByCourseId(couseId);
        for (CoursePushResourceRelation c : resourceRelation) {
            wareId.add(c.getPushresourceid());
        }
        if (wareId.size() > 0) {
            list = courseWareRespository.findByidIn(wareId);
        }
        return list;
    }

    /**
     * 我的资源
     *
     * @param userInfo
     * @return
     * @throws TecherException
     */
    public List<CourseWare> getMyResoure(UserInfoForToken userInfo, String type) throws TecherException {
        List<String> wareId = new ArrayList<String>();
        List<CourseWare> list = courseWareRespository.findByTeacheridAndIsCheckAndType(userInfo.getUserId(), "2", type);
        return list;
    }

    /**
     * ppt
     *
     * @param userInfo
     * @return
     * @throws TecherException
     */
    public CourseResouceModel getMyResourePpt(UserInfoForToken userInfo, String courseId) throws TecherException {
        List<String> wareId = new ArrayList<String>();
        CourseResouceModel model = new CourseResouceModel();
        List<CourseWare> ppt = new ArrayList<CourseWare>();
        List<CourseWare> vedio = new ArrayList<CourseWare>();
        List<CourseWare> shi = new ArrayList<CourseWare>();
        List<CourseWare> pic = new ArrayList<CourseWare>();
        List<CourseResourceRelation> list = courseWareRelationRepository.findByCourseId(courseId);
        if (list.size() <= 0) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }
        for (CourseResourceRelation c : list) {
            wareId.add(c.getResourceid());
        }
        List<CourseWare> courseWares = courseWareRespository.findByIdIn(wareId);
        if (courseWares.size() <= 0) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }
        for (CourseWare cw : courseWares) {
            //截取文件名
            String oriName = cw.getFilename().substring(cw.getFilename().lastIndexOf(".") + 1);
            if (oriName.equalsIgnoreCase("ppt")) {//ppt
                ppt.add(cw);
            } else if (oriName.equalsIgnoreCase("avi") ||
                    oriName.equalsIgnoreCase("rmvb") ||
                    oriName.equalsIgnoreCase("mp4")) {//视频
                vedio.add(cw);
            } else if (oriName.equalsIgnoreCase("bmp") ||
                    oriName.equalsIgnoreCase("png") ||
                    oriName.equalsIgnoreCase("jpg")) {//图片
                pic.add(cw);
            } else {//ppt
                shi.add(cw);
            }
            model.setPpt(ppt);
            model.setVedio(vedio);
            model.setShi(shi);
            model.setPic(pic);
        }

        return model;
    }


    /**
     * 教师等级
     *
     * @param
     * @return
     */
  /*  public String getMyGrade(UserInfoForToken userInfo) {
        //List<TeacherCourseRelation> list = teacherCouseRelationRepository.findAllByTecherId(userInfo.getUserId());
        List<CourseUserRelation> list = courseUserRelationRepository.findAllByUid(userInfo.getUserId());
        IntegralRule integralRule = integralRuleRepository.findByRoleAndType("老师", "评分");
        IntegralRule jin = integralRuleRepository.findByRoleAndType("老师", "金牌");
        IntegralRule yin = integralRuleRepository.findByRoleAndType("老师", "银牌");
        IntegralRule tong = integralRuleRepository.findByRoleAndType("老师", "铜牌");
        Double score = integralRule.getIntegral().doubleValue();
        Double avg = 0.d;
        Integer sum = 0;//记录分数超过4.0的次数
        String name = "普通";
        if (list.size() < 10) {
        }
        for (CourseUserRelation t : list) {
            avg = classInfoMapper.getCourseAvg(t.getCourseId());
            if (avg >= score) {
                sum++;
            }
        }
       *//* for (TeacherCourseRelation t : list) {
            avg = classInfoMapper.getCourseAvg(t.getCourseId());
            if (avg >= score) {
                sum++;
            }
        }*//*
        if (sum >= jin.getCount()) {
            name = "金牌";
        } else if (sum >= yin.getCount()) {
            name = "银牌";
        } else if (sum >= tong.getCount()) {
            name = "铜牌";
        }
        return name;
    }*/


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

    //时间戳转化为时间（毫秒）
    public static String getDateToString(long milSecond) {
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            Date date = new Date(milSecond * 1000);
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 时间戳转换成日期格式字符串
     *
     * @param second 精确到秒的字符串
     * @param
     * @return
     */
    public static String timeStamp2Date(long second, String format) {
        String seconds = String.valueOf(second);
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

}
