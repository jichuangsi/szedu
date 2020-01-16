package cn.com.szedu.service;

import cn.com.szedu.entity.IntegralRule;
import cn.com.szedu.entity.Message;
import cn.com.szedu.entity.StudentInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.student.StudentClassInfoModel;
import cn.com.szedu.model.student.StudentInfoModel;
import cn.com.szedu.model.student.StudentIntegralModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.MessageModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentInfoService {


    public StudentInfoModel loginStudent(StudentInfoModel model)throws UserServiceException;

    Integer signin(UserInfoForToken userInfo) throws UserServiceException;

    Boolean LeavingMessage(UserInfoForToken userInfo,MessageModel message)throws UserServiceException;

    List<Message> getLeavingMessage(UserInfoForToken userInfo)throws UserServiceException;

  StudentInfo studentInfo(UserInfoForToken userInfo,String studentId)throws UserServiceException;

    StudentClassInfoModel getstudentInfo(UserInfoForToken userInfo, String studentId)throws UserServiceException;

    Boolean  updateStudent(UserInfoForToken userInfo,StudentModel model)throws UserServiceException;

    Page<IntegralRule> integralRule(UserInfoForToken userInfo,int pageNum, int pageSize) throws UserServiceException ;

    List<StudentIntegralModel> integralRanking(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException ;

    List<Message>  getStudentMessage(UserInfoForToken userInfo, int pageNum, int pageSize)throws UserServiceException;
    Integer countSystemMessage(UserInfoForToken userInfo,String schoolid)throws UserServiceException;
}
