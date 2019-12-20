package cn.com.szedu.repository;

import cn.com.szedu.entity.AttendanceInClass;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAttendanceRepository extends JpaRepository<AttendanceInClass,Integer> {

    List<AttendanceInClass> findAllByStudentNameLike(String name);

    List<AttendanceInClass> findByCourseId(String courseId);
}
