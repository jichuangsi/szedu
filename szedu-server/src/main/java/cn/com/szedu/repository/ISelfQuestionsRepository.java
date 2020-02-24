package cn.com.szedu.repository;

import cn.com.szedu.entity.SelfQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ISelfQuestionsRepository extends JpaRepository<SelfQuestions,Integer>,PagingAndSortingRepository<SelfQuestions,Integer>,JpaSpecificationExecutor<SelfQuestions> {
    void deleteByid(Integer id);
    SelfQuestions findByid(Integer id);
    List<SelfQuestions> findByTeacherIdAndTypeOrderByCreateTime(String teacherId,String type);
    //根据科目查询题目
    List<SelfQuestions> findByTeacherIdAndSubjectIdIn(String teacherId,List<String> subIds);
    List<SelfQuestions> findByTeacherIdAndTypeAndSubjectIdIn(String teacherId,String type,List<String> subIds);
    List<SelfQuestions> findByTeacherIdAndTypeAndChapterIn(String teacherId,String type,List<String> chapter);
    /*List<SelfQuestions> findAllByTeacherId*/
    List<SelfQuestions> findByidIn(List<Integer> questionIds);
    int countBySubjectIdAndTeacherIdAndType(String subjectId,String teacherId,String type);
    List<SelfQuestions> findAllByTypeidAndIdIn(String typeId,List<Integer> id);
}
