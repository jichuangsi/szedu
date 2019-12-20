package cn.com.szedu.repository;

import cn.com.szedu.entity.SelfQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISelfQuestionsRepository extends JpaRepository<SelfQuestions,Integer> {
    void deleteByid(Integer id);
    SelfQuestions findByid(Integer id);
}
