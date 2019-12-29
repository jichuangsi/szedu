package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ArticleUserRelation;
import cn.com.szedu.entity.IntermediateTable.CourseUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICourseUserRelationRepository extends JpaRepository<CourseUserRelation,Integer> {
    @Transactional
    void deleteByCourseId(String courseId);

    CourseUserRelation findByCourseId(String courseId);
    List<CourseUserRelation> findAllByUid(String teacherId);
}
