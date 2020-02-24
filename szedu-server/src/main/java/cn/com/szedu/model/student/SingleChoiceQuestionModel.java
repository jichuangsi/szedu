package cn.com.szedu.model.student;

public class SingleChoiceQuestionModel {

    private String examId;
    private String examName;
    private String type;
    private String subjectId;//标签科目
    private String subject;//标签科目
    private String chapter;//章节
    private Integer turepeople;//答对人数
    private Integer falsepeople;//答错人数
    private Double accuracyRate;//正确率

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public Integer getTurepeople() {
        return turepeople;
    }

    public void setTurepeople(Integer turepeople) {
        this.turepeople = turepeople;
    }

    public Integer getFalsepeople() {
        return falsepeople;
    }

    public void setFalsepeople(Integer falsepeople) {
        this.falsepeople = falsepeople;
    }

    public Double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(Double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }
}
