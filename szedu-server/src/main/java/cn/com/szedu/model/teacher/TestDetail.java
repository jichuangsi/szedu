package cn.com.szedu.model.teacher;

import java.util.Date;
import java.util.List;

public class TestDetail {
    private String testId;
    private String testName;
    private String term;//学期
    private String subjectId;
    private String subjectName;
    private String testType;//考试类型
    private Date startTime;
    private String authorId;
    private String authorName;
    private Integer shouldNumber;//应到
    private Integer factNumber;//实到
    private Integer absenceNumber;//缺席
    private List<String> absenceName;//缺席人员
    private List<ClassModel> classModes;
    private String testPaperId;
    private String testConten;//简介

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public Integer getShouldNumber() {
        return shouldNumber;
    }

    public void setShouldNumber(Integer shouldNumber) {
        this.shouldNumber = shouldNumber;
    }

    public Integer getFactNumber() {
        return factNumber;
    }

    public void setFactNumber(Integer factNumber) {
        this.factNumber = factNumber;
    }

    public Integer getAbsenceNumber() {
        return absenceNumber;
    }

    public void setAbsenceNumber(Integer absenceNumber) {
        this.absenceNumber = absenceNumber;
    }

    public List<String> getAbsenceName() {
        return absenceName;
    }

    public void setAbsenceName(List<String> absenceName) {
        this.absenceName = absenceName;
    }

    public List<ClassModel> getClassModes() {
        return classModes;
    }

    public void setClassModes(List<ClassModel> classModes) {
        this.classModes = classModes;
    }

    public String getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(String testPaperId) {
        this.testPaperId = testPaperId;
    }

    public String getTestConten() {
        return testConten;
    }

    public void setTestConten(String testConten) {
        this.testConten = testConten;
    }
}
