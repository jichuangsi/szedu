package cn.com.szedu.repository;

import cn.com.szedu.entity.ResourcesRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IResourceRuleRepository extends JpaRepository<ResourcesRule,Integer> {
    ResourcesRule findFirstByid(Integer ruleId);
}
