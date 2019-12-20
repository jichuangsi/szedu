package cn.com.szedu.model.teacher;

public class ClassGradeDistributionModel {
    private String examId;
    private String fractionSegment;//分数段
    private String rank;//等级
    private Integer count;//人数
    private Double rate;//比例

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getFractionSegment() {
        return fractionSegment;
    }

    public void setFractionSegment(String fractionSegment) {
        this.fractionSegment = fractionSegment;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
