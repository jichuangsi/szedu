package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.TeacherCourseRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ITeacherCouseRelationRepository extends JpaRepository<TeacherCourseRelation,Integer> {

    List<TeacherCourseRelation> findAllByTecherId(String teacherId);
    @Transactional
    Integer deleteByCourseIdAndTecherId(String courseId,String teacherId);

    TeacherCourseRelation findByCourseId(String teacherId);
}
