package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CourseClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface IClassCourseRelationRepository extends JpaRepository<CourseClassRelation,Integer> {
    @Transactional
    Integer deleteByCourseId(String courseId);
    List<CourseClassRelation> findByCourseId(String courseId);
    //Integer deleteByCourseId(String courseId);

    List<CourseClassRelation> findByClassIdIn(List<String> classId);
}
