package cn.com.szedu.entity;

import cn.com.szedu.entity.IntermediateTable.ClassRelation;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "classInfo")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class ClassInfo {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String className;
    private String schoolId;//所属学校
    private String schoolName;
    private String speciality;//专业
    private String ruTime;//入学时间
    private String openTime;//开班时间
    private String content;//班级详情
    private String status;//班级状态A使用中B禁用
    private String founder;//创建人
    private String founderId;//创建人Id
    private String educationalSystem;//学制
    private String createTime;//创建时间
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picImg;

   /* @ManyToMany(fetch=FetchType.LAZY, targetEntity=ClassRelation.class)
    @JoinColumn(name = "id")
    private ClassRelation classRelation;

    public ClassRelation getClassRelation() {
        return classRelation;
    }

    public void setClassRelation(ClassRelation classRelation) {
        this.classRelation = classRelation;
    }*/

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRuTime() {
        return ruTime;
    }

    public void setRuTime(String ruTime) {
        this.ruTime = ruTime;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getPicImg() {
        return picImg;
    }

    public void setPicImg(byte[] picImg) {
        this.picImg = picImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String getFounderId() {
        return founderId;
    }

    public void setFounderId(String founderId) {
        this.founderId = founderId;
    }

    public String getEducationalSystem() {
        return educationalSystem;
    }

    public void setEducationalSystem(String educationalSystem) {
        this.educationalSystem = educationalSystem;
    }
}
