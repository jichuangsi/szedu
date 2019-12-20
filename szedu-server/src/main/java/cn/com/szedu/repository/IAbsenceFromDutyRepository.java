package cn.com.szedu.repository;

import cn.com.szedu.entity.AbsenceFromDuty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAbsenceFromDutyRepository extends JpaRepository<AbsenceFromDuty,Integer> {
    List<AbsenceFromDuty> findByCourseId(String courseId);
}
