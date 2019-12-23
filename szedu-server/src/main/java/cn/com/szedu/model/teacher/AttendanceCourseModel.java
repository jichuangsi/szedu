package cn.com.szedu.model.teacher;

import java.util.Date;

public class AttendanceCourseModel {
    private String courseId;//  课堂id
    private String courseName;//课堂名称
    private String studentName;//学生名字
    private String studentId;//学号
    private Date atime;//考勤时间(日)
    private Integer sum;//应到人数
    private Integer trueto;//实到人数
    private Integer dutystudent;//缺勤人数
   private String staus;//考勤状态
    private String className;//班级


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Date getAtime() {
        return atime;
    }

    public void setAtime(Date atime) {
        this.atime = atime;
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

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }
}
