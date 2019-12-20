package cn.com.szedu.service.impl;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.constant.Status;
import cn.com.szedu.entity.BackUser;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.entity.UserInfo;
import cn.com.szedu.exception.BackUserException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.*;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.IUserInfoRepository;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.UserInfoService;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private IUserInfoRepository userInfoRepository;

    @Resource
    private BackTokenService backTokenService;

    @Resource
    private IOpLogRepository opLogRepository;

    @Override
    public void saveTeacher(UserInfoForToken userInfo,TeacherModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if(userInfoRepository.countByAccount(model.getAccount())>0){
            throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        UserInfo info = MappingEntity2ModelCoverter.CONVERTEERFROMTEACHERMODEL(model);
        info.setStatus(Status.ACTIVATE.getName());
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加老师");
        opLogRepository.save(opLog);
        userInfoRepository.save(info);
    }

    @Override
    public void saveStudent(UserInfoForToken userInfo,StudentModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if(userInfoRepository.countByAccount(model.getAccount())>0){
            throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        UserInfo info = MappingEntity2ModelCoverter.CONVERTEERFROMSTUDENTMODEL(model);
        info.setStatus(Status.ACTIVATE.getName());
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加学生");
        opLogRepository.save(opLog);
        userInfoRepository.save(info);
    }

    @Override
    public void updateTeacher(UserInfoForToken userInfo, TeacherModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getId())){
            throw new UserServiceException(ResultCode.PARAM_ERR_MSG);
        }
        UserInfo info=userInfoRepository.findFirstById(model.getId());
        info.setIntegral(model.getIntegral());
        info.setName(model.getName());
        info.setSchoolName(model.getSchoolName());
        info.setSubject(model.getSubject());
        userInfoRepository.save(info);
    }

    @Override
    public void updateStudent(UserInfoForToken userInfo, StudentModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getId())){
            throw new UserServiceException(ResultCode.PARAM_ERR_MSG);
        }
        UserInfo info=userInfoRepository.findFirstById(model.getId());
        info.setName(model.getName());
        info.setSchoolName(model.getSchoolName());
        info.setPhone(model.getPhone());
        userInfoRepository.save(info);
    }

    @Override
    public void deleteUserInfo(UserInfoForToken userInfo, String userId) throws UserServiceException {
        if (StringUtils.isEmpty(userId)) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        userInfoRepository.deleteById(userId);
    }

    @Override
    public String saveExcelTeachers(MultipartFile file, UserInfoForToken userInfo) throws UserServiceException {
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".xls")){
            System.out.println("文件不是.xls类型");
        }
        try{
            // 得到这个excel表格对象
            HSSFWorkbook workbook=new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            List<String> errorRowNum=new ArrayList<>();
            List<UserInfo> userInfos=new ArrayList<>();
            List<String> userAccounts=new ArrayList<>();
            // 得到这个excel表格的sheet数量
            int numberOfSheets=workbook.getNumberOfSheets();
            for (int i=0;i<numberOfSheets;i++){
                //得到sheet
                HSSFSheet sheet=workbook.getSheetAt(i);
                //得到sheet里的总行数
                int physicalNumberOfRows=sheet.getPhysicalNumberOfRows();
                for(int j=1;j<physicalNumberOfRows;j++){
                    HSSFRow row=sheet.getRow(j);
                    UserInfo info;
                    try {
                        info=saveRowTeacher(row,userAccounts);
                        userAccounts.add(info.getAccount());
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    userInfos.add(info);
                }
            }
            userInfoRepository.saveAll(userInfos);
            OpLog opLog=new OpLog(userInfo.getUserName(),"添加","批量添加老师");
            opLogRepository.save(opLog);
            return JSONObject.toJSONString(errorRowNum.toArray());
        }catch (IOException e){
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }

    @Override
    public String saveExcelStudents(MultipartFile file, UserInfoForToken userInfo) throws UserServiceException {
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".xls")){
            System.out.println("文件不是.xls类型");
        }
        try{
            // 得到这个excel表格对象
            HSSFWorkbook workbook=new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            List<String> errorRowNum=new ArrayList<>();
            List<UserInfo> userInfos=new ArrayList<>();
            List<String> userAccounts=new ArrayList<>();
            // 得到这个excel表格的sheet数量
            int numberOfSheets=workbook.getNumberOfSheets();
            for (int i=0;i<numberOfSheets;i++){
                //得到sheet
                HSSFSheet sheet=workbook.getSheetAt(i);
                //得到sheet里的总行数
                int physicalNumberOfRows=sheet.getPhysicalNumberOfRows();
                for(int j=1;j<physicalNumberOfRows;j++){
                    HSSFRow row=sheet.getRow(j);
                    UserInfo info;
                    try {
                        info=saveRowStudent(row,userAccounts);
                        userAccounts.add(info.getAccount());
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    userInfos.add(info);
                }
            }
            userInfoRepository.saveAll(userInfos);
            OpLog opLog=new OpLog(userInfo.getUserName(),"添加","批量添加学生");
            opLogRepository.save(opLog);
            return JSONObject.toJSONString(errorRowNum.toArray());
        }catch (IOException e){
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }

    @Override
    public PageInfo<TeacherModel> getTeachersByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException {
        List<UserInfo> infos=new ArrayList<>();
        int count=0;
        if (StringUtils.isEmpty(model.getUserName())){
            infos=userInfoRepository.findByRole("Teacher");
            count=userInfoRepository.countByRole("Teacher");
        }else{
            infos=userInfoRepository.findByRoleAndNameLike("Teacher","%"+model.getUserName()+"%");
            count=userInfoRepository.countByRoleAndNameLike("Teacher","%"+model.getUserName()+"%");
        }
        List<TeacherModel> models = new ArrayList<TeacherModel>();
        infos.forEach(userInfo1 -> {
            models.add(MappingEntity2ModelCoverter.CONVERTERFROMUSERINFO(userInfo1));
        });
        PageInfo<TeacherModel> pageInfo=new PageInfo<>();
        pageInfo.setList(models);
        pageInfo.setPageNum(model.getPageIndex());
        pageInfo.setPageSize(model.getPageSize());
        pageInfo.setTotal(count);
        return pageInfo;
    }

    @Override
    public PageInfo<StudentModel> getStudentsByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException {
        List<UserInfo> infos=new ArrayList<>();
        int count=0;
        if (StringUtils.isEmpty(model.getUserName())){
            infos=userInfoRepository.findByRole("Student");
            count=userInfoRepository.countByRole("Student");
        }else{
            infos=userInfoRepository.findByRoleAndNameLike("Student","%"+model.getUserName()+"%");
            count=userInfoRepository.countByRoleAndNameLike("Student","%"+model.getUserName()+"%");
        }
        List<StudentModel> models = new ArrayList<StudentModel>();
        infos.forEach(userInfo1 -> {
            models.add(MappingEntity2ModelCoverter.CONVERTERFROMUSERINFOTOSTUDENT(userInfo1));
        });
        PageInfo<StudentModel> pageInfo=new PageInfo<>();
        pageInfo.setList(models);
        pageInfo.setPageNum(model.getPageIndex());
        pageInfo.setPageSize(model.getPageSize());
        pageInfo.setTotal(count);
        return pageInfo;
    }

    @Override
    public void updateStaffPwd(UserInfoForToken userInfo,String id, String newPwd, String pwd) throws UserServiceException {
        if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(newPwd)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if (!pwd.equals(newPwd)){
            throw new UserServiceException(ResultCode.PWD_DIFFERENT_MSG);
        }
        UserInfo userInfo1=userInfoRepository.findFirstById(id);
        userInfo1.setPwd(Md5Util.encodeByMd5(pwd));
        userInfoRepository.save(userInfo1);
    }

    @Override
    public String loginBackUser(BackUserLoginModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        UserInfo userInfo=userInfoRepository.findByAccountAndPwd(model.getAccount(),Md5Util.encodeByMd5(model.getPwd()));
        if (userInfo==null){
            throw new UserServiceException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        String user=JSONObject.toJSONString(MappingEntity2ModelCoverter.CONVERTERFROMBACKUSERUSER(userInfo));
        try {
            return backTokenService.createdToken(user);
        }catch (UnsupportedEncodingException e){
            throw new UserServiceException(e.getMessage());
        }
    }

    @Override
    public void insertSuperMan() throws UserServiceException {
        if (userInfoRepository.countByAccount("admin") > 0){
            throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        UserInfo userInfo=new UserInfo();
        userInfo.setName("admin");
        userInfo.setAccount("admin");
        userInfo.setPwd(Md5Util.encodeByMd5("admin"));
        userInfo.setRole("admin");
        userInfo.setSchoolName("12345");
        userInfo.setCreateTime(new Date().getTime());
        userInfo.setSubject("12345");
        userInfo.setPhone("123456");
        userInfoRepository.save(userInfo);
    }

    @Override
    public void updateUserIntegral(String userId, String integral)throws UserServiceException {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(integral)){
            throw new  UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        userInfoRepository.updateIntegral(integral,userId);
    }

    @Override
    public List<TeacherModel> getAllTeacher() {
        List<UserInfo> infos=userInfoRepository.findByRole("Teacher");
        List<TeacherModel> models = new ArrayList<TeacherModel>();
        infos.forEach(userInfo1 -> {
            models.add(MappingEntity2ModelCoverter.CONVERTERFROMUSERINFO(userInfo1));
        });
        return models;
    }

    private UserInfo saveRowTeacher(HSSFRow row,List<String> accounts) throws UserServiceException{
        UserInfo userInfo=new UserInfo();
        String account = "";
        String userName = "";
        String sexStr = "";
        String subject = "";
        String schoolName = "";
        String pwd = "";
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        for (int i = 0; i < physicalNumberOfCells; i++) {
            switch (i) {
                case 0:
                    account = getCellString(row.getCell(i));
                    break;
                case 1:
                    userName = getCellString(row.getCell(i));
                    break;
                case 2:
                    sexStr = getCellString(row.getCell(i));
                    break;
                case 3:
                    subject = getCellString(row.getCell(i));
                    break;
                case 4:
                    schoolName = getCellString(row.getCell(i));
                    break;
                case 5:
                    pwd = getCellString(row.getCell(i));
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isEmpty(account)) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if (StringUtils.isEmpty(pwd)) {
            pwd="abcd1234";
        }
        if (accounts.contains(account)) {
            throw new UserServiceException(ResultCode.USER_IS_EXIST);
        }
        /*if ("男".equals(sexStr.trim())) {
            userInfo.setSex("M");
        } else {
            userInfo.setSex("F");
        }*/
        userInfo.setSex(sexStr);
        userInfo.setAccount(account);
        userInfo.setName(userName);
        userInfo.setSubject(subject);
        userInfo.setSchoolName(schoolName);
        userInfo.setPwd(Md5Util.encodeByMd5(pwd));
        userInfo.setRole("Teacher");
        userInfo.setIntegral(0);
        userInfo.setStatus(Status.ACTIVATE.getName());
        return userInfo;
    }

    private UserInfo saveRowStudent(HSSFRow row,List<String> accounts) throws UserServiceException{
        UserInfo userInfo=new UserInfo();
        String account = "";
        String userName = "";
        String phone="";
        String sexStr = "";
        String schoolName = "";
        String pwd = "";
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        for (int i = 0; i < physicalNumberOfCells; i++) {
            switch (i) {
                case 0:
                    account = getCellString(row.getCell(i));
                    break;
                case 1:
                    userName = getCellString(row.getCell(i));
                    break;
                case 2:
                    phone = getCellNumber(row.getCell(i));
                    break;
                case 3:
                    schoolName = getCellString(row.getCell(i));
                    break;
                case 4:
                    sexStr = getCellString(row.getCell(i));
                    break;
                case 5:
                    pwd = getCellString(row.getCell(i));
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isEmpty(account)) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if (StringUtils.isEmpty(pwd)) {
            pwd="abcd1234";
        }
        if (accounts.contains(account)) {
            throw new UserServiceException(ResultCode.USER_IS_EXIST);
        }
        /*if ("男".equals(sexStr.trim())) {
            userInfo.setSex("M");
        } else {
            userInfo.setSex("F");
        }*/
        userInfo.setSex(sexStr);
        userInfo.setAccount(account);
        userInfo.setName(userName);
        userInfo.setPwd(Md5Util.encodeByMd5(pwd));
        userInfo.setPhone(phone);
        userInfo.setSchoolName(schoolName);
        userInfo.setRole("Student");
        userInfo.setStatus(Status.ACTIVATE.getName());
        return userInfo;
    }

    private String getCellString(HSSFCell cell) throws UserServiceException {
        if (null == cell) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        return cell.getStringCellValue();
    }

    private String getCellNumber(HSSFCell cell) throws UserServiceException {
        if (null == cell) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        return NumberToTextConverter.toText(cell.getNumericCellValue());
    }
}
