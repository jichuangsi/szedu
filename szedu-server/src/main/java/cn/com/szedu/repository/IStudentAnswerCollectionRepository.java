package cn.com.szedu.repository;

import cn.com.szedu.entity.StudentAnswerCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentAnswerCollectionRepository extends JpaRepository<StudentAnswerCollection,Integer> {
}
