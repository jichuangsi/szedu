package cn.com.szedu.repository;

import cn.com.szedu.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICurriculumRepository extends JpaRepository<Curriculum,Integer> {
    Curriculum findByid(Integer id);
    @Query(value = "SELECT * FROM curriculum WHERE id IN ?1 LIMIT ?2,?3",nativeQuery = true)
    List<Curriculum> findByidIn(List<Integer> curriculumId,int num,int size);
}
