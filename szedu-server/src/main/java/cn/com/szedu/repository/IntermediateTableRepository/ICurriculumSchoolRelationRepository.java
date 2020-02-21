package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.CurriculumSchoolRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICurriculumSchoolRelationRepository extends JpaRepository<CurriculumSchoolRelation,Integer> {
    CurriculumSchoolRelation findByCurriculumIdAndSchoolId(Integer curriculumId,String schoolId);
    List<CurriculumSchoolRelation> findBySchoolId(String schoolId);
}
