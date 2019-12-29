package cn.com.szedu.dao.mapper;

import cn.com.szedu.entity.ClassInfo;
import cn.com.szedu.entity.Course;
import cn.com.szedu.entity.Message;
import cn.com.szedu.entity.TeacherInfo;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.teacher.ClassModel;
import cn.com.szedu.model.teacher.StudentCourseScoreModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IClassInfoMapper {

    @Select("<script>SELECT c.id AS classId,c.`class_name` AS className ,c.`school_id` AS schoolId,c.`school_name` AS schoolName,\n" +
            "c.`speciality` AS specialty,c.`ru_time` AS enrollTime,c.`open_time` AS startTime,c.`content` AS content,\n" +
            "c.`status` AS `status` ,c.`founder` AS founder,c.`founder_id` AS founderId,c.`educational_system` AS educationalSystem\n" +
            ",c.`create_time` AS createTime,t.* FROM class_info c INNER JOIN `techer_class_relation` tcr \n" +
            "ON c.`id`=tcr.`class_id` INNER JOIN `teacher_info` t ON tcr.`techer_id`=t.`id`\n" +
            "WHERE t.`id`=#{teacherId} ORDER BY c.`create_time` DESC LIMIT #{num},#{size}</script>")
    List<ClassModel> getPageByTeacherId(@Param("teacherId") String teacherId,@Param("num") int num,@Param("size") int size);
    @Select("<script>SELECT s.`name` AS `name`,s.`student_id` AS studentId,s.`account` AS account, ci.`class_name` AS classId,ci.`speciality`AS specialtity,\n" +
        "\ts.`phone` AS phone\n" +
        "\tFROM `student_info` s  INNER JOIN `student_class_relation` scr ON s.id=scr.`student_id`\n" +
        "\tINNER JOIN `class_info` ci ON scr.`class_id`=ci.`id`\n" +
        "\tWHERE scr.`class_id`=#{classId} LIMIT #{num},#{size}</script>")
    List<StudentModel> getPageByClassId(@Param("classId") String classId,@Param("num") int num,@Param("size") int size);

    @Select("<script>SELECT AVG(scorse) AS `avg` FROM `student_course_relation` WHERE course_id=#{courseId}</script>")
     Double getCourseAvg(String courseId);
    @Select("<script>UPDATE `message` SET `already_read`=0 WHERE `recipient_id`=#{teacherId}</script>")
    Integer updateReady(String teacherId);

    @Select("<script>SELECT * FROM `course` WHERE `teacher_id`=#{teacherId} `lesson_type_name`=#{type} \n" +
            "`subject_id`=#{subject} `start_time`=#{startTime}  LIMIT #{num},#{size}</script>")
    List<Course> getCourseByTeaherAndSubjectAndType(@Param("teacherId") String teacherId,@Param("type") String type,
                                                    @Param("subject") Integer subject, @Param("startTime") long startTime
                                                ,@Param("num") int num,@Param("size") int size);

    @Select("<script>SELECT t.* FROM `teacher_info` t,`techer_course_relation` tc\n" +
            "  WHERE t.id=tc.`techer_id` AND `course_id`=#{courseId}</script>")
    TeacherInfo getTeacherByCourse(@Param("courseId") String courseId);

    @Select("<script>SELECT c.* FROM `class_info` c,`course_class_relation` cc\n" +
            "  WHERE c.id=cc.`class_id` AND `course_id`=#{courseId}</script>")
    List<ClassInfo> getClassByCourse(@Param("courseId") String courseId);

    @Select("<script> SELECT m.* FROM `message` m,`messageuserrelation` mu WHERE m.id=mu.`m_id`\n" +
            "  AND `u_id`=#{userId}  LIMIT #{num},#{size}</script>")
    List<Message> getMessageByUser(@Param("userId")String userId ,@Param("num") int num,@Param("size") int size);
}
