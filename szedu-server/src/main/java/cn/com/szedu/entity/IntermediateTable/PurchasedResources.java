package cn.com.szedu.entity.IntermediateTable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchasedResources")
public class PurchasedResources {//购买资源表
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String resourceId;//复制后资源id
    private String resourceName;
    private Integer integral;//积分
    private String teacherId;
    private String oldResourceId;//原资源id
    private long createTime=new Date().getTime();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getOldResourceId() {
        return oldResourceId;
    }

    public void setOldResourceId(String oldResourceId) {
        this.oldResourceId = oldResourceId;
    }

    public PurchasedResources(String resourceId, String resourceName, Integer integral, String teacherId, String oldResourceId) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.integral = integral;
        this.teacherId = teacherId;
        this.oldResourceId = oldResourceId;
    }

    public PurchasedResources() {
    }
}
