package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CoursePushResourceRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ICoursePushWareRelationRepository extends JpaRepository<CoursePushResourceRelation,Integer> {
    @Transactional
    Integer deleteByCourseId(String courseId);

    List<CoursePushResourceRelation> findByCourseId(String courseId);
    List<CoursePushResourceRelation> findByCourseIdInAndPushAndPushTime(List<String> courseId,String push,long time);
    @Transactional
    @Modifying
    @Query(value = " UPDATE `course_push_resource_relation` SET push=Y WHERE `course_id`:courseId",nativeQuery = true)
    void updateByCourseId(@Param("courseId") String courseId);

    List<CoursePushResourceRelation> findByCourseIdAndPush(String courseId,String push);

}
