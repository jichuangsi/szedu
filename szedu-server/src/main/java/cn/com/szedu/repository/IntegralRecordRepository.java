package cn.com.szedu.repository;

import cn.com.szedu.entity.IntegralRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IntegralRecordRepository extends JpaRepository<IntegralRecord,String>,PagingAndSortingRepository<IntegralRecord,String>,JpaSpecificationExecutor<IntegralRecord> {

    Integer countByOperatorIdAndFunctionAndCreateTimeGreaterThanAndCreateTimeLessThan(String name,String id,long startTime,long endTime);
    List<IntegralRecord> findByOperatorId(String operatorId);

    @javax.transaction.Transactional
    @Modifying
    @Query(value = "UPDATE CourseWare SET operatorName=?2 WHERE operatorId=?1")
    void updateOperatorName(String operatorId,String operatorName);

}
