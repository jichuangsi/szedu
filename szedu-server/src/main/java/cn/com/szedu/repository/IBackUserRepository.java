package cn.com.szedu.repository;

import cn.com.szedu.entity.BackUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBackUserRepository extends JpaRepository<BackUser,String> {
    int countByaccount(String account);
    BackUser findByAccountAndPwd(String account, String pwd);
    BackUser findByid(String id);
}
