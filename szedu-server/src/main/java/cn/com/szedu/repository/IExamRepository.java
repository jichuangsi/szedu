package cn.com.szedu.repository;

import cn.com.szedu.entity.Exam;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IExamRepository extends JpaRepository<Exam,String>,PagingAndSortingRepository<Exam,String>,JpaSpecificationExecutor<Exam> {
    void deleteById(String id);
    Exam findFirstByid(String id);
    boolean countByTestPaperId(String testPaperId);
}
