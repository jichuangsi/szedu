package cn.com.szedu.model.teacher;

import cn.com.szedu.entity.ClassInfo;

import java.util.List;

public class ExamModel {
    private String examId;
    private String examName;
    private String term;//学期
    private String examType;
    private String subjectId;
    private String subjectName;
    private String creatorId;//创建者、出题人
    private String creatorName;
    private String status;//状态（发布/未发布）
    private String statusName;
    private String content;//考试简介
    private String testPaperId;;//试卷
    private String isOpenAnswer;//是否公布答案
    private List<String> classId;
    private List<ClassInfo> classInfos;
    private List<TestTimeModel> models;//时间

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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(String testPaperId) {
        this.testPaperId = testPaperId;
    }

    public List<String> getClassId() {
        return classId;
    }

    public void setClassId(List<String> classId) {
        this.classId = classId;
    }

    public List<TestTimeModel> getModels() {
        return models;
    }

    public void setModels(List<TestTimeModel> models) {
        this.models = models;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getIsOpenAnswer() {
        return isOpenAnswer;
    }

    public void setIsOpenAnswer(String isOpenAnswer) {
        this.isOpenAnswer = isOpenAnswer;
    }

    public List<ClassInfo> getClassInfos() {
        return classInfos;
    }

    public void setClassInfos(List<ClassInfo> classInfos) {
        this.classInfos = classInfos;
    }
}
