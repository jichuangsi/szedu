package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ExamClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IExamClassRelationRepository extends JpaRepository<ExamClassRelation,Integer> {
    List<ExamClassRelation> findByExamId(String examId);
    List<ExamClassRelation> findByClassIdIn(List<String> classIds);
}
