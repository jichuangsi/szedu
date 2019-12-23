package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CoursePushResourceRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICoursePushWareRelationRepository extends JpaRepository<CoursePushResourceRelation,Integer> {

    Integer deleteByCourseId(String courseId);
}
