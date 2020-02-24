package cn.com.szedu.repository;

import cn.com.szedu.entity.StudentAnswerCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStudentAnswerCollectionRepository extends JpaRepository<StudentAnswerCollection,Integer> {
    int countByStudentIdAndExamId(String studentId,String examId);
    List<StudentAnswerCollection> findByExamIdAndStudentId(String examId,String studentId);
    StudentAnswerCollection findAllByExamIdAndQuestionId(String examId,Integer questionId);
    //StudentAnswerCollection findAllByExamIdAndQuestionIdAndIsTure(String examId,Integer questionId,String answer);
    List<StudentAnswerCollection> findAllByExamIdAndQuestionIdIn(String examId,List<Integer> questionId);
    List<StudentAnswerCollection> findAllByExamIdAndQuestionIdInAndIsTure(String examId,List<Integer> questionId,String answer);

    List<StudentAnswerCollection> findByExamIdAndQuestionId(String examId,Integer questionId);
    List<StudentAnswerCollection> findByExamIdAndQuestionIdAndIsTure(String examId,Integer questionId,String answer);

}
