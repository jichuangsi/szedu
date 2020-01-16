package cn.com.szedu.service;

import cn.com.szedu.entity.BackUser;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.student.SchoolInfoModel;
import cn.com.szedu.model.UserInfoForToken;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IBackSchoolService {
    void addSchool(UserInfoForToken userInfo,SchoolInfoModel model);
    void saveTeacherBySchool(String schoolId,String teacherId);
    boolean schoolPic(MultipartFile file, UserInfoForToken userInfo, String schoolId)throws IOException;

    boolean updateSchoolInfo(UserInfoForToken userInfo,SchoolInfoModel model)throws UserServiceException;
    void schoolInfoStatus(UserInfoForToken userInfo, String schoolId,String status)throws UserServiceException;

    boolean saveAdmin(BackUser user,UserInfoForToken userInfo)throws UserServiceException;
    boolean updateAdmin(BackUser user,UserInfoForToken userInfo)throws UserServiceException;
    boolean deleteAdmin(UserInfoForToken userInfo,String id)throws UserServiceException;
    boolean frozenAdmin(UserInfoForToken userInfo,String id,String status)throws UserServiceException;
    boolean adminPwd(UserInfoForToken userInfo,String id,String pwd)throws UserServiceException;
}
