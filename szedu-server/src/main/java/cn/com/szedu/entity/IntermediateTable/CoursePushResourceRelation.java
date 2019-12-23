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
