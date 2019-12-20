package cn.com.szedu.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name ="student_class_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class StudentClassRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String studentId;//学生ID
    private String classId;//班级id

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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public StudentClassRelation(String studentId, String classId) {
        this.studentId = studentId;
        this.classId = classId;
    }

    public StudentClassRelation() {
    }
}
