package cn.com.szedu.service;

import cn.com.szedu.entity.IntegralRecord;
import cn.com.szedu.entity.IntegralRule;
import cn.com.szedu.entity.Message;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.SandMessageModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.MessageModel;
import cn.com.szedu.model.teacher.TeacherModel;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;;import javax.servlet.http.HttpSession;
import java.util.List;

public interface TeacherInfoService {


    public TeacherModel loginTeacher(TeacherModel model) throws UserServiceException;

    Integer signin(UserInfoForToken userInfo) throws UserServiceException;

    public Page<IntegralRecord> getTeacherIntegral(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException;

    //系统消息
    public Page<Message> getTeacherMessage(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException;

    Integer getTeacherMessageCount(UserInfoForToken userInfo) throws UserServiceException;

   /* void getUserLogin(UserInfoForToken userInfo) throws UserServiceException;*/
   void getUserLogin( String userId);

    void updateTeacherMessage(UserInfoForToken userInfo) throws UserServiceException;

    Page<IntegralRule> integralRule(int pageNum,int pageSize) throws UserServiceException;

    @Transactional
    Integer sendMessageByTeacher( UserInfoForToken userInfo);

    List<Message> getTeacherInteractionMessage(UserInfoForToken userInfo, int pageNum, int pageSize)throws UserServiceException;

    void addInteractionMessage(UserInfoForToken userInfo,SandMessageModel model)throws UserServiceException;
    //修改积分规则
    void updateintegralRule(UserInfoForToken userInfo,IntegralRule integralRule);
    //查看所有老师
    List<TeacherModel> getAllTeacher(UserInfoForToken userInfo)throws UserServiceException;
}
