package cn.com.szedu.entity;

import javax.persistence.*;

@Entity
@Table(name = "questionOptions")
public class QuestionOptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer questionId;
    private String A;
    private String AoptionPic;//图片
    private String B;
    private String BoptionPic;
    private String C;
    private String CoptionPic;
    private String D;
    private String DoptionPic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getAoptionPic() {
        return AoptionPic;
    }

    public void setAoptionPic(String aoptionPic) {
        AoptionPic = aoptionPic;
    }

    public String getBoptionPic() {
        return BoptionPic;
    }

    public void setBoptionPic(String boptionPic) {
        BoptionPic = boptionPic;
    }

    public String getCoptionPic() {
        return CoptionPic;
    }

    public void setCoptionPic(String coptionPic) {
        CoptionPic = coptionPic;
    }

    public String getDoptionPic() {
        return DoptionPic;
    }

    public void setDoptionPic(String doptionPic) {
        DoptionPic = doptionPic;
    }

}
