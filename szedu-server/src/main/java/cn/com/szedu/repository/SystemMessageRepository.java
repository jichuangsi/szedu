package cn.com.szedu.repository;

import cn.com.szedu.entity.SystemMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SystemMessageRepository extends JpaRepository<SystemMessage, Integer>, JpaSpecificationExecutor<SystemMessage> {

    List<SystemMessage> findBySchoolId(String schoolid);

    @Transactional
    void deleteById(String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE SystemMessage SET examine=?2 WHERE id=?1")
    void updateIsExamine(String id, int check);

    SystemMessage findById(String id);
   /* @Transactional
    @Modifying
    @Query(value = "UPDATE SystemMessage SET alreadyRead=?2 WHERE id=?1")
    void updatealreadyRead(String id,String read);
*/
    //Integer countByAlreadyReadAndSchoolIdAndExamine(String read,String schoolid,String check);

    Integer countBySchoolIdAndExamine(String schoolId, int examine);
}
