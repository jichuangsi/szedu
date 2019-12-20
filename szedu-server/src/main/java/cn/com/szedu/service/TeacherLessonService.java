package cn.com.szedu.service;

import cn.com.szedu.constant.CourseStatus;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.CourseClassRelation;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.entity.IntermediateTable.TeacherCourseRelation;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceCourseModel;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.model.teacher.ClassModel;
import cn.com.szedu.model.teacher.TeacherLessonModel;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.IClassCourseRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ITeacherCouseRelationRepository;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class TeacherLessonService {
    @Resource
    private IAttendanceRepository attendanceRepository;
    @Resource
    private ITeacherCouseRelationRepository teacherCouseRelationRepository;
    @Resource
    private ICourseRepository courseRepository;
    @Resource
    private IClassCourseRelationRepository classCourseRelationRepository;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private IStudentClassRelationRepository studentClassRelationRepository;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Resource
    private IAbsenceFromDutyRepository absenceFromDutyRepository;
    @Resource
    private ICourseWareRespository courseWareRespository;

    /**
     * 我的课堂
     * @param userInfo
     * @param subject
     * @param lessionType
     * @param time
     * @param pageNum1
     * @param pageSize1
     * @return
     * @throws TecherException
     */
    public PageInfo<TeacherLessonModel> getAllLesson(UserInfoForToken userInfo, String subject, String lessionType, Date time, int pageNum1, int pageSize1)throws TecherException {
    if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(lessionType)
            ||StringUtils.isEmpty(time)){throw  new TecherException(ResultCode.TOKEN_MISS_MSG);}
        int pageNum=pageNum1==0?1:pageNum1;
        int pageSize=pageSize1==0?1:pageSize1;
        List<String> classId=new ArrayList<String>();
        List<TeacherLessonModel> models=new ArrayList<TeacherLessonModel>();
        TeacherLessonModel model=new TeacherLessonModel();
        Course course=null;
        List<TeacherCourseRelation> rlist=teacherCouseRelationRepository.findAllByTecherId(userInfo.getUserId());//老师课堂关系
        if (rlist.size()<=0){throw  new TecherException(ResultCode.SELECT_NULL_MSG);}
        for (TeacherCourseRelation tc:rlist) {
            classId.add(tc.getCourseId());
        }
        if (classId.size()<=0){throw  new TecherException(ResultCode.SELECT_NULL_MSG);}
        //老师的课堂
        List<Course> list=courseRepository.findBySubjectAndLessonTypeNameAndStartTimeAndIdIn(subject,lessionType,time,classId);
        for (Course corse:list) {
            model=MappingEntity3ModelCoverter.CONVERTERFROMBACKLESSONMODEL(corse);
            model.setSum(list.size());
            models.add(model);
        }
        PageInfo<TeacherLessonModel> pageinfo=new PageInfo<>();
        pageinfo.setTotal(list.size());
        pageinfo.setPageSize(pageSize);
        pageinfo.setPageNum(pageNum);
        pageinfo.setList(models);
    return pageinfo;
    }

    /**
     * 删除课堂
     * @param userInfo
     * @param lessionId
     */
    public void deleteLession(UserInfoForToken userInfo, String lessionId)throws TecherException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(lessionId)){throw  new TecherException(ResultCode.PARAM_MISS_MSG);}
        courseRepository.deleteByid(lessionId);
    }

    /**
     * 复制课堂
     * @return
     */
    public TeacherLessonModel copyLession(UserInfoForToken userInfo, Course course)throws TecherException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(course)){throw  new TecherException(ResultCode.PARAM_MISS_MSG);}
        TeacherLessonModel model=MappingEntity3ModelCoverter.CONVERTERFROMBACKLESSONMODEL(course);
        return model;
    }

    /**
     * 改变课堂状态
     * @param userInfo
     * @param couseId
     * @param staus
     * @return
     */
    public void updateCourseStatus(UserInfoForToken userInfo,String couseId,String staus)throws TecherException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(couseId)|| StringUtils.isEmpty(staus)){
            throw  new TecherException(ResultCode.PARAM_MISS_MSG);}
            Course course1=courseRepository.findFirstByid(couseId);
            if (StringUtils.isEmpty(course1)){throw new TecherException(ResultCode.SELECT_NULL_MSG);}
            if (staus.equalsIgnoreCase(CourseStatus.FINISH.getName())){
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Timestamp d = new Timestamp(System.currentTimeMillis());
                course1.setEndTime(d);
            }
            course1.setStatus(staus);
            courseRepository.save(course1);
            if (staus.equalsIgnoreCase(CourseStatus.FINISH.getName())){
                List<String> classId=new ArrayList<String>();
                List<String> said=new ArrayList<String>();//签到学生
                AbsenceFromDuty aduty=new AbsenceFromDuty();
                List<AttendanceInClass> alist=attendanceRepository.findByCourseId(couseId);
                for (AttendanceInClass ac:alist){
                    said.add(ac.getSid());
                }
                List<CourseClassRelation> cclist=classCourseRelationRepository.findByCourseId(couseId);//根据courseId查班级
                for (CourseClassRelation cc:cclist) {
                    classId.add(cc.getClassId());
                }
                List<StudentClassRelation> sclist=studentClassRelationRepository.findByClassIdIn(classId);//根据classid查学生
                for (StudentClassRelation sc:sclist) {
                    if (!said.contains(sc.getStudentId())){
                        StudentInfo info=studentInfoRespository.findFirstByid(sc.getStudentId());
                        ClassInfo cinfo=classInfoRepository.findExistById(sc.getClassId());
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
     * @param userInfo
     * @param courseId
     * @return
     */
    public TeacherLessonModel getCourseDetail(UserInfoForToken userInfo,String courseId){
        ClassModel classModel=new ClassModel();
        List<ClassModel> classModelList=new ArrayList<ClassModel>();
        List<CourseClassRelation> acr=classCourseRelationRepository.findByCourseId(courseId);//根据课堂Id查询班级
        for (CourseClassRelation course: acr) {
            ClassInfo classInfo=classInfoRepository.findExistById(course.getClassId());
            classModel.setClassId(course.getClassId());
            classInfo.setClassName(classInfo.getClassName());
            classModelList.add(classModel);
        }
        Course course2=courseRepository.findFirstByid(courseId);
        TeacherLessonModel model=MappingEntity3ModelCoverter.CONVERTERFROMBACKLESSONMODEL(course2);
        model.setClassModelList(classModelList);
        //资源

        if (course2.getStatus().equalsIgnoreCase(CourseStatus.FINISH.getName())){//结束
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Timestamp d = new Timestamp(System.currentTimeMillis());
        }
        return model;
    }




    /**
     * 学生考勤
     * @return
     */
    public PageInfo<AttendanceCourseModel> getAttendanceByCourse(UserInfoForToken userInfo, String couseId, int pageNum1, int pageSize1)throws TecherException{
        int pageNum=pageNum1==0?1:pageNum1;
        int pageSize=pageSize1==0?100:pageSize1;
        List<String> classId=new ArrayList<String>();
        List<AttendanceCourseModel> models=new ArrayList<AttendanceCourseModel>();
        AttendanceCourseModel model=new AttendanceCourseModel();
        List<AttendanceInClass> list=attendanceRepository.findByCourseId(couseId);//根据课堂查询签到学生
        List<AbsenceFromDuty> dutyList=absenceFromDutyRepository.findByCourseId(couseId);//根据课堂缺勤学生
        for (AttendanceInClass ac:list) {//签到学生
            model.setStudentName(ac.getStudentName());
            model.setStudentId(ac.getStudentId());
            model.setClassName(ac.getClassName());
            model.setStaus("Y");//到课状态
            model.setAtime(ac.getAtime());
            models.add(model);
        }
        for (AbsenceFromDuty ad:dutyList) {//缺勤学生
            model.setStudentName(ad.getStudentName());
            model.setStudentId(ad.getStudentId());
            model.setClassName(ad.getClassName());
            model.setStaus("N");//到课状态
            model.setAtime(ad.getAtime());
            models.add(model);
        }
        model.setSum(list.size()+dutyList.size());
        model.setDutystudent(dutyList.size());
        model.setTrueto(list.size());
        models.add(model);

        PageInfo<AttendanceCourseModel> pageinfo=new PageInfo<>();
        pageinfo.setTotal(models.size());
        pageinfo.setPageSize(pageSize);
        pageinfo.setPageNum(pageNum);
        pageinfo.setList(models);
        return pageinfo;
    }


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
    }
}
