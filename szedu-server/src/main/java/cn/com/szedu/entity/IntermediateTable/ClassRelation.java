package cn.com.szedu.entity.IntermediateTable;

import cn.com.szedu.entity.ClassInfo;
import cn.com.szedu.entity.TeacherInfo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "techer_class_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class ClassRelation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String classId;
    private String techerId;
  /*  @ManyToOne(fetch=FetchType.LAZY, targetEntity=ClassInfo.class)
    @JoinColumn(name = "id")
    private ClassInfo classInfo;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity=TeacherInfo.class)
    @JoinColumn(name = "id")
    private TeacherInfo teacherInfo;

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }*/

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
