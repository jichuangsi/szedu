package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CollectionCourseWare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ICollectionCourseWareRepository extends JpaRepository<CollectionCourseWare,Integer> {
    List<CollectionCourseWare> findByUserIdOrderByCreateTimeDesc(String userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM collection_course_ware WHERE course_id=?1 AND user_id=?2",nativeQuery = true)
    void deleteByCourseIdAndUserId(String courseId,String userId);
}
