package cn.com.szedu.model.teacher;

import java.util.List;

public class ClassExamModel {
    private String examId;
    private String examName;//试卷名字
    private String subjectId;
    private String subjiectName;//科目
    private String examType;//试卷类型
    //private String startTime;//开考时间
    private long startTime;//开考时间
    private List<String> examClass;//考试班级
    private String authorName;//出题人
    private Integer sum;//应考人数
    private Integer actual;//实考人数
    private Integer miss;//缺考人数
    private List<String> aName;//实考人

    private Integer passingNumber;//及格人数
    private Double passingRate;//及格率
    private Double excellentRate;//优秀率
    private Double avg;//平均分
    private Double max;//最高分
    private Double min;//最低分
    //private ClassGradeDistributionModel model;
    private List<ClassGradeDistributionModel> model;

    public List<ClassGradeDistributionModel> getModel() {
        return model;
    }

    public void setModel(List<ClassGradeDistributionModel> model) {
        this.model = model;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
/* public ClassGradeDistributionModel getModel() {
        return model;
    }

    public void setModel(ClassGradeDistributionModel model) {
        this.model = model;
    }*/

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

    public String getSubjiectName() {
        return subjiectName;
    }

    public void setSubjiectName(String subjiectName) {
        this.subjiectName = subjiectName;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    /*public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }*/

    public List<String> getExamClass() {
        return examClass;
    }

    public void setExamClass(List<String> examClass) {
        this.examClass = examClass;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        this.actual = actual;
    }

    public Integer getMiss() {
        return miss;
    }

    public void setMiss(Integer miss) {
        this.miss = miss;
    }

    public List<String> getaName() {
        return aName;
    }

    public void setaName(List<String> aName) {
        this.aName = aName;
    }


    public Integer getPassingNumber() {
        return passingNumber;
    }

    public void setPassingNumber(Integer passingNumber) {
        this.passingNumber = passingNumber;
    }

    public Double getPassingRate() {
        return passingRate;
    }

    public void setPassingRate(Double passingRate) {
        this.passingRate = passingRate;
    }

    public Double getExcellentRate() {
        return excellentRate;
    }

    public void setExcellentRate(Double excellentRate) {
        this.excellentRate = excellentRate;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }
}
