package cn.com.szedu.repository;

import cn.com.szedu.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ICourseRepository extends JpaRepository<Course,String> {
    Course findFirstByid(String id);
    void deleteByid(String id);


   // List<Course> findBySubjectAndLessonTypeNameAndStartTime(String subject, String type, Date time);

    List<Course> findBySubjectAndLessonTypeNameAndStartTimeAndIdIn(String subject, String type, Date time,List<String> classId);

    Course findBySubjectAndLessonTypeNameAndStartTime(String subject, String type, Date time);
}
