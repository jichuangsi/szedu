package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CourseResourceRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ICourseWareRelationRepository extends JpaRepository<CourseResourceRelation,Integer> {
    @Transactional
    Integer deleteByCourseId(String courseId);
    List<CourseResourceRelation> findByCourseId(String courseId);
}
