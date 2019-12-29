package cn.com.szedu.repository;

import cn.com.szedu.entity.IntegralRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IIntegralRuleRepository extends JpaRepository<IntegralRule,Integer>,JpaSpecificationExecutor<IntegralRule> {

    List<IntegralRule> findByRole(String name);
    IntegralRule findByRoleAndType(String name,String type);

    IntegralRule findFirstById(Integer id);

}
