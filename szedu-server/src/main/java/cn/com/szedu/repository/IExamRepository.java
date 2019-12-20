package cn.com.szedu.repository;

import cn.com.szedu.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExamRepository extends JpaRepository<Exam,String> {
    void deleteById(String id);
    Exam findFirstByid(String id);
}
