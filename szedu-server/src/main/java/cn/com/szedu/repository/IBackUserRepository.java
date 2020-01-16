package cn.com.szedu.repository;

import cn.com.szedu.entity.BackUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface IBackUserRepository extends JpaRepository<BackUser,String> {
    int countByaccount(String account);
    BackUser findByAccountAndPwd(String account, String pwd);

    BackUser findByAccountAndPwdAndStatus(String account, String pwd,String status);
    BackUser findByid(String id);
    int countByAccount(String account);
    @Transactional
    void deleteById(String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE BackUser SET status=?2 WHERE id=?1")
    void updateIsStatus(String id,String status);

    @Transactional
    @Modifying
    @Query(value = "UPDATE BackUser SET pwd=?2 WHERE id=?1")
     void updatePwd(String id,String pwd);
}
