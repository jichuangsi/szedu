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
    private String enrollTime;//入学时间
    private String startTime;//开班时间
    private String content;//班级详情
    private String classCover;//班级封面
    private List<StudentModel> studentModels;
    private String status;//状态
    private String founder;//创建人
    private String founderId;//创建人
    private String educationalSystem;//学制
    private Integer student;//班级人数

    private String createTime;//创建时间


    public Integer getStudent() {
        return student;
    }

    public void setStudent(Integer student) {
        this.student = student;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(String enrollTime) {
        this.enrollTime = enrollTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String getFounderId() {
        return founderId;
    }

    public void setFounderId(String founderId) {
        this.founderId = founderId;
    }

    public String getEducationalSystem() {
        return educationalSystem;
    }

    public void setEducationalSystem(String educationalSystem) {
        this.educationalSystem = educationalSystem;
    }
}
