package cn.com.szedu.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "CoursePushResourceRelation")
public class CoursePushResourceRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String courseId;
    private String pushresourceid;
    private String push;//是否推送Y是N否

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getPushresourceid() {
        return pushresourceid;
    }

    public void setPushresourceid(String pushresourceid) {
        this.pushresourceid = pushresourceid;
    }

    public CoursePushResourceRelation(String courseId, String pushresourceid) {
        this.courseId = courseId;
        this.pushresourceid = pushresourceid;
    }
    public CoursePushResourceRelation() {
    }
}
