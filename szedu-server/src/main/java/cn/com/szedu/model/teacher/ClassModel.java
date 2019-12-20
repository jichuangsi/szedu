package cn.com.szedu.model.teacher;

import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.SubjectModel;

import java.util.Date;
import java.util.List;

public class ClassModel {
    private String classId;
    private String className;
    private String schoolId;
    private String schoolName;
    private String specialty;//专业
    private List<SubjectModel> subjectModels;//科目
    private Date enrollTime;//入学时间
    private Date startTime;//开班时间
    private String content;//班级详情
    private String classCover;//班级封面
    private List<StudentModel> studentModels;

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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public List<SubjectModel> getSubjectModels() {
        return subjectModels;
    }

    public void setSubjectModels(List<SubjectModel> subjectModels) {
        this.subjectModels = subjectModels;
    }

    public Date getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(Date enrollTime) {
        this.enrollTime = enrollTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClassCover() {
        return classCover;
    }

    public void setClassCover(String classCover) {
        this.classCover = classCover;
    }

    public List<StudentModel> getStudentModels() {
        return studentModels;
    }

    public void setStudentModels(List<StudentModel> studentModels) {
        this.studentModels = studentModels;
    }
}
