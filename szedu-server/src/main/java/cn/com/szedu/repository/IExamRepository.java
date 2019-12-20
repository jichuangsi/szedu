package cn.com.szedu.repository;

import cn.com.szedu.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IExamRepository extends JpaRepository<Exam,String>,PagingAndSortingRepository<Exam,String>,JpaSpecificationExecutor<Exam> {
    void deleteById(String id);
    Exam findFirstByid(String id);
}
