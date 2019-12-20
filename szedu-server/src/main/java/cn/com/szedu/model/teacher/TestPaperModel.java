package cn.com.szedu.model.teacher;

import java.util.Date;
import java.util.List;

public class TestPaperModel {//试卷
    private String id;
    private String name;
    private String testType;//考试类型
    private String testPaperType;//试卷类型
    private Date startTime;//试卷开始时间
    private Date endTime;//试卷结束时间
    private String duration;//时长
    private String classId;
    private String className;
    //提前进入考试
    private String strettiTime;//提前进入考试
    private String testCover;//封面
    private List<SelfQuestionsModel> questionsModels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getStrettiTime() {
        return strettiTime;
    }

    public void setStrettiTime(String strettiTime) {
        this.strettiTime = strettiTime;
    }

    public String getTestCover() {
        return testCover;
    }

    public void setTestCover(String testCover) {
        this.testCover = testCover;
    }

    public List<SelfQuestionsModel> getQuestionsModels() {
        return questionsModels;
    }

    public void setQuestionsModels(List<SelfQuestionsModel> questionsModels) {
        this.questionsModels = questionsModels;
    }
}
