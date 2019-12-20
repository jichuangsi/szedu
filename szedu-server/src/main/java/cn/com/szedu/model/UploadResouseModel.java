package cn.com.szedu.model;

public class UploadResouseModel {
    private String teacherId;
    private String userName;
    private String subject;
    private String school;
    private String integral;
    private Integer countUpload;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public Integer getCountUpload() {
        return countUpload;
    }

    public void setCountUpload(Integer countUpload) {
        this.countUpload = countUpload;
    }
}
