package cn.com.szedu.service;

import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.Student.StudentInfoModel;
import cn.com.szedu.model.UserInfoForToken;

public interface StudentInfoService {


    public StudentInfoModel loginStudent(StudentInfoModel model)throws UserServiceException;
    Integer signin(UserInfoForToken userInfo) throws UserServiceException;
}
