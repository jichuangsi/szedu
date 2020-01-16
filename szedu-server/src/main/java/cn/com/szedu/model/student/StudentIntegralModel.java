package cn.com.szedu.model.student;

public class StudentIntegralModel {
    private String pic;
    private String studentId;
    private String acount;
    private String studentName;
    private Integer integral;
    private Integer rank;
    private Integer myrank;//我的排行

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
    }

    public Integer getMyrank() {
        return myrank;
    }

    public void setMyrank(Integer myrank) {
        this.myrank = myrank;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public StudentIntegralModel(String pic, String studentId, String acount, String studentName, Integer integral, Integer rank, Integer myrank) {
        this.pic = pic;
        this.studentId = studentId;
        this.acount = acount;
        this.studentName = studentName;
        this.integral = integral;
        this.rank = rank;
        this.myrank = myrank;
    }

    public StudentIntegralModel() {
    }
}
