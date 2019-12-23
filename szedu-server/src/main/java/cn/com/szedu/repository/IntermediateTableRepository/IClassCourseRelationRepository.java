package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CourseClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IClassCourseRelationRepository extends JpaRepository<CourseClassRelation,Integer> {

    Integer deleteByCourseId(String courseId);
    List<CourseClassRelation> findByCourseId(String courseId);
}
