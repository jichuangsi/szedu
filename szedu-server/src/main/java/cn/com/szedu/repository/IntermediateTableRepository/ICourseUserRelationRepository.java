package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ArticleUserRelation;
import cn.com.szedu.entity.IntermediateTable.CourseUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseUserRelationRepository extends JpaRepository<CourseUserRelation,Integer> {

}
