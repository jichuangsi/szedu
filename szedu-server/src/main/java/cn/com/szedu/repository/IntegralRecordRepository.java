package cn.com.szedu.repository;

import cn.com.szedu.entity.IntegralRecord;
import cn.com.szedu.entity.OpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IntegralRecordRepository extends JpaRepository<IntegralRecord,Integer>,PagingAndSortingRepository<IntegralRecord,Integer>,JpaSpecificationExecutor<IntegralRecord> {

    Integer countByOperatorIdAndFunctionAndCreateTimeGreaterThanAndCreateTimeLessThan(String name,String id,long startTime,long endTime);
}
