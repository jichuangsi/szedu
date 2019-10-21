package cn.com.szedu.repository;

import cn.com.szedu.entity.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IUserPositionRepository extends JpaRepository<UserPosition,String>,JpaSpecificationExecutor<UserPosition> {
    void deleteByuserid(String uid);
    List<UserPosition> findByStatus(String status);
    void deleteByuseridAndAndClassid(String userId, String classId);
    List<UserPosition> findByUserid(String userId);
    List<UserPosition> findBySubjectidAndGradeid(String subjectId, String gradeId);
    void deleteByGradeidIn(List<String> gradeIds);
    //List<UserPosition> findByGradeidIn(List<String> gradeId);
}
