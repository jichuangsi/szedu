package cn.com.szedu.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name = "courseware_share")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CourseWareShare {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String gradeid;//年级id
    private String subjectid;//科目id
    private String coursewareid;//课件id
    private String coursewarename;//课件名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGradeid() {
        return gradeid;
    }

    public void setGradeid(String gradeid) {
        this.gradeid = gradeid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getCoursewareid() {
        return coursewareid;
    }

    public void setCoursewareid(String coursewareid) {
        this.coursewareid = coursewareid;
    }

    public String getCoursewarename() {
        return coursewarename;
    }

    public void setCoursewarename(String coursewarename) {
        this.coursewarename = coursewarename;
    }
}
