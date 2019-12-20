package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ResourceTeacherInfoRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IResourceTeacherInfoRelationRepository extends JpaRepository<ResourceTeacherInfoRelation,Integer> {

    List<ResourceTeacherInfoRelation> findByTeacherId(String teacherId);
}
