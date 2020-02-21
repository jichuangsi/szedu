package cn.com.szedu.model.student;

import java.util.List;

public class StudentAnswerModel2 {
    private String examId;
    private String testPaperId;
    private List<StudentAnswerModel> single;//单选
    private List<StudentAnswerModel> multiple;//多选
    private List<StudentAnswerModel> judge;//判断

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(String testPaperId) {
        this.testPaperId = testPaperId;
    }

    public List<StudentAnswerModel> getSingle() {
        return single;
    }

    public void setSingle(List<StudentAnswerModel> single) {
        this.single = single;
    }

    public List<StudentAnswerModel> getMultiple() {
        return multiple;
    }

    public void setMultiple(List<StudentAnswerModel> multiple) {
        this.multiple = multiple;
    }

    public List<StudentAnswerModel> getJudge() {
        return judge;
    }

    public void setJudge(List<StudentAnswerModel> judge) {
        this.judge = judge;
    }
}
