package cn.com.szedu.dao.mapper;

import cn.com.szedu.entity.Course;
import cn.com.szedu.entity.Exam;
import cn.com.szedu.model.CourseModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ICourseWareMapper {
    @Select("<script>SELECT u.`name` as teacherName,u.id as teacherId,c.id as resourceId,c.filename as resourceName,c.describes as describes,c.integral AS integral,c.is_check as isCheck FROM courseware c INNER JOIN  school_user u ON c.teacherid=u.id </script>")
    List<CourseModel> getCheckResourceList();

    @Select("<script>SELECT c.id AS resourceId,c.filename AS resourceName,u.id AS teacherId,u.`name` AS teacherName,c.integral AS integral,c.describes as describes FROM courseware c INNER JOIN school_user u ON c.teacherid=u.id WHERE c.is_check='2'</script>")
    List<CourseModel> getResourceList();

    @Select("<script>SELECT c.id AS resourceId,c.filename AS resourceName,u.id AS teacherId,u.`name` AS teacherName,c.integral AS integral,c.describes as describes FROM courseware c INNER JOIN school_user u ON c.teacherid=u.id WHERE c.is_check='2' AND c.filename like #{name}</script>")
    List<CourseModel> getResourceListByName(@Param("name")String name);

    @Select("<script>SELECT e.id AS id,e.exam_name AS examName,e.course as course,e.test_time_length AS testTimeLength,e.start_time AS startTime,e.creator_id AS creatorId,u.`name` AS creatorName  FROM exam e INNER JOIN school_user u ON e.creator_id=u.id</script>")
    List<Exam> getAllExam();

    @Select("<script>SELECT e.id AS id,e.exam_name AS examName,e.course as course,e.test_time_length AS testTimeLength,e.start_time AS startTime,e.creator_id AS creatorId,u.`name` AS creatorName  FROM exam e INNER JOIN school_user u ON e.creator_id=u.id  WHERE e.exam_name like #{name}</script>")
    List<Exam> getAllExamByname(@Param("name")String name);

    @Select("<script>SELECT c.id as id,c.content as content,c.course_time_length as courseTimeLength,c.course_title as courseTitle,c.start_time as startTime,c.`subject` AS subject,c.teacher_id as teacherId,u.`name` as teacherName FROM course c INNER JOIN school_user u ON c.teacher_id =u.id </script>")
    List<Course> getAllCourse();

    @Select("<script>SELECT c.id as id,c.content as content,c.course_time_length as courseTimeLength,c.course_title as courseTitle,c.start_time as startTime,c.`subject` AS subject,c.teacher_id as teacherId,u.`name` as teacherName FROM course c INNER JOIN school_user u ON c.teacher_id =u.id WHERE c.course_title like #{name}</script>")
    List<Course> getAllCourseByName(@Param("name")String name);
}
