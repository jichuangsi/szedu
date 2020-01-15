package cn.com.szedu.model.teacher;

import cn.com.szedu.entity.SelfQuestions;

import java.util.Date;
import java.util.List;

public class TestPaperModel {//试卷
    private Integer id;
    private String testPaperName;
    private String grade;//年级
    private String type;//考试类型
    private String remark;//备注
    private String teacherId;
    private String teacherName;
    private String testTimeLength;//考试时长
    private long createTime=new Date().getTime();
    private long updateTime;
    private List<SelfQuestions> questionsModels;//题目

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTestPaperName() {
        return testPaperName;
    }

    public void setTestPaperName(String testPaperName) {
        this.testPaperName = testPaperName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getTestTimeLength() {
        return testTimeLength;
    }

    public void setTestTimeLength(String testTimeLength) {
        this.testTimeLength = testTimeLength;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public List<SelfQuestions> getQuestionsModels() {
        return questionsModels;
    }

    public void setQuestionsModels(List<SelfQuestions> questionsModels) {
        this.questionsModels = questionsModels;
    }
}
