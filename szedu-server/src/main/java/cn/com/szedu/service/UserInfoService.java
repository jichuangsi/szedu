package cn.com.szedu.service;

import cn.com.szedu.entity.CommonProblem;
import cn.com.szedu.entity.Message;
import cn.com.szedu.entity.MessageFeedback;
import cn.com.szedu.entity.SchoolInfo;
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
    String saveExcelStudents(MultipartFile file, UserInfoForToken userInfo,String schoolId) throws UserServiceException;
    PageInfo<TeacherModel> getTeachersByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException;
    PageInfo<StudentModel> getStudentsByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException;
    void updateStaffPwd(UserInfoForToken userInfo,String id,String newPwd,String pwd)throws UserServiceException;
    String loginBackUser(BackUserLoginModel model)throws UserServiceException;
    void insertSuperMan() throws UserServiceException;
    void updateUserIntegral(String userId,String integral)throws UserServiceException;
    List<TeacherModel> getAllTeacher();


    List<Message> getLeavingMessage(UserInfoForToken userInfo)throws UserServiceException;
    List<SchoolInfo> getAllSchool( UserInfoForToken userInfo)throws UserServiceException;


    List<MessageFeedback> getMessageFeedback(UserInfoForToken userInfo)throws UserServiceException;

  boolean teacherStatus(UserInfoForToken userInfo,String teacherId,String status)throws UserServiceException;
    boolean teacherdelete(UserInfoForToken userInfo,String teacherId)throws UserServiceException;
    boolean teacherPwd(UserInfoForToken userInfo,String teacherId,String pwd)throws UserServiceException;
    boolean addProblem(UserInfoForToken userInfo,CommonProblem problem)throws UserServiceException;
    boolean updateProblem(UserInfoForToken userInfo,CommonProblem problem)throws UserServiceException;
    List<CommonProblem> allProblem(UserInfoForToken userInfo)throws UserServiceException;
    List<CommonProblem> schoolProblem(UserInfoForToken userInfo,String schoolid)throws UserServiceException;
    List<CommonProblem> aroblemAnswer(UserInfoForToken userInfo,String id)throws UserServiceException;
}
