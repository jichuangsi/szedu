package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.StudentCourseRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStudentCourseScoreRepository extends JpaRepository<StudentCourseRelation,Integer> {

    List<StudentCourseRelation> findByCourseId(String courseId);
    Integer countByCourseIdAndScorse(String courseId,Integer count);
    Integer countByCourseId(String courseId);

    List<StudentCourseRelation> findByStudentId(String studentId);

    StudentCourseRelation findByCourseIdAndStudentId(String courseId,String studentId);

}
