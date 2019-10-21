package cn.com.szedu.model;

import cn.com.szedu.entity.CourseWare;

import java.util.List;

public class ShareElements {
    List<SubjectModel> subjects;//学科
    List<GradeModel> gradeModels;//年级
    List<CourseWare> courseWares;//课件列表

    public List<SubjectModel> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectModel> subjects) {
        this.subjects = subjects;
    }

    public List<GradeModel> getGradeModels() {
        return gradeModels;
    }

    public void setGradeModels(List<GradeModel> gradeModels) {
        this.gradeModels = gradeModels;
    }

    public List<CourseWare> getCourseWares() {
        return courseWares;
    }

    public void setCourseWares(List<CourseWare> courseWares) {
        this.courseWares = courseWares;
    }
}
