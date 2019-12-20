package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.SchoolUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISchoolUserRelationRepository extends JpaRepository<SchoolUserRelation,Integer> {

}
