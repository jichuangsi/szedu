package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CourseResourceRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseWareRelationRepository extends JpaRepository<CourseResourceRelation,Integer> {

    Integer deleteByCourseId(String courseId);
}
