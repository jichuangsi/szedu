package cn.com.szedu.service;

import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.teacher.TeacherModel;

public interface TeacherInfoService {

    public TeacherModel loginTeacher(TeacherModel model) throws UserServiceException ;

}
