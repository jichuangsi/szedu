package cn.com.szedu.model.teacher;

import cn.com.szedu.model.student.PerformanceAnalysisModel;

import java.util.ArrayList;
import java.util.List;

public class ScoreAnalysisModel {
    private String studentID;//学号
    private String stdentName;
    private Integer maxScore;//最高分
    private Integer minScore;//最低分
    List<PerformanceAnalysisModel> models=new ArrayList<PerformanceAnalysisModel>();

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStdentName() {
        return stdentName;
    }

    public void setStdentName(String stdentName) {
        this.stdentName = stdentName;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
    }

    public List<PerformanceAnalysisModel> getModels() {
        return models;
    }

    public void setModels(List<PerformanceAnalysisModel> models) {
        this.models = models;
    }
}
