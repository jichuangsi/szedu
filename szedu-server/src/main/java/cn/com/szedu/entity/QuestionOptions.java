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
    private String E;
    private String EoptionPic;
    private String F;
    private String FoptionPic;
    private String tureOption;
    private String tureOptionPic;
    private String falseOption;
    private String falseOptionPic;

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

    public String getE() {
        return E;
    }

    public void setE(String e) {
        E = e;
    }

    public String getF() {
        return F;
    }

    public void setF(String f) {
        F = f;
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

    public String getEoptionPic() {
        return EoptionPic;
    }

    public void setEoptionPic(String eoptionPic) {
        EoptionPic = eoptionPic;
    }

    public String getFoptionPic() {
        return FoptionPic;
    }

    public void setFoptionPic(String foptionPic) {
        FoptionPic = foptionPic;
    }

    public String getTureOption() {
        return tureOption;
    }

    public void setTureOption(String tureOption) {
        this.tureOption = tureOption;
    }

    public String getTureOptionPic() {
        return tureOptionPic;
    }

    public void setTureOptionPic(String tureOptionPic) {
        this.tureOptionPic = tureOptionPic;
    }

    public String getFalseOption() {
        return falseOption;
    }

    public void setFalseOption(String falseOption) {
        this.falseOption = falseOption;
    }

    public String getFalseOptionPic() {
        return falseOptionPic;
    }

    public void setFalseOptionPic(String falseOptionPic) {
        this.falseOptionPic = falseOptionPic;
    }
}
