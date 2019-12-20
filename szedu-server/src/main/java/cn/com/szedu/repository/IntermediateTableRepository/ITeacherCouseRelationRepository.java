package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.TeacherCourseRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITeacherCouseRelationRepository extends JpaRepository<TeacherCourseRelation,Integer> {
    List<TeacherCourseRelation> findAllByTecherId(String teacherId);
}
