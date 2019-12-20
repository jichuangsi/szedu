package cn.com.szedu.service.impl;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.IntermediateTable.ResourceTeacherInfoRelation;
import cn.com.szedu.entity.IntermediateTable.TeacherCourseRelation;
import cn.com.szedu.entity.TeacherInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.repository.ITeacherInfoRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IResourceTeacherInfoRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ITeacherCouseRelationRepository;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.TeacherInfoService;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class TeacherInfoServiceImpl implements TeacherInfoService {

    @Resource
    private ITeacherInfoRepository teacherInfoRepository;
    @Resource
    private BackTokenService backTokenService;
    @Resource
    private ITeacherCouseRelationRepository teacherCouseRelationRepository;
    @Resource
    private IResourceTeacherInfoRelationRepository resourceTeacherInfoRelationRepository;


    @Override
    /**
     * 老师登录
     */
    public TeacherModel loginTeacher(TeacherModel model) throws UserServiceException {
        TeacherModel model1=null;
        if (StringUtils.isEmpty(model.getAcount()) || StringUtils.isEmpty(model.getPassword())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        TeacherInfo userInfo=teacherInfoRepository.findByAccountAndPwd(model.getAcount(),Md5Util.encodeByMd5(model.getPassword()));
        if (userInfo==null){
            throw new UserServiceException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        List<TeacherCourseRelation> tcr=teacherCouseRelationRepository.findAllByTecherId(userInfo.getId());//老师课堂
        List<ResourceTeacherInfoRelation> trr=resourceTeacherInfoRelationRepository.findByTeacherId(userInfo.getId());//老师资源
        String user=JSONObject.toJSONString(MappingEntity3ModelCoverter.CONVERTERFROMBACKUSERUSER(userInfo));
        try {
            String acc=backTokenService.createdToken(user);
            model1=MappingEntity3ModelCoverter.CONVERTERFROMBACKTEACHERINFO(userInfo);
            model1.setAccessToken(acc);
            model1.setCountCourse(tcr.size());
            model1.setCountResource(trr.size());
            return model1;
        }catch (UnsupportedEncodingException e){
            throw new UserServiceException(e.getMessage());
        }
    }


}
