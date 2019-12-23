package cn.com.szedu.repository;

import cn.com.szedu.entity.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITeacherInfoRepository extends JpaRepository<TeacherInfo,Integer> {


    TeacherInfo findExsitById(String id);
    TeacherInfo findByAccountAndPwd(String account, String pwd);
}
