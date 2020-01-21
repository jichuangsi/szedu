package cn.com.szedu.model.teacher;

public class ExamClassResultModel {

    private String examId;
    private String studentID;//学号
    private String stdentName;
    private String className;//班级
    //private Double result;//分数
    private Integer result;//分数
    private String rank;//等级
    private Double accuracyRate;//正确率
    private Integer ranking;//排名
    private String remarks;//备注


    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /*public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }*/

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(Double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
