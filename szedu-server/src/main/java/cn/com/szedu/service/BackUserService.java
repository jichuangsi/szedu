package cn.com.szedu.service;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.BackUser;
import cn.com.szedu.entity.Course;
import cn.com.szedu.entity.SystemMessage;
import cn.com.szedu.entity.TeacherInfo;
import cn.com.szedu.exception.BackUserException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.BackUserLoginModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IBackUserRepository;
import cn.com.szedu.repository.SystemMessageRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BackUserService {

    @Resource
    private IBackUserRepository backUserRepository;
    @Resource
    private BackTokenService backTokenService;
    @Resource
    private SystemMessageRepository systemMessageRepository;


    public void registBackUser(BackUser backUser)throws BackUserException {
        if (StringUtils.isEmpty(backUser.getAccount()) || StringUtils.isEmpty(backUser.getPwd())
                || StringUtils.isEmpty(backUser.getUserName()) || StringUtils.isEmpty(backUser.getRoleId())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        if (backUserRepository.countByaccount(backUser.getAccount())>0){
            throw new BackUserException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        backUser.setStatus("A");
        backUser.setPwd(Md5Util.encodeByMd5(backUser.getPwd()));
        backUserRepository.save(backUser);
    }

    public String loginBackUser(BackUserLoginModel model)throws BackUserException{
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUser backUser=backUserRepository.findByAccountAndPwd(model.getAccount(),Md5Util.encodeByMd5(model.getPwd()));
        BackUser backUser2=backUserRepository.findByAccountAndPwdAndStatus(model.getAccount(),Md5Util.encodeByMd5(model.getPwd()),"A");
        if (backUser==null){
            throw new BackUserException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        String user=JSONObject.toJSONString(MappingEntity2ModelCoverter.CONVERTERFROMBACKUSERINFO(backUser));
        try {
            return backTokenService.createdToken(user);
        }catch (UnsupportedEncodingException e){
            throw new BackUserException(e.getMessage());
        }
    }

    public void insertSuperMan() throws BackUserException {
        if (backUserRepository.countByaccount("admin") > 0){
            throw new BackUserException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        BackUser userInfo = new BackUser();
        userInfo.setStatus("A");
        userInfo.setAccount("admin");
        userInfo.setUserName("admin");
        userInfo.setRoleId("123456");
        userInfo.setRoleName("M");
        userInfo.setDeptId("123456");
        userInfo.setDeptName("S");
        userInfo.setPwd(Md5Util.encodeByMd5("admin"));
        userInfo.setCreatedTime(new Date().getTime());
        backUserRepository.save(userInfo);
    }

    public BackUser getBackUserById(UserInfoForToken userInfoForToken)throws BackUserException{
        BackUser user=backUserRepository.findByid(userInfoForToken.getUserId());
        if(user==null){
            throw new BackUserException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        return user;
    }

    public boolean sendSystemAdmin(UserInfoForToken userInfo,SystemMessage message) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(message) ){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
       message.setSendId(userInfo.getUserId());
        message.setSendName(userInfo.getUserName());
        message.setExamine(1);
        message.setAlreadyRead("false");
        message.setId(UUID.randomUUID().toString().replaceAll("-",""));
        systemMessageRepository.save(message);
        return true;
    }

    public Page<SystemMessage> getSystemSchoolId(UserInfoForToken userInfo,String schoolId,int pageNum,int pageSize) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(schoolId) ){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "time"));
        Pageable pageable=new PageRequest(pageNum-1,pageSize,sort);
        Page<SystemMessage> systemMessages=systemMessageRepository.findAll((Root<SystemMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();

            predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),schoolId));
            predicateList.add(criteriaBuilder.equal(root.get("check").as(Integer.class),2));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return systemMessages;
    }

    public Page<SystemMessage> getSystemAdmin(UserInfoForToken userInfo,String schoolId,String adminId,int pageNum,int pageSize) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(schoolId) ||StringUtils.isEmpty(adminId)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "time"));
        Pageable pageable=new PageRequest(pageNum-1,pageSize,sort);
        Page<SystemMessage> systemMessages=systemMessageRepository.findAll((Root<SystemMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
           /* Path<Object> path = root.get("id");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (String s:courseId) {
                in.value(s);
            }*/
           /* predicateList.add(criteriaBuilder.and(criteriaBuilder.and(in)));
            if(!StringUtils.isEmpty(mmodel.getSubjectId()) && mmodel.getSubjectId()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("subjectId").as(Integer.class),mmodel.getSubjectId()));
            }
            if(!StringUtils.isEmpty(mmodel.getLessionType())){
                predicateList.add(criteriaBuilder.equal(root.get("lessonTypeName").as(String.class),mmodel.getLessionType()));
            }
           *//* if(mmodel.getTime()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("startTime").as(long.class),mmodel.getTime()));
            }*//*
            if (!StringUtils.isEmpty(mmodel.getTime())) {
                predicateList.add(criteriaBuilder.between(root.get("startTime"),mmodel.getTime(), System.currentTimeMillis()));
            }
            predicateList.add(criteriaBuilder.notEqual(root.get("status"),"N"));*/
            predicateList.add(criteriaBuilder.equal(root.get("check").as(Integer.class),2));
            predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),schoolId));
            predicateList.add(criteriaBuilder.equal(root.get("sendId").as(String.class),adminId));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return systemMessages;
    }
    //未审核
    public Page<SystemMessage> checkMessage(UserInfoForToken userInfo,String schoolId,int pageNum,int pageSize) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(schoolId)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "time"));
        Pageable pageable=new PageRequest(pageNum-1,pageSize,sort);
        Page<SystemMessage> systemMessages=systemMessageRepository.findAll((Root<SystemMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("check").as(Integer.class),1));
            predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),schoolId));
            /*predicateList.add(criteriaBuilder.equal(root.get("sendId").as(String.class),adminId));*/
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return systemMessages;
    }

    public boolean updateCheckMessage(UserInfoForToken userInfo,String id,int check) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(id) ||StringUtils.isEmpty(check)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}

      systemMessageRepository.updateIsExamine(id,check);
        return true;
    }

    public boolean deletCheckMessage(UserInfoForToken userInfo,String id) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(id)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
          systemMessageRepository.deleteById(id);
        return true;
    }


    public boolean  updateSystemAlready(UserInfoForToken userInfo,String id)throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        systemMessageRepository.updatealreadyRead(id,"true");
        return true;
    }

}
