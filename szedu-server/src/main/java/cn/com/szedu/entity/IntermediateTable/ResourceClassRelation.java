package cn.com.szedu.entity.IntermediateTable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "resourceClassRelation")
public class ResourceClassRelation{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String resourceId;
    private String classId;
    private long createTime=new Date().getTime();//推送时间
    private String teacherId;

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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public ResourceClassRelation(String resourceId, String classId, String teacherId) {
        this.resourceId = resourceId;
        this.classId = classId;
        this.teacherId = teacherId;
    }

    public ResourceClassRelation() {
    }
}
