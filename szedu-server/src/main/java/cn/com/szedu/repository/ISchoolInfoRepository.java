package cn.com.szedu.repository;

import cn.com.szedu.entity.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ISchoolInfoRepository extends JpaRepository<SchoolInfo,String> {

    SchoolInfo findFirstById(String id);
    SchoolInfo findBySchoolName(String name);

    @Transactional
    @Modifying
    @Query(value = "UPDATE SchoolInfo SET status=?2 WHERE id=?1")
    void updateIsStatus(String id,String status);
}
