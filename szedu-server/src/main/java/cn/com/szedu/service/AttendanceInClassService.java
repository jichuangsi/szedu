package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.AttendanceInClass;
import cn.com.szedu.entity.Course;
import cn.com.szedu.entity.IntermediateTable.ClassRelation;
import cn.com.szedu.entity.IntermediateTable.CourseClassRelation;
import cn.com.szedu.entity.IntermediateTable.CourseUserRelation;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.entity.StudentInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.repository.IAttendanceRepository;
import cn.com.szedu.repository.IClassInfoRepository;
import cn.com.szedu.repository.ICourseRepository;
import cn.com.szedu.repository.IStudentInfoRespository;
import cn.com.szedu.repository.IntermediateTableRepository.IClassCourseRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ICourseUserRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.TecherClassRelationRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class AttendanceInClassService {
    @Resource
    private IAttendanceRepository attendanceRepository;
    @Resource
    private TecherClassRelationRepository techerClassRelationRepository;
    /*@Resource
    private ITeacherCouseRelationRepository teacherCouseRelationRepository;*/
    @Resource
    private IClassCourseRelationRepository classCourseRelationRepository;
    @Resource
    private IStudentClassRelationRepository studentClassRelationRepository;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Resource
    private ICourseRepository courseRepository;
   /* @Resource
    private IClassInfoMapper classInfoMapper;*/
   @Resource
   private IClassInfoRepository classInfoRepository;
   @Resource
   private ICourseUserRelationRepository courseUserRelationRepository;


    /**
     * 考勤
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     * @throws UserServiceException
     */
    public PageInfo<AttendanceModel> getAttendance(UserInfoForToken userInfo,int pageNum , int pageSize)throws UserServiceException {
       /* AttendanceModel model=new AttendanceModel();*/

        List<AttendanceModel> alist=new ArrayList<AttendanceModel>();
        List<StudentModel> shidao=new ArrayList<StudentModel>();//实到人
        List<StudentModel> duty=new ArrayList<StudentModel>();//缺勤人
        Integer sum=0;//课堂总人数
        Integer dutyren=0;//缺勤人数
        List<StudentClassRelation> slistsum=new ArrayList<StudentClassRelation>();
        Set<String> s=new HashSet<String>();
        List<AttendanceInClass> list=attendanceRepository.findAll();
        if (list.size()<=0){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}

       List<ClassRelation> clist=techerClassRelationRepository.findByTecherId(userInfo.getUserId());//根据老师id查询班级
        List<CourseUserRelation> listt=courseUserRelationRepository.findAllByUid(userInfo.getUserId());//根据老师查询课堂
        if (listt.size()<=0){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        for (CourseUserRelation t:listt) {//循环课堂
            Date time=new Date();//考勤时间
            Course course=courseRepository.findFirstById(t.getCourseId());
            if (StringUtils.isEmpty(course)){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
            }
            List<AttendanceInClass> attendance=attendanceRepository.findByCourseId(t.getCourseId());//根据课堂查询学生考勤记录
            List<CourseClassRelation> cclist=classCourseRelationRepository.findByCourseId(t.getCourseId());//根据课堂查询班级
            if (attendance.size()<=0 ||cclist.size()<=0){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
            for (CourseClassRelation c:cclist) {
                List<StudentClassRelation> slist=studentClassRelationRepository.findAllByClassId(c.getClassId());//根据班级获取所有学生
                sum=sum+slist.size();//该堂课所以学生
                slistsum.addAll(slist);//所有学生
            }
            for (AttendanceInClass ac:attendance ) {
                s.add(ac.getStudentId());//获取考勤学生
                StudentInfo info=studentInfoRespository.findFirstByid(ac.getStudentId());
                if (info!=null){
                    StudentModel smodel=new StudentModel();
                    smodel.setId(ac.getStudentId());
                    smodel.setAccount(info.getAccount());
                    smodel.setName(info.getName());
                    shidao.add(smodel);//人数
                }
                time=ac.getAtime();
            }
            dutyren=sum-attendance.size();//缺勤人数
            if (slistsum.size()>0){
                for (StudentClassRelation sc:slistsum) {
                   if (s.contains(sc.getStudentId())){//在
                   }else {//不在
                       StudentInfo info=studentInfoRespository.findFirstByid(sc.getStudentId());
                       if (info!=null){
                           StudentModel smodel=new StudentModel();
                           smodel.setId(sc.getStudentId());
                           smodel.setAccount(info.getAccount());
                           smodel.setName(info.getName());
                           duty.add(smodel);//缺勤人
                       }
                   }
                }
            }
            AttendanceModel model=new AttendanceModel();
            model.setAtime(time);
            model.setId(t.getCourseId());
            model.setCourseName(course.getCourseTitle());
            model.setTrueto(sum);
            model.setDutystudent(dutyren);
            model.setSignin(shidao);
            model.setDuty(duty);
            alist.add(model);
        }

        PageInfo<AttendanceModel> pageInfo=new PageInfo<AttendanceModel>();
        pageInfo.setList(alist);
        pageInfo.setTotal(list.size());
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        return pageInfo;
    }
   /* public PageInfo<AttendanceModel> getAttendanceByName(UserInfoForToken userInfo,String studentName ,int pageNum ,int pageSize)throws UserServiceException{
        List<AttendanceInClass> list=attendanceRepository.findAllByStudentNameLike(studentName);
        PageInfo<AttendanceInClass> pageInfo=new PageInfo<AttendanceInClass>();
        pageInfo.setList(list);
        pageInfo.setTotal(list.size());
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        return pageInfo;
    }*/


}
