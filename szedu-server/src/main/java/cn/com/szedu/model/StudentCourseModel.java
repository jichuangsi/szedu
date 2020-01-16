package cn.com.szedu.model;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

public class StudentCourseModel {
    private String id;
    private String courseTitle;//课堂名称
    private String subject;//科目
    private String chapter;//章节
    private String founderName;//创建人
    private String teacherName;
    private String courseTimeLength;
    private long startTime;//开课时间
    private String content;//简介
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] coursePic;//图片数据0;
    private String teachAddress;//教学地址
    private String lessonTypeName;//课堂类型
    private String status;//课堂状态
    private long endTime;//结束时间
    private long createTime=System.currentTimeMillis();//创建时间
    private boolean qiandao;//是否签到
    private Integer count;//总课堂数


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

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

    public String getFounderName() {
        return founderName;
    }

    public void setFounderName(String founderName) {
        this.founderName = founderName;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
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

    public String getLessonTypeName() {
        return lessonTypeName;
    }

    public void setLessonTypeName(String lessonTypeName) {
        this.lessonTypeName = lessonTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isQiandao() {
        return qiandao;
    }

    public void setQiandao(boolean qiandao) {
        this.qiandao = qiandao;
    }
}
