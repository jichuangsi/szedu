package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.PurchasedResources;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPurchasedResourcesRepository extends JpaRepository<PurchasedResources,Integer> {
    List<PurchasedResources> findByTeacherId(String teacherId);
    List<PurchasedResources> findByTeacherIdOrderByCreateTimeDesc(String teacherId);
}
