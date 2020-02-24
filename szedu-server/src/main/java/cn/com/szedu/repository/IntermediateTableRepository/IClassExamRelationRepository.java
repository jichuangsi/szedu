package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CourseClassRelation;
import cn.com.szedu.entity.IntermediateTable.ExamClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IClassExamRelationRepository extends JpaRepository<ExamClassRelation,Integer> {


    List<ExamClassRelation> findByClassId(String calssId);
    List<ExamClassRelation> findByClassIdIn(List<String> calssId);

    List<ExamClassRelation> findByExamId(String examId);

    ExamClassRelation findByClassIdAndExamId(String calssId,String examId);
}
