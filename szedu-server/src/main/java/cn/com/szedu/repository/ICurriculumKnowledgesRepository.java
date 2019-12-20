package cn.com.szedu.repository;

import cn.com.szedu.entity.IntermediateTable.CurriculumKnowledges;
import cn.com.szedu.entity.IntermediateTable.Knowledges;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICurriculumKnowledgesRepository extends JpaRepository<CurriculumKnowledges,Integer> {
    void deleteByCurriculumId(Integer curriculumId);
}
