package cn.com.szedu.util;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.Status;
import cn.com.szedu.entity.BackUser;
import cn.com.szedu.entity.StaticPage;
import cn.com.szedu.entity.UserInfo;
import cn.com.szedu.model.StaticPageModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.TeacherModel;
import cn.com.szedu.model.UserInfoForToken;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MappingEntity2ModelCoverter {
    public final static UserInfoForToken CONVERTERFROMBACKUSERINFO(BackUser userInfo) {
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getUserName());
        userInfoForToken.setRoleId(userInfo.getRoleId());
        return userInfoForToken;
    }

    public final static UserInfoForToken CONVERTERFROMBACKUSERUSER(UserInfo userInfo) {
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getName());
        userInfoForToken.setRoleId(userInfo.getRole());
        return userInfoForToken;
    }

    public final static List<StaticPageModel> CONVERTERFROMStaticPage(List<StaticPage> staticPages) {
        List<StaticPageModel> modelList = new ArrayList<>();
        staticPages.forEach(model -> {
            StaticPageModel model1 = new StaticPageModel();
            model1.setPageId(model.getId());
            model1.setPageName(model.getPageName());
            model1.setPageUrl(model.getPageUrl());
            model1.setParentNode(model.getParentNode());
            modelList.add(model1);
        });
        return modelList;
    }

    public static UserInfo CONVERTEERFROMTEACHERMODEL(TeacherModel model) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(model.getId());
        userInfo.setAccount(model.getAccount());
        userInfo.setName(model.getName());
        if (!StringUtils.isEmpty(model.getPwd())) {
            userInfo.setPwd(Md5Util.encodeByMd5(model.getPwd()));
        }
        userInfo.setSex(model.getSex());
        userInfo.setStatus(Status.ACTIVATE.getName());
        userInfo.setSubject(model.getSubject());
        userInfo.setSchoolName(model.getSchoolName());
        userInfo.setRole("Teacher");
        userInfo.setIntegral(0);
        return userInfo;
    }

    public static UserInfo CONVERTEERFROMSTUDENTMODEL(StudentModel model) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(model.getId());
        userInfo.setAccount(model.getAccount());
        userInfo.setName(model.getName());
        if (!StringUtils.isEmpty(model.getPwd())) {
            userInfo.setPwd(Md5Util.encodeByMd5(model.getPwd()));
        }
        userInfo.setSex(model.getSex());
        userInfo.setStatus(Status.ACTIVATE.getName());
        userInfo.setPhone(model.getPhone());
        userInfo.setSchoolName(model.getSchoolName());
        userInfo.setRole("Student");
        return userInfo;
    }

    public final static TeacherModel CONVERTERFROMUSERINFO(UserInfo userInfo){
        TeacherModel model = new TeacherModel();
        model.setAccount(userInfo.getAccount());
        model.setId(userInfo.getId());
        model.setName(userInfo.getName());
        model.setSex(userInfo.getSex());
        model.setStatus(userInfo.getStatus());
        model.setSchoolName(userInfo.getSchoolName());
        model.setSubject(userInfo.getSubject());
        model.setIntegral(userInfo.getIntegral());
        return model;
    }

    public final static StudentModel CONVERTERFROMUSERINFOTOSTUDENT(UserInfo userInfo){
        StudentModel model = new StudentModel();
        model.setAccount(userInfo.getAccount());
        model.setId(userInfo.getId());
        model.setName(userInfo.getName());
        model.setSex(userInfo.getSex());
        model.setStatus(userInfo.getStatus());
        model.setSchoolName(userInfo.getSchoolName());
        model.setPhone(userInfo.getPhone());
        return model;
    }

}
