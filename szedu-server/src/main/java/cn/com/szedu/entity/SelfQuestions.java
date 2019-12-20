package cn.com.szedu.entity;

import javax.persistence.*;

@Entity
@Table(name = "selfQuestions")
public class SelfQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;//题干1
    /*private String Aoption;
    private String Boption;
    private String Coption;
    private String Doption;
    private String Eoption;
    private String Foption;*/
    private String answer;//答案
    private String answerDetail;//解析
    private String typeid;//题型id
    private String type;//题型
    private String subjectId;//标签科目
    private String subject;//标签科目
    private Integer integral;//积分
    /*private String knowledges;*/
    private String teacherId;//老师id1
    private String teacherName;//1
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] questionPic;//图片数据0;
    private long createTime;//0
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

    /*public String getAoption() {
        return Aoption;
    }

    public void setAoption(String aoption) {
        Aoption = aoption;
    }

    public String getBoption() {
        return Boption;
    }

    public void setBoption(String boption) {
        Boption = boption;
    }

    public String getCoption() {
        return Coption;
    }

    public void setCoption(String coption) {
        Coption = coption;
    }

    public String getDoption() {
        return Doption;
    }

    public void setDoption(String doption) {
        Doption = doption;
    }

    public String getEoption() {
        return Eoption;
    }

    public void setEoption(String eoption) {
        Eoption = eoption;
    }

    public String getFoption() {
        return Foption;
    }

    public void setFoption(String foption) {
        Foption = foption;
    }*/

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public byte[] getQuestionPic() {
        return questionPic;
    }

    public void setQuestionPic(byte[] questionPic) {
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

   /* public String getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(String knowledges) {
        this.knowledges = knowledges;
    }*/

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
