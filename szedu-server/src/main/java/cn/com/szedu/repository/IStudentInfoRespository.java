package cn.com.szedu.repository;

import cn.com.szedu.entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStudentInfoRespository extends JpaRepository<StudentInfo,Integer> {


    StudentInfo findFirstByid(String id);

    //Integer findByStudentId(String studentId);
    Integer countByStudentId(String studentId);
    Integer countByNameAndAccount(String studentName,String acount);
    Integer countByAccount(String acount);
    Integer countByAccountAndPhone(String acount,String phone);
    Integer countByName(String acount);
    Integer countByPhone(String phone);
    StudentInfo findByAccountAndPhone(String acount,String phone);
    List<StudentInfo> findByNameLike(String name);
    int countByNameLike(String name);
}
