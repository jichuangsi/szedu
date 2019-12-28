package cn.com.szedu.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "topQuestions")
public class TopQuestions {
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
    private String answer;//答案
    private String answerDetail;//解析
    private String typeid;//题型id
    private String type;//题型
    private String subjectId;//标签科目
    private String subject;//标签科目
    private String chapter;//章节
    private Integer integral;//积分
    private String createrId;//创建者id1
    private String createrName;//1
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

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
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
