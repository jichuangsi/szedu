package cn.com.szedu.repository;

import cn.com.szedu.entity.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITeacherInfoRepository extends JpaRepository<TeacherInfo,Integer> {


    TeacherInfo findExsitById(String id);
    TeacherInfo findByAccountAndPwd(String account, String pwd);
    Integer countByAccount(String account);

    List<TeacherInfo> findByNameLike(String name);
    int countByNameLike(String name);

    List<TeacherInfo> findByIdNot(String id);
}
