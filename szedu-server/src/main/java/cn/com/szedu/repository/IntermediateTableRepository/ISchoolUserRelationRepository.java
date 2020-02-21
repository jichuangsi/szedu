package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.SchoolUserRelation;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISchoolUserRelationRepository extends JpaRepository<SchoolUserRelation,Integer> {
    SchoolUserRelation findByUid(String uid);
}
