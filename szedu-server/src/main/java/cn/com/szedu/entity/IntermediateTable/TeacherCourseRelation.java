package cn.com.szedu.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "techer_course_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class TeacherCourseRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String courseId;
    private String techerId;

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

    public String getTecherId() {
        return techerId;
    }

    public void setTecherId(String techerId) {
        this.techerId = techerId;
    }
}
