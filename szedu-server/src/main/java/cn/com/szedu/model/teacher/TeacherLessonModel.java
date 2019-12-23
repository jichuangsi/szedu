package cn.com.szedu.model.teacher;

import cn.com.szedu.entity.CourseWare;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TeacherLessonModel {
    private String lessonId;
    private String lessonName;
    private String lessonContent;
    private Date teachTime;//开课 时间
    private String teachTimeLength;
    private String subjectId;//科目
    private String subjectName;
    private String chapter;//章节
    private String classRoom;//班级
    private String teacherId;
    private String teacherName;//任课老师
    private String founderId;//
    private String founderName;//创建人
    private String lessonTypeId;//课堂类型
    private String lessonTypeName;//课堂类型
    private String teachAddress;//上课地点
    private String lessonCover;//课堂封面
    private List<CourseWare> courseWares;//上课资源
    private List<CourseWare> pushcourseWares;//推送资源资源
    private List<ClassModel> classModelList;//班级
    private Integer sum;
    private Timestamp endTime;//结束时间
    private List<StudentCourseScoreModel> score;//课堂评分


    public List<CourseWare> getPushcourseWares() {
        return pushcourseWares;
    }

    public void setPushcourseWares(List<CourseWare> pushcourseWares) {
        this.pushcourseWares = pushcourseWares;
    }

    public List<StudentCourseScoreModel> getScore() {
        return score;
    }

    public void setScore(List<StudentCourseScoreModel> score) {
        this.score = score;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getFounderId() {
        return founderId;
    }

    public void setFounderId(String founderId) {
        this.founderId = founderId;
    }

    public String getFounderName() {
        return founderName;
    }

    public void setFounderName(String founderName) {
        this.founderName = founderName;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

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
