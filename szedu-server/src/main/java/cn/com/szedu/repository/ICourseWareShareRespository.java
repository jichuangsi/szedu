package cn.com.szedu.repository;

import cn.com.szedu.entity.CourseWareShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ICourseWareShareRespository extends JpaRepository<CourseWareShare,String>,PagingAndSortingRepository<CourseWareShare,String>,JpaSpecificationExecutor<CourseWareShare> {
    void deleteByid(String id);
    List<CourseWareShare> findByGradeidAndSubjectid(String gradeId, String subjectId);
    List<CourseWareShare> findByGradeidInAndSubjectidIn(List<String> gradeId, List<String> subjectId);
    void deleteByCoursewareid(String Coursewareid);
}
