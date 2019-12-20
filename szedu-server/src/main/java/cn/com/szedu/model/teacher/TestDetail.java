package cn.com.szedu.model.teacher;

import java.util.Date;
import java.util.List;

public class TestDetail {
    private String testId;
    private String testName;
    private String subjectId;
    private String subjectName;
    private String testPaperType;
    private Date startTime;
    private String authorId;
    private String authorName;
    private String classId;
    private String className;
    private Integer shouldNumber;//应到
    private Integer factNumber;//实到
    private Integer absenceNumber;//缺席
    private List<String> absenceName;//缺席人员

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

    public String getTestPaperType() {
        return testPaperType;
    }

    public void setTestPaperType(String testPaperType) {
        this.testPaperType = testPaperType;
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
}
