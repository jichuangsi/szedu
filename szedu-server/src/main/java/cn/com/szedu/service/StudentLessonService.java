package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.AttendanceInClass;
import cn.com.szedu.entity.ClassInfo;
import cn.com.szedu.entity.IntegralRecord;
import cn.com.szedu.entity.IntermediateTable.CourseClassRelation;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.entity.IntermediateTable.StudentCourseScore;
import cn.com.szedu.entity.StudentInfo;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.repository.IAttendanceRepository;
import cn.com.szedu.repository.IClassInfoRepository;
import cn.com.szedu.repository.IStudentInfoRespository;
import cn.com.szedu.repository.IntegralRecordRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IClassCourseRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentCourseScoreRepository;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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


        //上课签到5积分
        StudentInfo studentInfo=new StudentInfo();
        StudentInfo infos=studentInfoRespository.findFirstByid(info.getUserId());
        //studentInfo.setId(info.getUserId());
        infos.setIntegral(infos.getIntegral()+5);
        studentInfoRespository.save(infos);
       /* IntegralRecord integralRecord=new IntegralRecord();
        integralRecord.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        integralRecord.setFunction("签到");
        integralRecord.setFunctionName("上课签到");
        integralRecord.setIntegra(5);
        integralRecord.setOperatorId(studentInfo.getId());
        integralRecord.setFunctionName(infos.getName());
        integralRecord.setMessage("你今日课堂已签到，获得 5 积分");
        integralRecord.setCreateTime(new Date().getTime());*/
        IntegralRecord integralRecord=new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                "签到","上课签到",studentInfo.getId(),infos.getName(),
                5,"你今日课堂已签到，获得 5 积分",new Date().getTime());
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
        StudentCourseScore studentCourseScore=new StudentCourseScore();
        studentCourseScore.setCourseId(courseId);
        studentCourseScore.setStudentId(info.getUserId());
        studentCourseScore.setScore(score);
        studentCourseScoreRepository.save(studentCourseScore);
    }
}
