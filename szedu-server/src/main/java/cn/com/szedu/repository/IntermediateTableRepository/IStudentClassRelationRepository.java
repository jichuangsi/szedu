package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStudentClassRelationRepository extends JpaRepository<StudentClassRelation,Integer> {

    List<StudentClassRelation> findAllByClassId(String classId);
    boolean deleteByClassIdAndStudentId(String classId,String studentId);
    List<StudentClassRelation> findByClassIdIn(List<String> classId);

    StudentClassRelation findByClassIdAndStudentId(String classId,String studentId);
}
