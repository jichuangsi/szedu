package cn.com.szedu.repository;

import cn.com.szedu.entity.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IQuestionTypeRepository extends JpaRepository<QuestionType,Integer> {
}
