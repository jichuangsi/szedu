package cn.com.szedu.model.teacher;

import cn.com.szedu.entity.CourseWare;

import java.util.Date;
import java.util.List;

public class TeacherLessonModel {
    private String lessonId;
    private String lessonName;
    private String lessonContent;
    private Date teachTime;
    private String teachTimeLength;
    private String subjectId;//科目
    private String subjectName;
    private String chapter;//章节
    private String classRoom;//班级
    private String teacherId;
    private String teacherName;
    private String lessonTypeId;//课堂类型
    private String lessonTypeName;//课堂类型
    private String teachAddress;//上课地点
    private String lessonCover;//课堂封面
    private List<CourseWare> courseWares;
    private List<ClassModel> classModelList;//班级

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonContent() {
        return lessonContent;
    }

    public void setLessonContent(String lessonContent) {
        this.lessonContent = lessonContent;
    }

    public Date getTeachTime() {
        return teachTime;
    }

    public void setTeachTime(Date teachTime) {
        this.teachTime = teachTime;
    }

    public String getTeachTimeLength() {
        return teachTimeLength;
    }

    public void setTeachTimeLength(String teachTimeLength) {
        this.teachTimeLength = teachTimeLength;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
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

    public String getTeachAddress() {
        return teachAddress;
    }

    public void setTeachAddress(String teachAddress) {
        this.teachAddress = teachAddress;
    }

    public String getLessonCover() {
        return lessonCover;
    }

    public void setLessonCover(String lessonCover) {
        this.lessonCover = lessonCover;
    }

    public List<CourseWare> getCourseWares() {
        return courseWares;
    }

    public void setCourseWares(List<CourseWare> courseWares) {
        this.courseWares = courseWares;
    }

    public List<ClassModel> getClassModelList() {
        return classModelList;
    }

    public void setClassModelList(List<ClassModel> classModelList) {
        this.classModelList = classModelList;
    }
}
