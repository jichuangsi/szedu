package cn.com.szedu.service.impl;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.Exam;
import cn.com.szedu.entity.IntegralRecord;
import cn.com.szedu.entity.IntermediateTable.ResourceTeacherInfoRelation;
import cn.com.szedu.entity.IntermediateTable.TeacherCourseRelation;
import cn.com.szedu.entity.TeacherInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.ITeacherInfoRepository;
import cn.com.szedu.repository.IntegralRecordRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IResourceTeacherInfoRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ITeacherCouseRelationRepository;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.TeacherInfoService;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @Resource
    private IOpLogRepository opLogRepository;
   @Resource
   private IntegralRecordRepository integralRecordRepository;

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
            //修改老师积分
            TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getId());
            teacherInfo.setIntegral(teacherInfo.getIntegral()+1);//老师等级积分
            teacherInfoRepository.save(teacherInfo);

            IntegralRecord integralRecord=new IntegralRecord(UUID.randomUUID().toString().replaceAll("-", ""),
                    "签到","老师登录签到",userInfo.getId(),userInfo.getName(),
                    1,"今日登录已签到，获得 1 积分",new Date().getTime());
            integralRecordRepository.save(integralRecord);

            return model1;
        }catch (UnsupportedEncodingException e){
            throw new UserServiceException(e.getMessage());
        }

    }

    /**
     * 查看系统消息
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     * @throws UserServiceException
     */
    @Override
    public Page<IntegralRecord> getTeacherMessage(UserInfoForToken userInfo, int pageNum, int pageSize)throws UserServiceException {
            pageNum=pageNum-1;
            Pageable pageable=new PageRequest(pageNum,pageSize);
            Page<IntegralRecord> records=integralRecordRepository.findAll((Root<IntegralRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
                List<Predicate> predicateList = new ArrayList<>();
               predicateList.add(criteriaBuilder.equal(root.get("operatorId"),userInfo.getUserId()));
                /*if(!StringUtils.isEmpty(userInfo.getUserName())){
                    predicateList.add(criteriaBuilder.equal(root.get("operatorName"),userInfo.getUserName()));
                }*/
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            },pageable);
            return records;
        }

    }

