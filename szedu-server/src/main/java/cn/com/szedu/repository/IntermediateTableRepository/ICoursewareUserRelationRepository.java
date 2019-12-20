package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CourseUserRelation;
import cn.com.szedu.entity.IntermediateTable.CoursewareUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICoursewareUserRelationRepository extends JpaRepository<CoursewareUserRelation,Integer> {

}
