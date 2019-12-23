package cn.com.szedu.service;

import cn.com.szedu.entity.IntegralRecord;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TeacherModel;
import org.springframework.data.domain.Page;;

public interface TeacherInfoService {



    public TeacherModel loginTeacher(TeacherModel model) throws UserServiceException ;

    public Page<IntegralRecord> getTeacherMessage(UserInfoForToken userInfo, int pageNum, int pageSize)throws UserServiceException;

}
