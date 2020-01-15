package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "courseware")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CourseWare {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String filename;//文件名
    private String teacherid;//老师id
    private String teacherName;
    private String filegroup;//组名
    private String filepath;//路径
    private String subject;//科目
    private String label;//标签
    private String describes;
    private String isCheck;//上传审核状态
    private String isShareCheck;//分享审核状态
    private Integer integral=0;//积分
    private String reason;//审核不通过原因
    private String type;//资源类型
    private String waysToget;//获得途径(1.购买
    private String coverPic;//封面
    private Integer buy=0;//购买数量
    private long createTime=new Date().getTime();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getFilegroup() {
        return filegroup;
    }

    public void setFilegroup(String filegroup) {
        this.filegroup = filegroup;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWaysToget() {
        return waysToget;
    }

    public void setWaysToget(String waysToget) {
        this.waysToget = waysToget;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getIsShareCheck() {
        return isShareCheck;
    }

    public void setIsShareCheck(String isShareCheckl) {
        this.isShareCheck = isShareCheckl;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public CourseWare(String filename, String teacherid, String filegroup, String filepath, String subject, String label, String describes, String isCheck, Integer integral, String reason, String type, String waysToget) {
        this.filename = filename;
        this.teacherid = teacherid;
        this.filegroup = filegroup;
        this.filepath = filepath;
        this.subject = subject;
        this.label = label;
        this.describes = describes;
        this.isCheck = isCheck;
        this.integral = integral;
        this.reason = reason;
        this.type = type;
        this.waysToget = waysToget;
    }

    public CourseWare() {
    }
}
