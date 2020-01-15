package cn.com.szedu.entity;

import javax.persistence.*;

@Entity
@Table(name = "curriculum")
public class Curriculum {//课程
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String curriculumName;//课程名称
    private String content;
    private String subjectId;//标签科目
    private String subject;//标签科目
    private Integer integral;//积分
    private String teacherid;//老师id1
    private String teacherName;//1
    private String isCheck;//1
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] curriculumPic;//图片数据0;

    private String chaper;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public byte[] getCurriculumPic() {
        return curriculumPic;
    }

    public void setCurriculumPic(byte[] curriculumPic) {
        this.curriculumPic = curriculumPic;
    }

    public String getChaper() {
        return chaper;
    }

    public void setChaper(String chaper) {
        this.chaper = chaper;
    }
}
