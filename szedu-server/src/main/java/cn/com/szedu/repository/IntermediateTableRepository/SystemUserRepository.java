package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SystemUserRepository extends JpaRepository<SystemUser,String> {

    @Transactional
    void deleteByUidAndSid(String uid,String sid);
    List<SystemUser> findByUid(String uid);

    Integer countByAlreadyReadAndUid(String read,String uid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE SystemUser SET alreadyRead=?3 WHERE sid=?1 AND uid=?2")
  public void updatealreadyRead(String sid,String uid,String read);
}
