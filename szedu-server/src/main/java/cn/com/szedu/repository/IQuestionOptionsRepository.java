package cn.com.szedu.repository;

import cn.com.szedu.entity.QuestionOptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IQuestionOptionsRepository extends JpaRepository<QuestionOptions,Integer> {
    void deleteByQuestionId(Integer questionId);
    QuestionOptions findByid(Integer questionId);
}
