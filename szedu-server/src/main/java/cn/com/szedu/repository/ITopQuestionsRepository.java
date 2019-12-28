package cn.com.szedu.repository;

import cn.com.szedu.entity.TopQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITopQuestionsRepository extends JpaRepository<TopQuestions,Integer> {
    void deleteByid(Integer id);
    TopQuestions findByid(Integer id);
    List<TopQuestions> findAllBySubjectIdOrderByCreateTimeDesc(String subject);
    List<TopQuestions> findAllBySubjectIdIn(List<String> subs);
    List<TopQuestions> findAllBySubjectIdInAndType(List<String> subs,String type);
}
