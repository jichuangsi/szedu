package cn.com.szedu.service;

import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserInfoService {
    void saveTeacher(UserInfoForToken userInfo,TeacherModel model) throws UserServiceException;
    void saveStudent(UserInfoForToken userInfo,StudentModel model) throws UserServiceException;
    void updateTeacher(UserInfoForToken userInfo,TeacherModel model) throws UserServiceException;
    void updateStudent(UserInfoForToken userInfo,StudentModel model) throws UserServiceException;
    //删除
    void deleteUserInfo(UserInfoForToken userInfo,String userId) throws UserServiceException;
    //批量
    String saveExcelTeachers(MultipartFile file, UserInfoForToken userInfo) throws UserServiceException;
    String saveExcelStudents(MultipartFile file, UserInfoForToken userInfo) throws UserServiceException;
    PageInfo<TeacherModel> getTeachersByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException;
    PageInfo<StudentModel> getStudentsByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException;
    void updateStaffPwd(UserInfoForToken userInfo,String id,String newPwd,String pwd)throws UserServiceException;
    String loginBackUser(BackUserLoginModel model)throws UserServiceException;
    void insertSuperMan() throws UserServiceException;
    void updateUserIntegral(String userId,String integral)throws UserServiceException;
    List<TeacherModel> getAllTeacher();
}
