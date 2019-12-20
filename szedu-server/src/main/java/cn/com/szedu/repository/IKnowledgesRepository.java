package cn.com.szedu.repository;

import cn.com.szedu.entity.IntermediateTable.Knowledges;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IKnowledgesRepository extends JpaRepository<Knowledges,Integer> {
    void deleteByQuestionId(Integer questionId);
}
