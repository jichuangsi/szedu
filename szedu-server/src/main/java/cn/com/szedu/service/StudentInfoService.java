package cn.com.szedu.service;

import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.Student.StudentInfoModel;

public interface StudentInfoService {


    public StudentInfoModel loginStudent(StudentInfoModel model)throws UserServiceException;
}
