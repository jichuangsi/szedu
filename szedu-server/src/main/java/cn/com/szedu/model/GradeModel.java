package cn.com.szedu.model;

import java.util.ArrayList;
import java.util.List;

public class GradeModel {

    private String gradeId;
    private String gradeName;
    private List<String> classIds = new ArrayList<String>();
    private String schoolId;
    private String phraseId;
    private Long createdTime;
    private Long updateTime;

    public GradeModel(){}

    public GradeModel(String gradeId,String gradeName){
        this.gradeId = gradeId;
        this.gradeName = gradeName;
    }

    public String getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(String phraseId) {
        this.phraseId = phraseId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public List<String> getClassIds() {
        return classIds;
    }

    public void setClassIds(List<String> classIds) {
        this.classIds = classIds;
    }
}
