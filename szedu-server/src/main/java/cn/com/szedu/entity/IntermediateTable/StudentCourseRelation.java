package cn.com.szedu.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name ="student_course_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class StudentCourseRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String studentId;//学生ID
    private String courseId;//课堂id
    private Integer scorse;//评分

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getScorse() {
        return scorse;
    }

    public void setScorse(Integer scorse) {
        this.scorse = scorse;
    }
}
