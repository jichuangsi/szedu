package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ExamUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExamUserRelationRepository extends JpaRepository<ExamUserRelation,Integer> {

}
