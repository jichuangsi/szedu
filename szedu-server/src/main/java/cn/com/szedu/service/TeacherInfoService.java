package cn.com.szedu.service;

import cn.com.szedu.entity.*;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.SandMessageModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.ClassModel;
import cn.com.szedu.model.teacher.MessageModel;
import cn.com.szedu.model.teacher.TeacherInfoModel;
import cn.com.szedu.model.teacher.TeacherModel;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface TeacherInfoService {


    public TeacherModel loginTeacher(TeacherModel model) throws UserServiceException;

    Integer signin(UserInfoForToken userInfo) throws UserServiceException;

    public Page<IntegralRecord> getTeacherIntegral(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException;

    //系统消息
    public List<Message> getTeacherMessage(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException;
//系统加互动
    Integer getTeacherMessageCount(UserInfoForToken userInfo) throws UserServiceException;
//系统
    Integer getSystemMessageCount(UserInfoForToken userInfo) throws UserServiceException;
//互动
    Integer gethuMessageCount(UserInfoForToken userInfo) throws UserServiceException;
    /* void getUserLogin(UserInfoForToken userInfo) throws UserServiceException;*/
    void getUserLogin(String userId);

    boolean updateTeacherMessage(UserInfoForToken userInfo,Integer id) throws UserServiceException;

    Page<IntegralRule> integralRule(int pageNum, int pageSize) throws UserServiceException;

    @Transactional
    Integer sendMessageByTeacher(UserInfoForToken userInfo);

    List<Message> getTeacherInteractionMessage(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException;

    boolean addInteractionMessage(UserInfoForToken userInfo, SandMessageModel model) throws UserServiceException;

    //修改积分规则
    void updateintegralRule(UserInfoForToken userInfo, IntegralRule integralRule);

    //查看所有老师
    List<TeacherModel> getAllTeacher(UserInfoForToken userInfo) throws UserServiceException;

    //查看班级
    List<ClassModel> getAllStudent(UserInfoForToken userInfo) throws UserServiceException;

    public void addMessage(UserInfoForToken userInfo, MessageModel message);

    public void addintegral(UserInfoForToken userInfo, IntegralRecord integralRecord);

    void deleteMessage(Integer id);//删除系统消息

    void deleteInteractionMessage(UserInfoForToken userInfo,Integer id);//删除互动消息

//查看互动消息，最新得一条
    List<Message> getMessageBysenderid(UserInfoForToken userInfo,String sendId, int pageNum, int pageSize) throws UserServiceException;
    //老师个人中心
    TeacherInfoModel teacherinfo(UserInfoForToken userInfo,String teacherId)throws UserServiceException;

    //编辑老师信息
    boolean updateteacherinfo(UserInfoForToken userInfo, TeacherInfo teacherInfo)throws UserServiceException;

    Boolean teacherinfoPic(MultipartFile file,UserInfoForToken userInfo,String teacherId) throws IOException;

    public List<ClassModel> getAllClass(UserInfoForToken userInfo) throws UserServiceException;
    public List<StudentModel> getStudentByClassId(UserInfoForToken userInfo, String classId) throws UserServiceException;

    List<TeacherModel> getStudentTeacher(UserInfoForToken userInfo)throws UserServiceException;


  boolean teacherMessage(UserInfoForToken userInfo,MessageFeedback feedback)throws UserServiceException;
    Integer countSystemMessage(UserInfoForToken userInfo,String schoolid)throws UserServiceException;
}
