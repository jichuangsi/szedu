package cn.com.szedu.service;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.MessageUserRelation;
import cn.com.szedu.entity.IntermediateTable.SystemUser;
import cn.com.szedu.exception.BackUserException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.BackUserLoginModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.IMessageUserRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.SystemUserRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Resource
    private ITeacherInfoRepository teacherInfoRepository;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Resource
    private IMessageUserRelationRepository messageUserRelationRepository;
    @Resource
    private SystemUserRepository systemUserRepository;

    private Logger logger=LoggerFactory.getLogger(getClass());


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
       /* message.setAlreadyRead("false");*/
        message.setTime(new Date().getTime());
        message.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        SystemMessage systemMessage=systemMessageRepository.save(message);
      SystemUser systemUser=new SystemUser();
        systemUser.setSid(systemMessage.getId());
        systemUser.setUid(userInfo.getUserId());
        systemUser.setAlreadyRead("false");
        systemUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        systemUserRepository.save(systemUser);
        /*  List<StudentInfo> studentInfo=studentInfoRespository.findBySchoolId(message.getSchoolId());
        if (studentInfo.size()>0){
            for (StudentInfo s:studentInfo) {
                systemUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                systemUser.setUid(s.getId());
                systemUserRepository.save(systemUser);
            }
        }
        List<TeacherInfo> teacherInfos=teacherInfoRepository.findBySchoolId(message.getSchoolId());
        if (studentInfo.size()>0){
            for (TeacherInfo t:teacherInfos) {
                systemUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                systemUser.setUid(t.getId());
                systemUserRepository.save(systemUser);
            }
        }*/
        return true;
    }

    public Page<SystemMessage> getSystemSchoolId(UserInfoForToken userInfo, String schoolId,int pageNum,int pageSize) throws UserServiceException {
        //String schoolId="";
        if (StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
        BackUser backUser=backUserRepository.findByid(userInfo.getUserId());
        StudentInfo studentInfo=studentInfoRespository.findFirstById(userInfo.getUserId());
       /* if (!StringUtils.isEmpty(teacherInfo)){
            schoolId=teacherInfo.getSchoolId();
        }
        BackUser backUser=backUserRepository.findByid(userInfo.getUserId());
        if (!StringUtils.isEmpty(backUser)){
            schoolId=backUser.getSchoolId();
        }*/
        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "time"));
        Pageable pageable=new PageRequest(pageNum-1,pageSize,sort);
        Page<SystemMessage> systemMessages=systemMessageRepository.findAll((Root<SystemMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
           /* if (!StringUtils.isEmpty(teacherInfo)){
                predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),teacherInfo.getSchoolId()));
            }else if (!StringUtils.isEmpty(backUser)){
                predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),backUser.getSchoolId()));
            }else if (!StringUtils.isEmpty(studentInfo)){
                predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),studentInfo.getSchoolId()));
            }*/
            predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),schoolId));
            predicateList.add(criteriaBuilder.equal(root.get("examine").as(Integer.class),2));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return systemMessages;
    }

   /* public Page<SystemMessage> getSystemAdmin(UserInfoForToken userInfo,String schoolId,String adminId,int pageNum,int pageSize) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(adminId)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
        BackUser backUser=backUserRepository.findByid(userInfo.getUserId());
        StudentInfo studentInfo=studentInfoRespository.findFirstById(userInfo.getUserId());

        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "time"));
        Pageable pageable=new PageRequest(pageNum-1,pageSize,sort);
        Page<SystemMessage> systemMessages=systemMessageRepository.findAll((Root<SystemMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
           *//* Path<Object> path = root.get("id");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (String s:courseId) {
                in.value(s);
            }*//*
           *//* predicateList.add(criteriaBuilder.and(criteriaBuilder.and(in)));
            if(!StringUtils.isEmpty(mmodel.getSubjectId()) && mmodel.getSubjectId()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("subjectId").as(Integer.class),mmodel.getSubjectId()));
            }
            if(!StringUtils.isEmpty(mmodel.getLessionType())){
                predicateList.add(criteriaBuilder.equal(root.get("lessonTypeName").as(String.class),mmodel.getLessionType()));
            }
           *//**//* if(mmodel.getTime()!=0){
                predicateList.add(criteriaBuilder.equal(root.get("startTime").as(long.class),mmodel.getTime()));
            }*//**//*
            if (!StringUtils.isEmpty(mmodel.getTime())) {
                predicateList.add(criteriaBuilder.between(root.get("startTime"),mmodel.getTime(), System.currentTimeMillis()));
            }
            predicateList.add(criteriaBuilder.notEqual(root.get("status"),"N"));*//*

            *//*if (!StringUtils.isEmpty(teacherInfo)){
                predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),teacherInfo.getSchoolId()));
            }else if (!StringUtils.isEmpty(backUser)){
                predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),backUser.getSchoolId()));
            }else if (!StringUtils.isEmpty(studentInfo)){
                predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),studentInfo.getSchoolId()));
            }*//*
            predicateList.add(criteriaBuilder.equal(root.get("examine").as(Integer.class),2));
            predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),schoolId));
            predicateList.add(criteriaBuilder.equal(root.get("sendId").as(String.class),adminId));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return systemMessages;
    }
    */

    public Page<SystemMessage> getSystemAdmin(UserInfoForToken userInfo,int pageNum,int pageSize) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
       // TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
        BackUser backUser=backUserRepository.findByid(userInfo.getUserId());
       // StudentInfo studentInfo=studentInfoRespository.findFirstById(userInfo.getUserId());
        List<String> uid=new ArrayList<String>();
        List<SystemUser> m=systemUserRepository.findByUid(userInfo.getUserId());
        for (SystemUser s:m){
            uid.add(s.getSid());
        }
        if (uid.size()<=0){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "time"));
        Pageable pageable=new PageRequest(pageNum-1,pageSize,sort);
        Page<SystemMessage> systemMessages=systemMessageRepository.findAll((Root<SystemMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            Path<Object> path = root.get("id");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (String s:uid) {
                in.value(s);
            }
            predicateList.add(criteriaBuilder.and(criteriaBuilder.and(in)));
            if (!backUser.getRoleId().equalsIgnoreCase("1")){
                if (!StringUtils.isEmpty(backUser)){
                    predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),backUser.getSchoolId()));
                }
            }
            predicateList.add(criteriaBuilder.equal(root.get("examine").as(Integer.class),2));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return systemMessages;
    }

    //未审核
    public Page<SystemMessage> checkMessage(UserInfoForToken userInfo,String schoolId,int pageNum,int pageSize) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(schoolId)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}


        BackUser backUser=backUserRepository.findByid(userInfo.getUserId());
        List<String> uid=new ArrayList<String>();
        List<SystemUser> m=systemUserRepository.findByUid(userInfo.getUserId());
        for (SystemUser s:m){
            uid.add(s.getSid());
        }
        if (uid.size()<=0){
            throw new UserServiceException(ResultCode.SELECT_NULL_MSG);
        }
        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "time"));
        Pageable pageable=new PageRequest(pageNum-1,pageSize,sort);
        Page<SystemMessage> systemMessages=systemMessageRepository.findAll((Root<SystemMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();

            Path<Object> path = root.get("id");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (String s:uid) {
                in.value(s);
            }
            if (!backUser.getRoleId().equalsIgnoreCase("1")){
            if (!StringUtils.isEmpty(backUser)){
                predicateList.add(criteriaBuilder.equal(root.get("schoolId").as(String.class),backUser.getSchoolId()));
            }
            }
            predicateList.add(criteriaBuilder.and(criteriaBuilder.and(in)));
            predicateList.add(criteriaBuilder.equal(root.get("examine").as(Integer.class),1));

            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return systemMessages;
    }

    public boolean updateCheckMessage(UserInfoForToken userInfo,String id,int check) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(id) ||StringUtils.isEmpty(check)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
       BackUser backUser=backUserRepository.findByid(userInfo.getUserId());
        systemMessageRepository.updateIsExamine(id,check);
        SystemMessage systemMessage=null;
     if (check==2){//审核通过
         systemMessage=systemMessageRepository.findById(id);
         if (systemMessage!=null){
             SystemUser systemUser=new SystemUser();
             systemUser.setSid(id);
             List<StudentInfo> studentInfo=studentInfoRespository.findBySchoolId(systemMessage.getSchoolId());
             /*if (StringUtils.isEmpty(backUser.getSchoolId())){
                 studentInfo=studentInfoRespository.findAll();
             }else {
                 studentInfo=studentInfoRespository.findBySchoolId(systemMessage.getSchoolId());
             }*/
             if (studentInfo.size()>0){
                 for (StudentInfo s:studentInfo) {
                     systemUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                     systemUser.setUid(s.getId());
                     systemUser.setAlreadyRead("false");
                     systemUserRepository.save(systemUser);
                 }
             }
             List<TeacherInfo> teacherInfos=teacherInfoRepository.findBySchoolId(systemMessage.getSchoolId());
            /* if (StringUtils.isEmpty(backUser.getSchoolId())){
                 teacherInfos=teacherInfoRepository.findAll();
             }else {
                 teacherInfos=teacherInfoRepository.findBySchoolId(backUser.getSchoolId());
             }*/
             if (studentInfo.size()>0){
                 for (TeacherInfo t:teacherInfos) {
                     systemUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                     systemUser.setUid(t.getId());
                     systemUser.setAlreadyRead("false");
                     systemUserRepository.save(systemUser);
                 }
             }
         }
     }

        return true;
    }

    public boolean deleteCheckMessage(UserInfoForToken userInfo,String id) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(id)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        systemMessageRepository.deleteById(id);
        systemUserRepository.deleteByUidAndSid(userInfo.getUserId(),id);
        return true;
    }


    public boolean  updateSystemAlready(UserInfoForToken userInfo,String id)throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
            SystemMessage systemMessage=systemMessageRepository.findById(id);
        systemUserRepository.updatealreadyRead(systemMessage.getId(),userInfo.getUserId(),"true");
        return true;
    }



}
