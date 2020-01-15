package cn.com.szedu.repository;

import cn.com.szedu.entity.StudentAnswerCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStudentAnswerCollectionRepository extends JpaRepository<StudentAnswerCollection,Integer> {
    int countByStudentIdAndExamId(String studentId,String examId);
    List<StudentAnswerCollection> findByExamIdAndStudentId(String examId,String studentId);
}
