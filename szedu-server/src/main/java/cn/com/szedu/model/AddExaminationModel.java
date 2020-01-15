package cn.com.szedu.model;

import java.util.List;

public class AddExaminationModel {
    private Integer id;
    private String testPaperName;
    private String grade;//年级
    private String type;//考试类型
    private String remark;//备注
    private List<String> subList;//科目
    private List<String> chapter;//章节
    private String way;//组卷方式
    private String teacherId;
    private String teacherName;

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

    public List<String> getSubList() {
        return subList;
    }

    public void setSubList(List<String> subList) {
        this.subList = subList;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
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

    public List<String> getChapter() {
        return chapter;
    }

    public void setChapter(List<String> chapter) {
        this.chapter = chapter;
    }
}
