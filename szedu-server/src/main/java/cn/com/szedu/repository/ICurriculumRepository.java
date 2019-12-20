package cn.com.szedu.repository;

import cn.com.szedu.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICurriculumRepository extends JpaRepository<Curriculum,Integer> {
    Curriculum findByid(Integer id);
}
