package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ResourceClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IResourceClassRelationRepository extends JpaRepository<ResourceClassRelation,Integer> {
    List<ResourceClassRelation> findByClassIdInOrderByCreateTimeDesc(List<String> classId);
}
