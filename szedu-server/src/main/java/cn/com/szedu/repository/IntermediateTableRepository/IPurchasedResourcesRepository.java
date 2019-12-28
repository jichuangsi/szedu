package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.PurchasedResources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPurchasedResourcesRepository extends JpaRepository<PurchasedResources,Integer> {
}
