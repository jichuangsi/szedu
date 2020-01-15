package cn.com.szedu.model;

import java.util.Date;

public class CourseModel {
    private String teacherId;
    private String resourceId;
    private String teacherName;//教师名
    private String resourceName;//资源名
    private String typeid;//资源类型
    private String resourceType;//资源标签
    private Integer integral;//积分
    private String resourceLabelId;//资源标签
    private String resourceLabel;//资源标签
    private String describes;//描述
    private String isCheck;//是否审核
    private String isShare;//是否分享（通过、不通过）
    private String reason;//审核不通过原因
    private Date updateTime;//修改时间
    private Integer buy;//购买数量
    private String waysToget;//获得途径(1.购买 我的资源
    private String isBuy;//获得途径(1.购买 我的资源
    private long createTime=new Date().getTime();
    private String coverPic;//封面

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getResourceLabel() {
        return resourceLabel;
    }

    public void setResourceLabel(String resourceLabel) {
        this.resourceLabel = resourceLabel;
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

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getResourceLabelId() {
        return resourceLabelId;
    }

    public void setResourceLabelId(String resourceLabelId) {
        this.resourceLabelId = resourceLabelId;
    }

    public String getWaysToget() {
        return waysToget;
    }

    public void setWaysToget(String waysToget) {
        this.waysToget = waysToget;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }
}
