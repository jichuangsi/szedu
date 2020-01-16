package cn.com.szedu.repository;

import cn.com.szedu.entity.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ITeacherInfoRepository extends JpaRepository<TeacherInfo,Integer> {
    TeacherInfo findExsitById(String id);
    TeacherInfo findByAccountAndPwd(String account, String pwd);
    Integer countByAccount(String account);
    List<TeacherInfo> findByNameLike(String name);
    int countByNameLike(String name);
    List<TeacherInfo> findByIdNot(String id);
    TeacherInfo findByid(String id);
    List<TeacherInfo> findBySchoolId(String schoolId);

    List<TeacherInfo> findByIdNotAndSchoolId(String id,String schoolId);
    List<TeacherInfo> findByIdIn(List<String> teacherId);
    @Transactional
    void deleteById(String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE TeacherInfo SET integral=?2 WHERE id=?1")
    void updateIntegral(String teacherId,Integer integral);
}
