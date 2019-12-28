package cn.com.szedu.repository;

import cn.com.szedu.entity.TestPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITestPaperRepository extends JpaRepository<TestPaper,Integer> {
    List<TestPaper> findByTeacherIdOrderByCreateTimeDesc(String teacherId);
    TestPaper findByid(Integer id);
    void deleteByid(Integer id);
}
