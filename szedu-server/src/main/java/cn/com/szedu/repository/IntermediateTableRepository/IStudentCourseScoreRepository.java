package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.StudentCourseScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentCourseScoreRepository extends JpaRepository<StudentCourseScore,Integer> {
}
