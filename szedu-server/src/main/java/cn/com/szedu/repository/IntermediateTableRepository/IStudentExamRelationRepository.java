package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ExamStudentRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentExamRelationRepository extends JpaRepository<ExamStudentRelation,Integer> {

}
