package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TecherClassRelationRepository extends JpaRepository<ClassRelation,Integer> {

    public List<ClassRelation> findByTecherId(String techerId);

    Integer deleteByClassIdAndAndTecherId(String classId,String techerId);

    Integer countByClassIdAndTecherId(String classId,String techerId);
}
