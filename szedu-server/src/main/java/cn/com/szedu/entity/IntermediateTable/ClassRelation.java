package cn.com.szedu.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "techer_class_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class ClassRelation {//班级老师
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String classId;
    private String techerId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTecherId() {
        return techerId;
    }

    public void setTecherId(String techerId) {
        this.techerId = techerId;
    }

    public ClassRelation(String classId, String techerId) {
        this.classId = classId;
        this.techerId = techerId;
    }
    public ClassRelation() {
    }
}
