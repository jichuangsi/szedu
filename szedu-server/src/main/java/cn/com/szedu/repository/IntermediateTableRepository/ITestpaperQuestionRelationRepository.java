package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.TestpaperQuestionRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITestpaperQuestionRelationRepository extends JpaRepository<TestpaperQuestionRelation,Integer> {
    List<TestpaperQuestionRelation> findByTestPaper(Integer testPaper);
    void deleteByTestPaper(Integer teatPaperId);
}
