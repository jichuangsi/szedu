package cn.com.szedu.repository;

import cn.com.szedu.entity.CourseWare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ICourseWareRespository extends JpaRepository<CourseWare,String>,PagingAndSortingRepository<CourseWare,String>,JpaSpecificationExecutor<CourseWare> {
    List<CourseWare> findByTeacherid(String teacherId);
    List<CourseWare> findByFilename(String fileName);
    int countByTeacherid(String teacherId);
    void deleteByid(String id);
    List<CourseWare> findByidIn(List<String> courseWareId);
    List<CourseWare> findBySubjectAndLabelAndIdIn(Integer subject,String lable,List<String> courseWareId);
    CourseWare findByid(String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CourseWare SET is_check=?2 WHERE id=?1")
    void updateIsCheck(String id,String status);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CourseWare SET is_share_check=?2 WHERE id=?1")
    void updateIsShareCheck(String id,String status);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CourseWare SET is_share_check=?2,integral=?3 WHERE id=?1")
    void updateIsShareCheckAndIntegral(String id,String status,Integer integral);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CourseWare SET integral=?2 where id=?1")
    void updateIntegral(String id,Integer integral);

    List<CourseWare> findByTeacheridAndIsCheckAndType(String teacherId,String check,String type);

    List<CourseWare> findByTeacheridInAndIsShareCheck(List<String> teacherIds,String status);
    int countByTeacheridInAndIsShareCheck(List<String> teacherIds,String status);

    @Query(value = "SELECT * FROM CourseWare WHERE teacherid IN ?1 AND is_share_check=?2 ORDER BY buy DESC LIMIT ?3,?4",nativeQuery = true)
    List<CourseWare> getTeacheridInAndIsShareCheck(List<String> teacherIds,String status,int num,int size);

    @Query(value = "SELECT * FROM CourseWare WHERE teacherid IN ?1 AND is_share_check=?2 ORDER BY buy,create_time DESC LIMIT ?3,?4",nativeQuery = true)
    List<CourseWare> getTeacheridInAndIsShareCheckOrderOrderByCreateTime(List<String> teacherIds,String status,int num,int size);
}
