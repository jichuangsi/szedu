package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "course")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class Course {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String courseTitle;//课堂名称
    private String subject;//科目
    private String chapter;//章节
    private String teacherId;
    private String teacherName;
    private String courseTimeLength;
    private Date startTime;
    private String content;//简介
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] coursePic;//图片数据0;
    private String teachAddress;//教学地址
    private String lessonTypeId;//课堂类型
    private String lessonTypeName;//课堂类型

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
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

    public String getCourseTimeLength() {
        return courseTimeLength;
    }

    public void setCourseTimeLength(String courseTimeLength) {
        this.courseTimeLength = courseTimeLength;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getCoursePic() {
        return coursePic;
    }

    public void setCoursePic(byte[] coursePic) {
        this.coursePic = coursePic;
    }

    public String getTeachAddress() {
        return teachAddress;
    }

    public void setTeachAddress(String teachAddress) {
        this.teachAddress = teachAddress;
    }

    public String getLessonTypeId() {
        return lessonTypeId;
    }

    public void setLessonTypeId(String lessonTypeId) {
        this.lessonTypeId = lessonTypeId;
    }

    public String getLessonTypeName() {
        return lessonTypeName;
    }

    public void setLessonTypeName(String lessonTypeName) {
        this.lessonTypeName = lessonTypeName;
    }
}
