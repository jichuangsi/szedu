package cn.com.szedu.service.impl;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.BackUser;
import cn.com.szedu.entity.IntermediateTable.SchoolCurriculumRelation;
import cn.com.szedu.entity.IntermediateTable.SchoolUserRelation;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.entity.SchoolInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.student.SchoolInfoModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IBackUserRepository;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.ISchoolInfoRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ISchoolCurriculumRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ISchoolUserRelationRepository;
import cn.com.szedu.service.IBackSchoolService;
import cn.com.szedu.service.UserInfoService;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class BackSchoolServiceImpl implements IBackSchoolService {
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ISchoolUserRelationRepository schoolUserRelationRepository;

    @Resource
    private ISchoolCurriculumRelationRepository schoolCurriculumRelationRepository;
    @Resource
    private IBackUserRepository backUserRepository;

    @Resource
    private IOpLogRepository opLogRepository;
    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;

    @Override
    public void addSchool(UserInfoForToken userInfo,SchoolInfoModel model) {
        //保存学校信息
        SchoolInfo info=schoolInfoRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMSCHOOLINFOMODELSCHOOLINFO(model));
        for (SchoolCurriculumRelation r:model.getSchoolCurriculumRelations()) {
            r.setSchoolId(info.getId());
        }
        //保存学校可使用的课程信息
        schoolCurriculumRelationRepository.saveAll(model.getSchoolCurriculumRelations());
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加考试");
        opLogRepository.save(opLog);
    }

    //学校内添加教师
    @Override
    public void saveTeacherBySchool(String schoolId, String teacherId) {
        SchoolUserRelation relation=new SchoolUserRelation();
        relation.setSchoolId(schoolId);
        relation.setUid(teacherId);
        schoolUserRelationRepository.save(relation);
    }

    @Override
    public boolean schoolPic(MultipartFile file, UserInfoForToken userInfo, String schoolId) throws IOException {
        if (StringUtils.isEmpty(schoolId)){//
            throw new IOException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo schoolInfo=schoolInfoRepository.findFirstById(schoolId);

        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>3){
            throw new IOException("图片过大！");
        }
        //重新生成文件名
        fileName =UUID.randomUUID()+suffixName;
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        schoolInfo.setPortrait(uri+fileName);
        schoolInfoRepository.save(schoolInfo);
        return true;
    }

    @Override
    public boolean updateSchoolInfo(UserInfoForToken userInfo, SchoolInfoModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model) || StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        SchoolInfo schoolInfo=schoolInfoRepository.findFirstById(model.getId());
        if (StringUtils.isEmpty(schoolInfo)){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        schoolInfo.setSchoolName(model.getSchoolName());
        schoolInfo.setAddress(model.getAddress());
        schoolInfoRepository.save(schoolInfo);
        return true;
    }

    @Override
    public void schoolInfoStatus(UserInfoForToken userInfo, String schoolId, String status) throws UserServiceException {
        if (StringUtils.isEmpty(schoolId) || StringUtils.isEmpty(status)|| StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        schoolInfoRepository.updateIsStatus(schoolId,status);
    }

    @Override
    public boolean saveAdmin(BackUser user, UserInfoForToken userInfo)throws UserServiceException {
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        Integer count=backUserRepository.countByAccount(user.getAccount());
        if (count>0){throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);}
        user.setPwd(Md5Util.encodeByMd5(user.getPwd()));
        backUserRepository.save(user);
        return true;
    }

    @Override
    public boolean updateAdmin(BackUser user, UserInfoForToken userInfo) throws UserServiceException {
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
            BackUser user1=backUserRepository.findByid(user.getId());
        if (user1==null){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        user1.setUserName(user.getUserName());
        backUserRepository.save(user1);
        return true;
    }

    @Override
    public boolean deleteAdmin(UserInfoForToken userInfo, String id) throws UserServiceException {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
       backUserRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean frozenAdmin(UserInfoForToken userInfo,String id,String status) throws UserServiceException {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        backUserRepository.updateIsStatus(id,status);
        return true;
    }

    @Override
    public boolean adminPwd(UserInfoForToken userInfo, String id, String pwd) throws UserServiceException {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(userInfo)|| StringUtils.isEmpty(pwd)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        String pwd2=Md5Util.encodeByMd5(pwd);
        backUserRepository.updatePwd(id,pwd2);
        return true;
    }
}
