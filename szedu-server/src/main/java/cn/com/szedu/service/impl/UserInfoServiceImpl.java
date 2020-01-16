package cn.com.szedu.service.impl;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.constant.Status;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.exception.BackUserException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.*;
import cn.com.szedu.repository.*;
import cn.com.szedu.service.BackTokenService;
import cn.com.szedu.service.UserInfoService;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
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
private ICommonProblemRepository commonProblemRepository;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private ITeacherInfoRepository teacherInfoRepository;
    @Resource
    private IStudentInfoRespository studentInfoRespository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;
    @Resource
    private IMessageFeedbackRepository messageFeedbackRepository;
    @Resource
    private IClassInfoMapper classInfoMapper;



    @Override
    public void saveTeacher(UserInfoForToken userInfo,TeacherModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if (teacherInfoRepository.countByAccount(model.getAccount())>0){
            throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        TeacherInfo teacherInfo=MappingEntity2ModelCoverter.CONVERTEERFROMTEACHERMODEL1(model);
        teacherInfo.setStatus(Status.ACTIVATE.getName());
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加老师");
        opLogRepository.save(opLog);
        teacherInfoRepository.save(teacherInfo);
       /* if(userInfoRepository.countByAccount(model.getAccount())>0){
            throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        UserInfo info = MappingEntity2ModelCoverter.CONVERTEERFROMTEACHERMODEL(model);
        info.setStatus(Status.ACTIVATE.getName());
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加老师");
        opLogRepository.save(opLog);
        userInfoRepository.save(info);*/

    }

    @Override
    public void saveStudent(UserInfoForToken userInfo,StudentModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        int ok=studentInfoRespository.countByStudentId(model.getStudentId());
        if (ok>0){throw  new UserServiceException(ResultCode.STUDENTID_IS_EXIST);}//学号
        Integer count=studentInfoRespository.countByAccount(model.getAccount());
        if (count>0){throw  new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);}//账号
        Integer count2=studentInfoRespository.countByAccountAndPhone(model.getAccount(),model.getPhone());
        if (count2>0){throw  new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);}//账号和手机号

       /* TeacherInfo tinfo=teacherInfoRepository.findExsitById(userInfo.getUserId());*/
       /* SchoolInfo schoolInfo=schoolInfoRepository.findFirstById(model.getSchoolId());
        model.setSchoolId(tinfo.getSchoolId());
        model.setSchoolName(tinfo.getSchoolName());*/
        StudentInfo studentInfo=MappingEntity3ModelCoverter.CONVERTERFROMSTUDENTINFO(model);
        StudentInfo info=studentInfoRespository.save(studentInfo);
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加学生");
        opLogRepository.save(opLog);

        //系统信息
        String messages="成功创建学生-----您创建了一个新学生，学生账号："+info.getAccount();
        Message message=new Message(userInfo.getUserId(),userInfo.getUserName(),messages,"N");
        messageRepository.save(message);
       /* if(userInfoRepository.countByAccount(model.getAccount())>0){
            throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        UserInfo info = MappingEntity2ModelCoverter.CONVERTEERFROMSTUDENTMODEL(model);
        info.setStatus(Status.ACTIVATE.getName());
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加学生");
        opLogRepository.save(opLog);
        userInfoRepository.save(info);*/
    }

    @Override
    public void updateTeacher(UserInfoForToken userInfo, TeacherModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getId())){
            throw new UserServiceException(ResultCode.PARAM_ERR_MSG);
        }
        TeacherInfo info=teacherInfoRepository.findExsitById(model.getId());
        info.setIntegral(model.getIntegral());
        info.setName(model.getName());
        info.setSchoolName(model.getSchoolName());
        teacherInfoRepository.save(info);
       /* if (userInfo.getUserName().equalsIgnoreCase(model.getName())){//修改了老师姓名
            integralRecordRepository.updateOperatorName(userInfo.getUserId(),model.getName());

        }*/
      /*  UserInfo info=userInfoRepository.findFirstById(model.getId());
        info.setIntegral(model.getIntegral());
        info.setName(model.getName());
        info.setSchoolName(model.getSchoolName());
        info.setSubject(model.getSubject());
        userInfoRepository.save(info);*/
    }

    @Override
    public void updateStudent(UserInfoForToken userInfo, StudentModel model) throws UserServiceException {
        if (StringUtils.isEmpty(model.getId())){
            throw new UserServiceException(ResultCode.PARAM_ERR_MSG);
        }
        StudentInfo info=studentInfoRespository.findFirstByid(model.getId());
        if (info!=null) {
            Integer ss = 0;
            if (!(info.getPhone().equalsIgnoreCase(model.getPhone()))) {//手机号不等
                ss = studentInfoRespository.countByAccountAndPhone(model.getAccount(), model.getPhone());
            }
            if (ss > 0) {//存在相同手机号
                throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
            }
            info.setName(model.getName());
            info.setSex(model.getSex());
            info.setBirthday(model.getBirthday());
            info.setAddress(model.getAddress());
            info.setPhone(model.getPhone());
            studentInfoRespository.save(info);
        }
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
       SchoolInfo schoolInfo=null;
       try{
           // 得到这个excel表格对象
           HSSFWorkbook workbook=new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
           List<String> errorRowNum=new ArrayList<>();
           List<TeacherInfo> userInfos=new ArrayList<>();
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
                   TeacherInfo info;
                   try {
                       info=saveRowTeacher2(row,userAccounts);
                       userAccounts.add(info.getAccount());
                       if (!StringUtils.isEmpty(info.getSchoolName())) {
                           schoolInfo = schoolInfoRepository.findBySchoolName(info.getSchoolName());
                           if (schoolInfo == null) {
                               continue;
                           }
                           info.setSchoolId(schoolInfo.getId());
                       }

                   }catch (UserServiceException e){
                       errorRowNum.add((j + 1) + " ");
                       continue;
                   }
                   userInfos.add(info);
               }
           }
           if (userInfos.size()>0){
               teacherInfoRepository.saveAll(userInfos);
               OpLog opLog=new OpLog(userInfo.getUserName(),"添加","批量添加老师");
               opLogRepository.save(opLog);
               return JSONObject.toJSONString(errorRowNum.toArray());
           }
          return null;
       }catch (IOException e){
           throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
       }
   }

    @Override
    public String saveExcelStudents(MultipartFile file, UserInfoForToken userInfo,String schoolId) throws UserServiceException {
       SchoolInfo schoolInfo=schoolInfoRepository.findFirstById(schoolId);
       if (schoolInfo==null){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".xls")){
            System.out.println("文件不是.xls类型");
        }
        try{
            // 得到这个excel表格对象
            HSSFWorkbook workbook=new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            List<String> errorRowNum=new ArrayList<>();
            List<StudentInfo> userInfos=new ArrayList<>();
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
                    StudentInfo info;
                    try {
                        info=saveRowStudent2(row,userAccounts);
                        userAccounts.add(info.getAccount());
                        info.setSchoolId(schoolInfo.getId());
                        info.setSchoolName(schoolInfo.getSchoolName());
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    userInfos.add(info);
                }
            }
            studentInfoRespository.saveAll(userInfos);
            OpLog opLog=new OpLog(userInfo.getUserName(),"添加","批量添加学生");
            opLogRepository.save(opLog);
            return JSONObject.toJSONString(errorRowNum.toArray());
        }catch (IOException e){
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }
    @Override
    public PageInfo<TeacherModel> getTeachersByCondition(UserInfoForToken userInfo, UserConditionModel model) throws UserServiceException {
        List<TeacherInfo>  infos=new ArrayList<TeacherInfo>();
        int  count=0;
            if (StringUtils.isEmpty(model.getUserName())){
                infos=teacherInfoRepository.findAll();
                count =infos.size();
            }else {
                infos=teacherInfoRepository.findByNameLike("%"+model.getUserName()+"%");
                count =teacherInfoRepository.countByNameLike("%"+model.getUserName()+"%");
            }
        List<TeacherModel> models = new ArrayList<TeacherModel>();
        infos.forEach(userInfo1 -> {
            models.add(MappingEntity2ModelCoverter.CONVERTERFROMUSERINFO2(userInfo1));
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
        List<StudentInfo> infos=new ArrayList<>();
        int count=0;
      if (StringUtils.isEmpty(model.getUserName())){
          infos=studentInfoRespository.findAll();
          count=infos.size();
      }else {
          infos=studentInfoRespository.findByNameLike("%"+model.getUserName()+"%");
          count=studentInfoRespository.countByNameLike("%"+model.getUserName()+"%");
      }

        List<StudentModel> models = new ArrayList<StudentModel>();
        infos.forEach(userInfo1 -> {
            models.add(MappingEntity2ModelCoverter.CONVERTERFROMUSERINFOTOSTUDENT2(userInfo1));
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
       /* List<UserInfo> infos=userInfoRepository.findByRole("Teacher");*/
        List<TeacherInfo> infos=teacherInfoRepository.findAll();
        List<TeacherModel> models = new ArrayList<TeacherModel>();
        infos.forEach(userInfo1 -> {
            models.add(MappingEntity2ModelCoverter.CONVERTERFROMUSERINFO2(userInfo1));
        });
        return models;
    }

    private TeacherInfo saveRowTeacher2(HSSFRow row,List<String> accounts) throws UserServiceException{
        TeacherInfo userInfo=new TeacherInfo();
        String account = "";
        String userName = "";
        String sexStr = "";
        String jifen = "";
        String schoolName = "";
        String pwd = "";
        SchoolInfo schoolInfo=null;
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
                    jifen = getCellNumber(row.getCell(i));
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
           /* schoolInfo=schoolInfoRepository.findBySchoolName(schoolName);
            if (schoolInfo==null) {
                break;
            }*/
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
        Integer jifen2=Integer.valueOf(jifen);
        userInfo.setSex(sexStr);
        userInfo.setAccount(account);
        userInfo.setName(userName);
        userInfo.setIntegral(jifen2);
        userInfo.setSchoolName(schoolName);
        //userInfo.setSchoolId(schoolInfo.getId());
        userInfo.setPwd(Md5Util.encodeByMd5(pwd));
        userInfo.setRole("Teacher");
        userInfo.setIntegral(0);
        userInfo.setStatus(Status.ACTIVATE.getName());
        return userInfo;
    }

   private StudentInfo saveRowStudent2(HSSFRow row,List<String> accounts) throws UserServiceException{
       StudentInfo userInfo=new StudentInfo();
       String account = "";
       String userName = "";
       String phone="";
       String sexStr = "F";
       String pwd = "";
       String studentId = "";
       int physicalNumberOfCells = row.getPhysicalNumberOfCells();
       for (int i = 0; i < physicalNumberOfCells; i++) {
           switch (i) {
               case 0:
                   userName = getCellString(row.getCell(i));
                   break;
               case 1:
                   studentId = getCellNumber(row.getCell(i));
                   break;
               case 2:
                   account = getCellString(row.getCell(i));
                   break;
               case 3:
                   phone = getCellNumber(row.getCell(i));
                   break;
               case 4:
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

       userInfo.setSex(sexStr);
       userInfo.setAccount(account);
       userInfo.setName(userName);
       userInfo.setPwd(Md5Util.encodeByMd5(pwd));
       userInfo.setPhone(phone);
       userInfo.setRole("Student");
       userInfo.setStatus(Status.ACTIVATE.getName());
       userInfo.setStudentId(studentId);
       userInfo.setIntegral(0);
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

    /**
     * 回去学生留言
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    @Override
    public List<Message> getLeavingMessage(UserInfoForToken userInfo) throws UserServiceException {
       List<Message> messages=messageRepository.findByRecipientId("1");
        return messages;
    }


    @Override
    public List<SchoolInfo> getAllSchool(UserInfoForToken userInfo) throws UserServiceException {
        List<SchoolInfo> schoolInfos=schoolInfoRepository.findAll();
        return schoolInfos;
    }

    @Override
    public List<MessageFeedback> getMessageFeedback(UserInfoForToken userInfo) throws UserServiceException {
        List<MessageFeedback> feedbacks=messageFeedbackRepository.findAll();
        return feedbacks;
    }

    @Override
    public boolean teacherStatus(UserInfoForToken userInfo, String teacherId, String status) throws UserServiceException {
      if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(teacherId)||StringUtils.isEmpty(status)){
          throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
      }
       TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(teacherId);
      if (StringUtils.isEmpty(teacherInfo)){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        //teacherInfoRepository.updateIsStatus(teacherId, status);
classInfoMapper.updateIsStatus(teacherId, status);
        return true;
    }


    @Override
    public boolean teacherPwd(UserInfoForToken userInfo, String teacherId, String pwd) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(teacherId)||StringUtils.isEmpty(pwd)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        String pwd2=Md5Util.encodeByMd5(pwd);
       // teacherInfoRepository.updatePwd(teacherId, pwd2);
        classInfoMapper.updatePwd(teacherId, pwd2);
        return true;
    }

    @Override
    public boolean teacherdelete(UserInfoForToken userInfo, String teacherId) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(teacherId)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        teacherInfoRepository.findExsitById(teacherId);
        return true;
    }

    @Override
    public boolean addProblem(UserInfoForToken userInfo, CommonProblem problem) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(problem)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        commonProblemRepository.save(problem);
        return true;
    }

    @Override
    public boolean updateProblem(UserInfoForToken userInfo, CommonProblem problem) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(problem)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        CommonProblem commonProblem=commonProblemRepository.findById(problem.getId());
        commonProblem.setCotent(problem.getCotent());
        commonProblem.setAnswer(problem.getAnswer());
        commonProblemRepository.save(commonProblem);
        return true;
    }

    @Override
    public List<CommonProblem> allProblem(UserInfoForToken userInfo) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
      List<CommonProblem> list=commonProblemRepository.findAll();
        return list;
    }

    @Override
    public List<CommonProblem> schoolProblem(UserInfoForToken userInfo, String schoolid) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<CommonProblem> list=commonProblemRepository.findBySchoolId(schoolid);
        return list;
    }

    @Override
    public List<CommonProblem> aroblemAnswer(UserInfoForToken userInfo, String id) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<CommonProblem> list=commonProblemRepository.findByDid(id);
        return list;
    }
}
