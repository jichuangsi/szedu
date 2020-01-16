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
public interface IClassInfoRepository extends JpaRepository<ClassInfo,String> {

    List<ClassInfo> getClassInfoByIdIn(Sort sort,List<String> classId);

    ClassInfo findExistById(String id);

    Integer countBySpecialityAndRuTimeAndEducationalSystemAndClassName(String specialtity,String ruTime,String ed,String className);
    List<ClassInfo> findBySchoolIdAndStatus(String schoolId,String status);

    List<ClassInfo> findByIdIn(List<String> classId);


}
