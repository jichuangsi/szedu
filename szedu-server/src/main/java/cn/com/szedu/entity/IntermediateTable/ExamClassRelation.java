package cn.com.szedu.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "examClassRelation")
public class ExamClassRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String examId;
    private String classId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public ExamClassRelation(String examId, String classId) {
        this.examId = examId;
        this.classId = classId;
    }

    public ExamClassRelation() {
    }
}
