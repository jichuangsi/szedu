package cn.com.szedu.model.teacher;

import cn.com.szedu.entity.CourseWare;

import java.util.List;

public class TeacherCourseModel {
    private String courseId;//课程id
    private String courseName;//课程名称
    private String countClassHours;//课时总数
    private String countResource;//资源总数
    private String content;//简介
    private List<CourseWare> courseWares;//资源列表

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

    public String getCountClassHours() {
        return countClassHours;
    }

    public void setCountClassHours(String countClassHours) {
        this.countClassHours = countClassHours;
    }

    public String getCountResource() {
        return countResource;
    }

    public void setCountResource(String countResource) {
        this.countResource = countResource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CourseWare> getCourseWares() {
        return courseWares;
    }

    public void setCourseWares(List<CourseWare> courseWares) {
        this.courseWares = courseWares;
    }
}
