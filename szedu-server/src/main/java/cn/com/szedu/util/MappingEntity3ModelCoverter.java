package cn.com.szedu.util;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.CourseStatus;
import cn.com.szedu.constant.Status;
import cn.com.szedu.entity.*;
import cn.com.szedu.model.SandMessageModel;
import cn.com.szedu.model.student.StudentClassInfoModel;
import cn.com.szedu.model.student.StudentInfoModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.*;
import org.springframework.util.StringUtils;

import java.rmi.MarshalledObject;
import java.util.Date;
import java.util.UUID;

public class MappingEntity3ModelCoverter {
    /**
     * 班级
     * @param userInfo
     * @param info
     * @return
     */
    public final static ClassModel CONVERTERFROMCLASSINFO(UserInfoForToken userInfo,ClassInfo info){
        ClassModel classModel=new ClassModel();
        classModel.setClassId(info.getId());
        classModel.setClassName(info.getClassName());
        classModel.setContent(info.getContent());
        classModel.setSchoolId(info.getSchoolId());
        classModel.setSchoolName(info.getSchoolName());
        classModel.setSpecialty(info.getSpeciality());
        classModel.setStartTime(info.getOpenTime());
        classModel.setStatus(info.getStatus());
        classModel.setFounder(info.getFounder());
        classModel.setFounderId(info.getFounderId());
        classModel.setCreateTime(info.getCreateTime());
        classModel.setEnrollTime(info.getRuTime());
        classModel.setEducationalSystem(info.getEducationalSystem());
        return classModel;
    }

    public final static ClassInfo CONVERTERFROMCLASSMODEL(UserInfoForToken userInfo,ClassModel info){
        ClassInfo classModel=new ClassInfo();
        classModel.setId(StringUtils.isEmpty(info.getClassId())?UUID.randomUUID().toString().replaceAll("-", ""):info.getClassId());
        classModel.setClassName(info.getClassName());
        classModel.setContent(info.getContent());
        classModel.setSchoolId(info.getSchoolId());
        classModel.setSchoolName(info.getSchoolName());
        classModel.setSpeciality(info.getSpecialty());
        classModel.setOpenTime(info.getStartTime());
        classModel.setStatus("A");
        classModel.setFounder(userInfo.getUserName());
        classModel.setFounderId(userInfo.getUserId());
        classModel.setEducationalSystem(info.getEducationalSystem());
        classModel.setRuTime(info.getEnrollTime());
        classModel.setCreateTime(info.getCreateTime());
        return classModel;
    }

    /**
     * 学生
     * @param model
     * @return
     */
    public final static StudentInfo CONVERTERFROMSTUDENTINFO(StudentModel model){
        StudentInfo info1=new StudentInfo();
        info1.setId(StringUtils.isEmpty(model.getId())?UUID.randomUUID().toString().replaceAll("-", ""):model.getId());
        info1.setAccount(model.getAccount());
        info1.setName(model.getName());
        if (!StringUtils.isEmpty(model.getPwd())) {
            info1.setPwd(Md5Util.encodeByMd5(model.getPwd()));
        }
        info1.setPhone(model.getPhone());
        info1.setStatus(Status.ACTIVATE.getName());
        info1.setSex(model.getSex());
        info1.setSchoolName(model.getSchoolName());
        info1.setSchoolId(model.getSchoolId());
       /* info1.setClassId(model.getClassId());*/
        info1.setStudentId(model.getStudentId());//学号
        info1.setRole("学生");
        info1.setCreateTime(new Date().getTime());
        info1.setUpdateTime(new Date().getTime());
        info1.setIntegral(0);
        return info1;
    }

    public final static StudentModel CONVERTERFROMSTUDENTMODEL(StudentInfo model){
        StudentModel info1=new StudentModel();
        info1.setId(model.getId());
        info1.setAccount(model.getAccount());
        info1.setName(model.getName());
        info1.setPwd(model.getPwd());
        info1.setPhone(model.getPhone());
        info1.setStatus(model.getStatus());
        info1.setSex(model.getSex());
        info1.setSchoolName(model.getSchoolName());
        info1.setClassId(model.getClassId());
        info1.setStudentId(model.getStudentId());//学号
        return info1;
    }

