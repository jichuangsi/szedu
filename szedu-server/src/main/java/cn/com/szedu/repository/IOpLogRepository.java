package cn.com.szedu.repository;

import cn.com.szedu.entity.OpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IOpLogRepository extends JpaRepository<OpLog,Integer>,PagingAndSortingRepository<OpLog,Integer>,JpaSpecificationExecutor<OpLog> {

}
