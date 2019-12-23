package cn.com.szedu.model.teacher;

import cn.com.szedu.model.StudentModel;

import java.util.Date;
import java.util.List;

public class AttendanceModel {

    private String id;
    private String courseId;//  课堂id
    private String courseName;//课堂名称
    private Date atime;//考勤时间(日)
    private List<StudentModel> signin;//签到学生
    private List<StudentModel> duty;//缺勤人
    private Integer trueto;//实到人数
    private Integer dutystudent;//缺勤人数
    private String classId;//  班级id
    private String className;//班级名称
    private String studentId;//学生号
    private String studentName;//学生名字
    private String sid;//学生id


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getAtime() {
        return atime;
    }

    public void setAtime(Date atime) {
        this.atime = atime;
    }

    public List<StudentModel> getSignin() {
        return signin;
    }

    public void setSignin(List<StudentModel> signin) {
        this.signin = signin;
    }

    public List<StudentModel> getDuty() {
        return duty;
    }

    public void setDuty(List<StudentModel> duty) {
        this.duty = duty;
    }

    public Integer getTrueto() {
        return trueto;
    }

    public void setTrueto(Integer trueto) {
        this.trueto = trueto;
    }

    public Integer getDutystudent() {
        return dutystudent;
    }

    public void setDutystudent(Integer dutystudent) {
        this.dutystudent = dutystudent;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
