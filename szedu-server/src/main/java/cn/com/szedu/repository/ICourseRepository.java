package cn.com.szedu.repository;

import cn.com.szedu.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface ICourseRepository extends JpaRepository<Course,String>,JpaSpecificationExecutor<Course> {
    @Transactional
    Course findFirstById(String id);
    @Transactional
    void deleteById(String id);

    Course findOneById(String id);

    //List<Course> findBySubjectIdAndLessonTypeNameAndStartTimeAndIdInOrderByStartTimeIdDesc(Integer subject, String type, long time,List<String> courseId);
  /* // List<Course> findBySubjectAndLessonTypeNameAndStartTime(String subject, String type, Date time);
   List<Course> findBySubjectIdAndLessonTypeNameAndStartTimeAndIdInOrderByStartTimeIdDesc(Integer subject, String type, long time,List<String> courseId);
   // List<Course> findBySubjectIdAndLessonTypeNameAndStartTimeAndIdInAndOrderByStartTimeIdDesc(Integer subject, String type, long time,List<String> courseId);
    List<Course> findBySubjectAndLessonTypeNameAndIdIn(String subject, String type,List<String> classId);
    Course findBySubjectAndLessonTypeNameAndStartTime(String subject, String type, Date time);
    List<Course> getBySubjectAndLessonTypeNameAndStartTimeAndIdIn(String subject, String type, String time,List<String> courseId);
*/
    List<Course> findBySubjectIdAndLessonTypeNameAndStartTimeAndIdIn(Integer subject, String type, long time,List<String> courseId);
}
