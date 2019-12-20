package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.entity.IntermediateTable.StudentCourseRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentCourseRelationRepository extends JpaRepository<StudentCourseRelation,Integer> {

    StudentCourseRelation findByCourseIdAndStudentId(String courseId,String studentId);

}
