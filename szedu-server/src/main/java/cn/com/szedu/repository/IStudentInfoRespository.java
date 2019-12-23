package cn.com.szedu.repository;

import cn.com.szedu.entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentInfoRespository extends JpaRepository<StudentInfo,Integer> {


    StudentInfo findFirstByid(String id);

    //Integer findByStudentId(String studentId);
    Integer countByStudentId(String studentId);
    Integer countByNameAndAccount(String studentName,String acount);
    Integer countByAccount(String acount);
    Integer countByAccountAndPhone(String acount,String phone);
    StudentInfo findByAccountAndPhone(String acount,String phone);

}
