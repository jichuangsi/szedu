package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picImg;

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
}
