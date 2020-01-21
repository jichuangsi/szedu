package cn.com.szedu.dao.mapper;

import cn.com.szedu.entity.*;
import cn.com.szedu.model.StudentRankModel;
import cn.com.szedu.model.student.StudentIntegralModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.student.StudyModel;
import cn.com.szedu.model.teacher.ClassModel;
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
    List<ClassModel> getPageByTeacherId(@Param("teacherId") String teacherId, @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT s.`name` AS `name`,s.`student_id` AS studentId,s.`account` AS account, ci.`class_name` AS classId,ci.`speciality`AS specialtity,\n" +
            "\ts.`phone` AS phone\n" +
            "\tFROM `student_info` s  INNER JOIN `student_class_relation` scr ON s.id=scr.`student_id`\n" +
            "\tINNER JOIN `class_info` ci ON scr.`class_id`=ci.`id`\n" +
            "\tWHERE scr.`class_id`=#{classId} LIMIT #{num},#{size}</script>")
    List<StudentModel> getPageByClassId(@Param("classId") String classId, @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT AVG(scorse) AS `avg` FROM `student_course_relation` WHERE course_id=#{courseId}</script>")
    Double getCourseAvg(String courseId);

    @Select("<script>UPDATE `message` SET `already_read`=0 WHERE `recipient_id`=#{teacherId}</script>")
    Integer updateReady(String teacherId);

    @Select("<script>SELECT * FROM `course` WHERE `teacher_id`=#{teacherId} `lesson_type_name`=#{type} \n" +
            "`subject_id`=#{subject} `start_time`=#{startTime}  LIMIT #{num},#{size}</script>")
    List<Course> getCourseByTeaherAndSubjectAndType(@Param("teacherId") String teacherId, @Param("type") String type,
                                                    @Param("subject") Integer subject, @Param("startTime") long startTime
            , @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT t.* FROM `teacher_info` t,`techer_course_relation` tc\n" +
            "  WHERE t.id=tc.`techer_id` AND `course_id`=#{courseId}</script>")
    TeacherInfo getTeacherByCourse(@Param("courseId") String courseId);

    @Select("<script>SELECT c.* FROM `class_info` c,`course_class_relation` cc\n" +
            "  WHERE c.id=cc.`class_id` AND `course_id`=#{courseId}</script>")
    List<ClassInfo> getClassByCourse(@Param("courseId") String courseId);

    @Select("<script> SELECT  m.*,m.`recipient_id` AS recipientId,m.`recipient_name` AS recipientName,m.`sender_name` AS senderName \n" +
            ",m.`already_read` AS alreadyRead  FROM `message` m,`messageuserrelation` mu WHERE m.id=mu.`m_id`\n" +
            "  AND `u_id`=#{userId} AND `send`=\"Y\" ORDER BY `time` DESC  LIMIT #{num},#{size}</script>")
    List<Message> getMessageByUser(@Param("userId") String userId, @Param("num") int num, @Param("size") int size);

    @Select("<script> SELECT cw.* FROM `courseware` cw INNER JOIN `course_push_resource_relation` cp ON cw.id=cp.`pushresourceid`\n" +
            "INNER JOIN `course_class_relation` cc ON cc.id=cp.`course_id` INNER JOIN `student_class_relation` sc \n" +
            "ON sc.id=cc.`class_id` INNER JOIN `student_info` s ON s.id=sc.`student_id` WHERE s.id=#{studentId} \n" +
            "AND cw.`subject`=#{subjectId} AND cw.`label`=#{type} AND cp.`push_time`=#{time}</script>")
    List<CourseWare> getPushResourseByStudent(@Param("studentId") String studentId, @Param("subjectId") Integer subjectId,
                                              @Param("type") String type, @Param("time") long time);


    @Select("<script>SELECT DISTINCT`student_id`,`exam_id` FROM `student_answer_collection` WHERE `exam_id`=#{examId}</script>")
    List<StudentAnswerCollection> getStudentByExam(@Param("examId") String examId);

    @Select("<script>SELECT toq.* FROM `exam` e INNER JOIN `testpaper` t ON `test_paper_id`=t.`id`=e.`test_paper_id` INNER JOIN \n" +
            "`testpaper_question_relation` tqr ON tqr.`test_paper`=t.`id` INNER JOIN `top_questions` toq ON toq.id=tqr.`question_id` \n" +
            "WHERE e.id=#{examId}</script>")
    List<TopQuestions> getQuestionByExam(@Param("examId") String examId);

    @Select("<script>SELECT * FROM `message` WHERE `recipient_id`=#{userId} AND `senderid`=#{sendId}} \n" +
            "OR `recipient_id`=#{sendId} AND `senderid`=#{userId} ORDER BY `time` DESC  LIMIT #{num},#{size}</script>")
    List<Message> getMessageBysenderid(@Param("userId") String userId, @Param("sendId") String sendId, @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT COUNT(*) FROM `student_info` s INNER JOIN `student_class_relation` scr ON s.id=scr.`student_id`\n" +
            "INNER JOIN `course_class_relation` ccr ON scr.`class_id`=ccr.`class_id` WHERE s.id=#{userId}</script>")
    Integer countStudentLesson(@Param("userId") String userId);

    /* @Select("<script>SELECT id AS studentId,account AS acount ,`name` AS studentName,`integral`AS integral \n" +
             "FROM `student_info` ORDER BY `integral` DESC LIMIT #{num},#{size}</script>")
     List<StudentIntegralModel> findIntegeralRank(@Param("num") int num,@Param("size") int size);*/
    @Select("<script>SELECT id AS studentId,account AS acount ,`name` AS studentName,`integral`AS integral \n" +
            "FROM `student_info` ORDER BY `integral` DESC</script>")
    List<StudentIntegralModel> findIntegeralRank();

    @Select("<script>SELECT id AS studentId FROM `student_info` ORDER BY `integral` DESC LIMIT #{num},#{size}</script>")
    List<String> findRank(@Param("num") int num, @Param("size") int size);

    /*@Select("<script>SELECT * FROM `message` WHERE `recipient_id`=#{userId} AND `senderid` IS NULL \n" +
            "OR `recipient_id` IN \n" +
            "<foreach collection='reId' item='item' open='(' separator=',' close=')'>'#{item}'</foreach> \n" +
            "AND `senderid` IS NULL ORDER BY `time` DESC  LIMIT #{num},#{size}</script>")*/
   /* @Select("<script>SELECT m.*,m.`recipient_id` AS recipientId,m.`recipient_name` AS recipientName,m.`sender_name` AS senderName \n" +
            ",m.`already_read` AS alreadyRead FROM `message` m \n" +
            "WHERE `recipient_id`=#{userId}  AND `senderid` IS NULL \n" +
            "OR `recipient_id` IN(SELECT `class_id` FROM `student_class_relation` WHERE `student_id`=#{userId})AND `senderid` IS NULL \n" +
            " ORDER BY `time` DESC LIMIT #{num},#{size}</script>")
    List<Message> getStudentMessage(@Param("userId") String userId, @Param("reId") List<String> reId, @Param("num") int num, @Param("size") int size);
*/

    @Select("<script>SELECT m.*,m.`recipient_id` AS recipientId,m.`recipient_name` AS recipientName,m.`sender_name` AS senderName \n" +
            ",m.`already_read` AS alreadyRead FROM `message` m \n" +
            "WHERE `recipient_id`=#{userId}  AND `senderid` IS NULL \n" +
            "ORDER BY `time` DESC LIMIT #{num},#{size}</script>")
    List<Message> getStudentMessage(@Param("userId") String userId, @Param("num") int num, @Param("size") int size);


    @Select("<script>SELECT m.*,m.`recipient_id` AS recipientId,m.`recipient_name` AS recipientName,m.`sender_name` AS senderName \n" +
            ",m.`already_read` AS alreadyRead FROM `message` m \n" +
            " WHERE `recipient_id`=#{userId}  AND `send`=\"Y\" \n" +
            "AND `senderid` IS NULL ORDER BY `time` ASC LIMIT #{num},#{size}</script>")
    List<Message> getTeacherMessage(@Param("userId") String userId, @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT s.`id` as id, s.`name` AS `name`,s.`student_id` AS studentId,s.`account` AS account, ci.`class_name` AS classId,ci.`speciality`AS specialtity,\n" +
            "\ts.`phone` AS phone\n" +
            "\tFROM `student_info` s  INNER JOIN `student_class_relation` scr ON s.id=scr.`student_id`\n" +
            "\tINNER JOIN `class_info` ci ON scr.`class_id`=ci.`id`\n" +
            "\tWHERE scr.`class_id`=#{classId}</script>")
    List<StudentModel> getByClassId(@Param("classId") String classId);


    @Select("<script>SELECT c.id AS classId,c.`class_name` AS className ,c.`school_id` AS schoolId,c.`school_name` AS schoolName,\n" +
            "c.`speciality` AS specialty,c.`ru_time` AS enrollTime,c.`open_time` AS startTime,c.`content` AS content,\n" +
            "c.`status` AS `status` ,c.`founder` AS founder,c.`founder_id` AS founderId,c.`educational_system` AS educationalSystem\n" +
            ",c.`create_time` AS createTime,t.* FROM class_info c INNER JOIN `techer_class_relation` tcr \n" +
            "ON c.`id`=tcr.`class_id` INNER JOIN `teacher_info` t ON tcr.`techer_id`=t.`id`\n" +
            "WHERE t.`id`=#{teacherId} ORDER BY c.`create_time` DESC</script>")
    List<ClassModel> getTeacherIdById(@Param("teacherId") String teacherId);

    @Select("<script>UPDATE teacher_info SET pwd=#{pwd} where id=#{teacherId}</script>")
    void updatePwd(String teacherId, String pwd);

    @Select("<script>UPDATE teacher_info SET status=#{status} WHERE id=#{teacherId}</script>")
    void updateIsStatus(String teacherId, String status);

   /* @Select("<script>SELECT * FROM `course`WHERE `subject_id`=#{time} AND`lesson_type_name`=#{time}  \n"+
           " AND `tsatus` <>'N' AND id IN( \n"+
            "  SELECT `course_id` FROM `course_class_relation` WHERE `class_id` IN \n"+
            "  (SELECT `class_id` FROM  `student_class_relation` WHERE `student_id` =#{studentId} )) \n"+
             "AND `start_time` BETWEEN #{startTime} AND #{time}</script>")
    int countNum(@Param("subjectId") Integer subjectId, @Param("lessonTypeName") String lessonTypeName,
              @Param("startTime") long startTime, @Param("time") long time,@Param("studentId") String studentId);*/


    @Select("<script>SELECT cw.`filename` AS `filename`,cw.`filepath` AS `filepath` ,cw.`teacher_name` AS teacherName,cw.`cover_pic` AS coverpic,\n" +
            "\tcp.`push_time` AS pushtime,re.`type_name` AS `type`,cw.`subject`AS `subject`,pl.`name` AS label" +
            " FROM `courseware` cw INNER JOIN `resources_rule` re ON cw.type=re.id \n" +
            "\tINNER JOIN `course_push_resource_relation` cp ON cp.`pushresourceid` =cw.id INNER JOIN `upload_label` pl ON pl.`id`=cw.`label` WHERE cp.`push`= 'Y' " +
            "\tAND cp.`course_id` IN(SELECT `course_id` FROM `course_class_relation` cc INNER JOIN `student_class_relation` sc \n" +
            "\tON cc.`class_id`=sc.`class_id` WHERE `student_id`=#{studentId})\n" +
            "\tAND cw.`subject`=#{subject} AND re.`type_name`=#{type}" +
            "\tORDER BY `pushtime` ASC LIMIT #{num},#{size}</script>")
    List<StudyModel> listCourseWare(@Param("studentId") String studentId, @Param("subject") String subject, @Param("type") String type, @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT COUNT(*) FROM `courseware` cw INNER JOIN `resources_rule` re ON cw.type=re.id \n" +
            "\tINNER JOIN `course_push_resource_relation` cp ON cp.`pushresourceid` =cw.id INNER JOIN `upload_label` pl ON pl.`id`=cw.`label` WHERE cp.`push`= 'Y' " +
            "\tAND cp.`course_id` IN(SELECT `course_id` FROM `course_class_relation` cc INNER JOIN `student_class_relation` sc \n" +
            "\tON cc.`class_id`=sc.`class_id` WHERE `student_id`=#{studentId})\n" +
            "\tAND cw.`subject`=#{subject} AND re.`type_name`=#{type}</script>")
    Integer countCourseWare(@Param("studentId") String studentId, @Param("subject") String subject, @Param("type") String type);

    @Select("<script>SELECT cw.`filename` AS `filename`,cw.`filepath` AS `filepath` ,cw.`teacher_name` AS teacherName,cw.`cover_pic` AS coverpic,\n" +
            "\tcp.`push_time` AS pushtime,re.`type_name` AS `type`,cw.`subject`AS `subject`,pl.`name` AS label" +
            " FROM `courseware` cw INNER JOIN `resources_rule` re ON cw.type=re.id \n" +
            "\tINNER JOIN `course_push_resource_relation` cp ON cp.`pushresourceid` =cw.id INNER JOIN `upload_label` pl ON pl.`id`=cw.`label` WHERE cp.`push`= 'Y' " +
            "\tAND cp.`course_id` IN(SELECT `course_id` FROM `course_class_relation` cc INNER JOIN `student_class_relation` sc \n" +
            "\tON cc.`class_id`=sc.`class_id` WHERE `student_id`=#{studentId})\n" +
            "\tORDER BY `pushtime` ASC LIMIT #{num},#{size}</script>")
    List<StudyModel> listCourseWareAll(@Param("studentId") String studentId, @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT COUNT(*) FROM `courseware` cw INNER JOIN `resources_rule` re ON cw.type=re.id \n" +
            "\tINNER JOIN `course_push_resource_relation` cp ON cp.`pushresourceid` =cw.id INNER JOIN `upload_label` pl ON pl.`id`=cw.`label` WHERE cp.`push`= 'Y' " +
            "\tAND cp.`course_id` IN(SELECT `course_id` FROM `course_class_relation` cc INNER JOIN `student_class_relation` sc \n" +
            "\tON cc.`class_id`=sc.`class_id` WHERE `student_id`=#{studentId})</script>")
    Integer countCourseWareAll(@Param("studentId") String studentId);

    @Select("<script>SELECT cw.`filename` AS `filename`,cw.`filepath` AS `filepath` ,cw.`teacher_name` AS teacherName,cw.`cover_pic` AS coverpic,\n" +
            "\tcp.`push_time` AS pushtime,re.`type_name` AS `type`,cw.`subject`AS `subject`,pl.`name` AS label" +
            " FROM `courseware` cw INNER JOIN `resources_rule` re ON cw.type=re.id \n" +
            "\tINNER JOIN `course_push_resource_relation` cp ON cp.`pushresourceid` =cw.id INNER JOIN `upload_label` pl ON pl.`id`=cw.`label` WHERE cp.`push`= 'Y' " +
            "\tAND cp.`course_id` IN(SELECT `course_id` FROM `course_class_relation` cc INNER JOIN `student_class_relation` sc \n" +
            "\tON cc.`class_id`=sc.`class_id` WHERE `student_id`=#{studentId})\n" +
            "\tAND cw.`subject`=#{subject}" +
            "\tORDER BY `pushtime` ASC LIMIT #{num},#{size}</script>")
    List<StudyModel> listCourseWareSubject(@Param("studentId") String studentId, @Param("subject") String subject, @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT COUNT(*) FROM `courseware` cw INNER JOIN `resources_rule` re ON cw.type=re.id \n" +
            "\tINNER JOIN `course_push_resource_relation` cp ON cp.`pushresourceid` =cw.id INNER JOIN `upload_label` pl ON pl.`id`=cw.`label` WHERE cp.`push`= 'Y' " +
            "\tAND cp.`course_id` IN(SELECT `course_id` FROM `course_class_relation` cc INNER JOIN `student_class_relation` sc \n" +
            "\tON cc.`class_id`=sc.`class_id` WHERE `student_id`=#{studentId})\n" +
            "\tAND cw.`subject`=#{subject}</script>")
    Integer countCourseWareSubject(@Param("studentId") String studentId, @Param("subject") String subject);


    @Select("<script>SELECT cw.`filename` AS `filename`,cw.`filepath` AS `filepath` ,cw.`teacher_name` AS teacherName,cw.`cover_pic` AS coverpic,\n" +
            "\tcp.`push_time` AS pushtime,re.`type_name` AS `type`,cw.`subject`AS `subject` ,pl.`name` AS label" +
            "\tFROM `courseware` cw INNER JOIN `resources_rule` re ON cw.type=re.id \n" +
            "\tINNER JOIN `course_push_resource_relation` cp ON cp.`pushresourceid` =cw.id INNER JOIN `upload_label` pl ON pl.`id`=cw.`label` WHERE cp.`push`= 'Y' " +
            "\tAND cp.`course_id` IN(SELECT `course_id` FROM `course_class_relation` cc INNER JOIN `student_class_relation` sc \n" +
            "\tON cc.`class_id`=sc.`class_id` WHERE `student_id`=#{studentId})\n" +
            "\tAND  re.`type_name`=#{type}" +
            "\tORDER BY `pushtime` DESC LIMIT #{num},#{size}</script>")
    List<StudyModel> listCourseWareType(@Param("studentId") String studentId, @Param("type") String type, @Param("num") int num, @Param("size") int size);

    @Select("<script>SELECT COUNT(*) FROM `courseware` cw INNER JOIN `resources_rule` re ON cw.type=re.id \n" +
            "\tINNER JOIN `course_push_resource_relation` cp ON cp.`pushresourceid` =cw.id INNER JOIN `upload_label` pl ON pl.`id`=cw.`label` WHERE cp.`push`= 'Y' " +
            "\tAND cp.`course_id` IN(SELECT `course_id` FROM `course_class_relation` cc INNER JOIN `student_class_relation` sc \n" +
            "\tON cc.`class_id`=sc.`class_id` WHERE `student_id`=#{studentId})\n" +
            "\tAND re.`type_name`=#{type}</script>")
    Integer countCourseWareType(@Param("studentId") String studentId, @Param("type") String type);


    @Select("<script>SELECT SUM(`score`) FROM `student_answer_collection` WHERE `exam_id`=#{examId}" +
            " AND `student_id`=#{studentId}</script>")
    Integer sumScoreExam(@Param("studentId") String studentId, @Param("examId") String examId);

    @Select("<script>SELECT SUM(`score`) FROM `student_answer_collection` WHERE `exam_id`=#{examId} " +
            "AND `student_id`=#{studentId} AND `score` IS NOT NULL</script>")
    Integer sumScoreNotNull(@Param("studentId") String studentId, @Param("examId") String examId);

    @Select("<script>SELECT SUM(`score`) FROM `student_answer_collection` WHERE `exam_id`=#{examId} " +
            "AND `student_id`=#{studentId}</script>")
    Integer sumScore(@Param("studentId") String studentId, @Param("examId") String examId);

    @Select("<script>SELECT * ,SUM(`score`) AS s FROM `student_answer_collection` WHERE `exam_id`= #{examId} ORDER BY s DESC </script>")
    List<StudentAnswerCollection> sumScoreAsc(@Param("examId") String examId);


    @Select("<script> SET @curRank := 0;\n" +
            " SELECT `student_id` AS studentId,SUM(`score`) AS score ,@curRank:= @curRank + 1 AS rank\n" +
            " FROM `student_answer_collection` WHERE `exam_id`=#{examId}  ORDER BY score DESC</script>")
    List<StudentRankModel> sumScoreCount(@Param("examId") String examId);

   /* @Select("<script>  SET @curRank := 0;\n" +
            " SELECT`student_id` AS studentId,SUM(`score`) AS score ,@curRank:= @curRank + 1 AS rank\n" +
            " FROM `student_answer_collection` WHERE `exam_id`=#{examId}  ORDER BY s ASC</script>")
    List<StudentRankModel> sumScoreCount2(@Param("examId") String examId);*/
}
