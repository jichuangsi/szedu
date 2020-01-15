package cn.com.szedu.repository;

import cn.com.szedu.entity.CurriculumResource;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface ICurriculumResourceRepository extends JpaRepository<CurriculumResource,Integer> {
    List<CurriculumResource> findByTeacherid(String teacherId);
    List<CurriculumResource> findByCurriculumId(Integer curriculumId);
    List<CurriculumResource> findByidIn(List<Integer> resourceId);
    List<CurriculumResource> findByChapterIdAndCurriculumId(Integer chapterId,Integer curriculmId);
    void deleteByCurriculumId(Integer id);
    CurriculumResource findByid(Integer id);
}
