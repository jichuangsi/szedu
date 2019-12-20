package cn.com.szedu.repository;

import cn.com.szedu.entity.ClassInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface IClassInfoRepository extends JpaRepository<ClassInfo,Integer> {

    List<ClassInfo> getClassInfoByIdIn(Sort sort,List<String> classId);

    ClassInfo findExistById(String id);

    Integer countBySpecialityAndRuTimeAndEducationalSystemAndClassName(String specialtity,String ruTime,String ed,String className);
    List<ClassInfo> findBySchoolIdAndStatus(String schoolId,String status);

    /*@Query(value = "SELECT c.*,t.* FROM class_info c INNER JOIN techer_class_relation tcr \n" +
            "ON c.id=tcr.class_id INNER JOIN teacher_info t ON tcr.techer_id=t.id\n" +
            "WHERE t.id=? ORDER BY c.create_time DESC\n")
   List<ClassInfo> getClassInfo();*/
}
