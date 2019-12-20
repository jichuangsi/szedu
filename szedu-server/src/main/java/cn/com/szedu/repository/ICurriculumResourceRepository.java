package cn.com.szedu.repository;

import cn.com.szedu.entity.CurriculumResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICurriculumResourceRepository extends JpaRepository<CurriculumResource,Integer> {
    List<CurriculumResource> findByTeacherid(String teacherId);
    List<CurriculumResource> findByCurriculumId(Integer curriculumId);
    void deleteByCurriculumId(Integer id);
}