    /**
     * 老师
     * @param userInfo
     * @return
     */
    public final static UserInfoForToken CONVERTERFROMBACKUSERUSER(TeacherInfo userInfo) {
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getName());
        userInfoForToken.setRoleId(userInfo.getRole());
        return userInfoForToken;
    }

    /**
     * 老师
     * @param userInfo
     * @return
     */
    public final static TeacherLoginModel CONVERTERFROMBACKTEACHERINFO(TeacherInfo userInfo) {
        TeacherLoginModel model1 = new TeacherLoginModel();
        model1.setAcount(userInfo.getAccount());
        model1.setTeacherName(userInfo.getName());
        model1.setTeacherId(userInfo.getId());
        if (userInfo.getIntegral()==null){
            model1.setIntegral(0);
        }else {
            model1.setIntegral(userInfo.getIntegral());
        }

        model1.setSchoolId(userInfo.getSchoolId());
        model1.setSchoolName(userInfo.getSchoolName());
        return model1;
    }

    public final static AttendanceInClass CONVERTERFROMBACKATTENDANCEMODEL(UserInfoForToken userInfo,AttendanceModel model) {
        AttendanceInClass att = new AttendanceInClass();
        att.setAid(StringUtils.isEmpty(model.getId())?UUID.randomUUID().toString().replaceAll("-", ""):model.getId());
        att.setAtime(model.getAtime());
        att.setClassId(model.getClassId());
        att.setClassName(model.getClassName());
        att.setCourseId(model.getCourseId());
        att.setCourseName(model.getCourseName());
        att.setStudentId(model.getStudentId());
        att.setStudentName(userInfo.getUserName());
        att.setSid(userInfo.getUserId());
        return att;
    }

    /**
     * 考试
     * @param model
     * @return
     */
    public final static TeacherExamDetailModel CONVERTERFROMBACKEXAMDETAIL(Exam model) {
        TeacherExamDetailModel att = new TeacherExamDetailModel();
       att.setExamId(model.getId());
       att.setExamName(model.getExamName());
     /*  att.setClassId(model.getClassId());*/
       att.setExamType(model.getExamType());
        att.setSubjectId(model.getSubjectId());
        att.setSubjectName(model.getSubjectName());
       att.setStartTime(model.getStartTime());
       att.setEndTime(model.getEndTime());
       /*att.setCheckWay(model.getCheckWay());
       att.setFabutime(model.getReleaseTime());*/
       att.setStatus(model.getStatus());
      att.setContent(model.getContent());
      att.setAuthorId(model.getCreatorId());
      att.setAuthorName(model.getCreatorName());
      att.setTiqian(model.getTiqian());
        return att;
    }

    /**
     * 课堂
     * @param info
     * @return
     */
    public final static TeacherLessonModel CONVERTERFROMBACKLESSONMODEL(Course info) {
        TeacherLessonModel model = new TeacherLessonModel();
      model.setLessonName(info.getCourseTitle());
      model.setLessonTypeName(info.getLessonTypeName());
      model.setLessonTypeId(info.getLessonTypeId());
      model.setSubjectId(info.getSubjectId());
      model.setSubjectName(info.getSubject());
      model.setTeachTime(info.getStartTime());
      model.setTeacherId(info.getTeacherId());
      model.setTeacherName(info.getTeacherName());
      model.setFounderId(info.getFounderId());
      model.setFounderName(info.getFounderName());
      model.setTeachTimeLength(info.getCourseTimeLength());
      model.setLessonContent(info.getContent());
      model.setEndTime(info.getEndTime());
        return model;
    }
    public final static Course CONVERTERFROMBACKLESSON(UserInfoForToken userInfo,TeacherLessonModel model) {
        Course course = new Course();
        course.setId(StringUtils.isEmpty(model.getLessonId()) ? UUID.randomUUID().toString().replaceAll("-", "") : model.getLessonId());
        course.setCourseTitle(model.getLessonName());
        course.setTeachAddress(model.getTeachAddress());
        course.setLessonTypeId(model.getLessonTypeId());
        course.setLessonTypeName(model.getLessonTypeName());
        course.setTeacherId(model.getTeacherId());
        course.setTeacherName(model.getTeacherName());
        course.setSubjectId(model.getSubjectId());
        course.setSubject(model.getSubjectName());
        course.setChapterId(model.getChapterId());
        course.setChapter(model.getChapter());
        course.setStartTime(model.getTeachTime());
        course.setCourseTimeLength(model.getTeachTimeLength());
        course.setContent(model.getLessonContent());
        course.setStatus(CourseStatus.UNPUBLISH.getName());
        course.setFounderId(userInfo.getUserId());
        course.setFounderName(userInfo.getUserName());
        return course;
    }
    /**
     * 学生
     * @param userInfo
     * @return
     */
    public final static UserInfoForToken CONVERTERFROMBACKSTUDENTINFO(StudentInfo userInfo) {
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getName());
        userInfoForToken.setRoleId(userInfo.getRole());
        return userInfoForToken;
    }

    /**
     * 学生
     * @param userInfo
     * @return
     */
    public final static StudentInfoModel CONVERTERFROMBACKSTUDENTMODEL(StudentInfo userInfo) {
        StudentInfoModel model1 = new StudentInfoModel();
       model1.setAccount(userInfo.getAccount());
       model1.setName(userInfo.getName());
        if (userInfo.getIntegral()==null){
            model1.setIntegral(0);
        }else {
            model1.setIntegral(userInfo.getIntegral());
        }
        model1.setSchoolId(userInfo.getSchoolId());
        model1.setSchoolName(userInfo.getSchoolName());
        model1.setPhone(userInfo.getPhone());
        model1.setId(userInfo.getId());
        return model1;
    }


    /*public final static CourseWare CONVERTERFROMBACKCOURCEWARE(TeacherLessonModel model) {
        Course course = new Course();
        course.setId(StringUtils.isEmpty(model.getLessonId())?UUID.randomUUID().toString().replaceAll("-", ""):model.getLessonId());
        course.setCourseTitle(model.getLessonName());
        course.setTeachAddress(model.getTeachAddress());
        course.setLessonTypeId(model.getLessonTypeId());
        course.setLessonTypeName(model.getLessonTypeName());
        course.setTeacherId(model.getTeacherId());
        course.setTeacherName(model.getTeacherName());
        course.setSubjectId(model.getSubjectId());
        course.setSubject(model.getSubjectName());
        course.setChapter(model.getChapter());
        course.setStartTime(model.getTeachTime());
        course.setCourseTimeLength(model.getTeachTimeLength());
        course.setContent(model.getLessonContent());
        course.setStatus(CourseStatus.UNPUBLISH.getName());
        return course;
    }*/

    /**
     *互动消息
     * @param info
     * @param model
     * @return
     */
    public final static Message CONVERTERFROMBACKMESSAGEMODEL(UserInfoForToken info,SandMessageModel model) {
        Message message=new Message();
        message.setSenderid(info.getUserId());
        message.setSenderName(info.getUserName());
        message.setMessage(model.getMessage());
        message.setAlreadyRead("false");
        message.setReply(model.getReply());
        message.setSend(model.getSend());//已发送
        return message;
    }
    /**
     *
     * @param info
     * @param model
     * @return
     */
    public final static Message CONVERTERFROMBACKMESSAGE(UserInfoForToken info,MessageModel model) {
        Message message=new Message();
        message.setSenderid(info.getUserId());//发送
        message.setSenderName(info.getUserName());
        message.setMessage(model.getMessage());//消息
         message.setAlreadyRead("false");//已读
        message.setRecipientId(model.getRecipientId());//接收
        message.setRecipientName(model.getRecipientName());
        message.setSend(model.getSend());//已发送
        return message;
    }

    /**
     *建议、留言、管理员可见
     * @param info
     * @param model
     * @return
     */
    public final static Message CONVERTERFROMBACKMESSAGEMODELLEAVE(UserInfoForToken info,MessageModel model) {
        Message message=new Message();
        message.setSenderid(info.getUserId());//发送
        message.setSenderName(info.getUserName());
        message.setMessage(model.getMessage());//消息
        message.setAlreadyRead("false");//已读
        message.setRecipientId("1");//接收
        message.setSend("Y");//已发送
        message.setTime(new Date().getTime());
        return message;
    }


    /**
     * 学生信息
     * @param userInfo
     * @return
     */
    public final static StudentClassInfoModel CONVERTERFROMBACKSTUDENT(StudentInfo userInfo) {
        StudentClassInfoModel model1 = new StudentClassInfoModel();
        model1.setId(userInfo.getId());
        model1.setAccount(userInfo.getAccount());
        model1.setName(userInfo.getName());
        model1.setPortrait(userInfo.getPortrait());
        model1.setSex(userInfo.getSex());
        model1.setBirthday(userInfo.getBirthday());
        model1.setAddress(userInfo.getAddress());
        model1.setSchoolId(userInfo.getSchoolId());
        model1.setSchoolName(userInfo.getSchoolName());
        model1.setPhone(userInfo.getPhone());
        return model1;
    }
}
