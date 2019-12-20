package cn.com.szedu.service;

import cn.com.szedu.model.student.SchoolInfoModel;
import cn.com.szedu.model.UserInfoForToken;

public interface IBackSchoolService {
    void addSchool(UserInfoForToken userInfo,SchoolInfoModel model);
    void saveTeacherBySchool(String schoolId,String teacherId);
}
