package cn.com.szedu.repository;

import cn.com.szedu.entity.CommonProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
@Repository
public interface ICommonProblemRepository extends JpaRepository<CommonProblem,Integer> {

    CommonProblem findById(String id);

    void deleteById(String id);
    List<CommonProblem> findBySchoolId(String schoolId);
    List<CommonProblem> findByDid(String id);
}
