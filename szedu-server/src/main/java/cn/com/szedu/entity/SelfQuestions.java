package cn.com.szedu.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "selfQuestions")
public class SelfQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;//题干1
    private String contentPic;
    private String Aoption;
    private String AoptionPic;//图片
    private String Boption;
    private String BoptionPic;
    private String Coption;
    private String CoptionPic;
    private String Doption;
    private String DoptionPic;
    private String Eoption;
    private String EoptionPic;
    private String Foption;
    private String FoptionPic;
    private String tureOption;
    private String tureOptionPic;
    private String falseOption;
    private String falseOptionPic;
    private String answer;//答案
    private String answerDetail;//解析
    private String typeid;//题型id
    private String type;//题型
    private String subjectId;//标签科目
    private String subject;//标签科目
    private String chapter;//章节
    private Integer integral;//积分
    private String teacherId;//老师id1
    private String teacherName;//1
    /*@Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] questionPic;*///图片数据0;
    private String questionPic;
    private long createTime=new Date().getTime();//0
    private long updateTime;//0

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentPic() {
        return contentPic;
    }

    public void setContentPic(String contentPic) {
        this.contentPic = contentPic;
    }

    public String getAoption() {
        return Aoption;
    }

    public void setAoption(String aoption) {
        Aoption = aoption;
    }

    public String getAoptionPic() {
        return AoptionPic;
    }

    public void setAoptionPic(String aoptionPic) {
        AoptionPic = aoptionPic;
    }

    public String getBoption() {
        return Boption;
    }

    public void setBoption(String boption) {
        Boption = boption;
    }

    public String getBoptionPic() {
        return BoptionPic;
    }

    public void setBoptionPic(String boptionPic) {
        BoptionPic = boptionPic;
    }

    public String getCoption() {
        return Coption;
    }

    public void setCoption(String coption) {
        Coption = coption;
    }

    public String getCoptionPic() {
        return CoptionPic;
    }

    public void setCoptionPic(String coptionPic) {
        CoptionPic = coptionPic;
    }

    public String getDoption() {
        return Doption;
    }

    public void setDoption(String doption) {
        Doption = doption;
    }

    public String getDoptionPic() {
        return DoptionPic;
    }

    public void setDoptionPic(String doptionPic) {
        DoptionPic = doptionPic;
    }

    public String getEoption() {
        return Eoption;
    }

    public void setEoption(String eoption) {
        Eoption = eoption;
    }

    public String getEoptionPic() {
        return EoptionPic;
    }

    public void setEoptionPic(String eoptionPic) {
        EoptionPic = eoptionPic;
    }

    public String getFoption() {
        return Foption;
    }

    public void setFoption(String foption) {
        Foption = foption;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerDetail() {
        return answerDetail;
    }

    public void setAnswerDetail(String answerDetail) {
        this.answerDetail = answerDetail;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getQuestionPic() {
        return questionPic;
    }

    public void setQuestionPic(String questionPic) {
        this.questionPic = questionPic;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
