package cn.com.szedu.repository;

import cn.com.szedu.entity.CourseWare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ICourseWareRespository extends JpaRepository<CourseWare,String>,PagingAndSortingRepository<CourseWare,String>,JpaSpecificationExecutor<CourseWare> {
    List<CourseWare> findByTeacherid(String teacherId);
    void deleteByid(String id);
    List<CourseWare> findByidIn(List<String> courseWareId);
    CourseWare findByid(String id);
}
