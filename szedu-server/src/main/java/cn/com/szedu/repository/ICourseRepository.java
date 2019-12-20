package cn.com.szedu.repository;

import cn.com.szedu.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseRepository extends JpaRepository<Course,String> {
    Course findFirstByid(String id);
    void deleteByid(String id);
}
