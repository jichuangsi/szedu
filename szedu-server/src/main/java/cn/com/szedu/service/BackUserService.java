package cn.com.szedu.service;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.BackUser;
import cn.com.szedu.exception.BackUserException;
import cn.com.szedu.model.BackUserLoginModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IBackUserRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class BackUserService {

    @Resource
    private IBackUserRepository backUserRepository;
    @Resource
    private BackTokenService backTokenService;

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
}
