package cn.com.szedu.repository;

import cn.com.szedu.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IUserInfoRepository extends JpaRepository<UserInfo,String> {
    int countByAccount(String account);
    List<UserInfo> findByRole(String role);
    List<UserInfo> findByRoleAndNameLike(String role,String name);
    int countByRole(String role);
    int countByRoleAndNameLike(String role,String name);
    UserInfo findFirstById(String id);
    UserInfo findByAccountAndPwd(String account, String pwd);
    @Transactional
    @Modifying
    @Query(value = "update UserInfo set integral=integral+?1 where id=?2")
    void updateIntegral(String integral,String teacherId);
}
