package cn.com.szedu.repository;

import cn.com.szedu.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserInfoRepository extends JpaRepository<UserInfo,String> {
    int countByAccount(String account);
    List<UserInfo> findByRole(String role);
    List<UserInfo> findByRoleAndNameLike(String role,String name);
    int countByRole(String role);
    int countByRoleAndNameLike(String role,String name);
    UserInfo findFirstById(String id);
    UserInfo findByAccountAndPwd(String account, String pwd);
}
