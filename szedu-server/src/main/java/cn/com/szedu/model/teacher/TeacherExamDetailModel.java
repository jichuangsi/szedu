package cn.com.szedu.model.teacher;

import java.util.Date;
import java.util.List;

public class TeacherExamDetailModel {
    private String examId;
    private String examName;
    private String term;//学期
    private String examType;
    private String subjectId;
    private String subjectName;
    private String authorId;//出题人
    private String authorName;
    private String checkWay;//考核方式
    private String classId;//班级
    private String content;//考试简介
    private List<TestPaperModel> testPaperModels;//试卷
    private String className;
    private Date time;//考试时间
    private Date fabutime;//发布时间

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCheckWay() {
        return checkWay;
    }

    public void setCheckWay(String checkWay) {
        this.checkWay = checkWay;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TestPaperModel> getTestPaperModels() {
        return testPaperModels;
    }

    public void setTestPaperModels(List<TestPaperModel> testPaperModels) {
        this.testPaperModels = testPaperModels;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getFabutime() {
        return fabutime;
    }

    public void setFabutime(Date fabutime) {
        this.fabutime = fabutime;
    }
}
