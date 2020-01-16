package cn.com.szedu.repository;

import cn.com.szedu.entity.OnlineProduction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOnlineProductionRepository extends JpaRepository<OnlineProduction,Integer> {

    OnlineProduction findById(String id);

}
