package cn.com.szedu.model.teacher;

public class TeacherModel {

    private String teacherId;
    private String acount;
    private String teacherName;
    private String password;
    private String schoolId;
    private String schoolName;
    private Integer countCourse;//课程总数
    private String countCollect;//收藏的试题和资源总数
    private Integer countResource;//资源总数
    private Integer integral;
    private String accessToken;
    private boolean signin;//签到

    public boolean isSignin() {
        return signin;
    }

    public void setSignin(boolean signin) {
        this.signin = signin;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getCountCourse() {
        return countCourse;
    }

    public void setCountCourse(Integer countCourse) {
        this.countCourse = countCourse;
    }

    public String getCountCollect() {
        return countCollect;
    }

    public void setCountCollect(String countCollect) {
        this.countCollect = countCollect;
    }

    public Integer getCountResource() {
        return countResource;
    }

    public void setCountResource(Integer countResource) {
        this.countResource = countResource;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
