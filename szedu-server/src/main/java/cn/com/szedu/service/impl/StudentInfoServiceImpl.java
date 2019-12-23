package cn.com.szedu.service.impl;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.IntermediateTable.ResourceTeacherInfoRelation;
import cn.com.szedu.entity.IntermediateTable.TeacherCourseRelation;
import cn.com.szedu.entity.StudentInfo;
import cn.com.szedu.entity.TeacherInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.Student.StudentInfoModel;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.repository.IStudentInfoRespository;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.StudentInfoService;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class StudentInfoServiceImpl implements StudentInfoService {

    @Resource
    private BackTokenService backTokenService;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Override
    public StudentInfoModel loginStudent(StudentInfoModel model) throws UserServiceException {

        StudentInfoModel model1=null;
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPhone())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        StudentInfo userInfo=studentInfoRespository.findByAccountAndPhone(model.getAccount(),model.getPhone());
        if (userInfo==null){
            throw new UserServiceException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }

        String user=JSONObject.toJSONString(MappingEntity3ModelCoverter.CONVERTERFROMBACKSTUDENTINFO(userInfo));
        try {
            String acc=backTokenService.createdToken(user);
            model1=MappingEntity3ModelCoverter.CONVERTERFROMBACKTEACHERINFO(userInfo);
            model1.setAccessToken(acc);
            return model1;
        }catch (UnsupportedEncodingException e){
            throw new UserServiceException(e.getMessage());
        }
    }
}
