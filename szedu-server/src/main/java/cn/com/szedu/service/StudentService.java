package cn.com.szedu.service;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.constant.Status;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.ClassInfo;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.entity.StudentInfo;
import cn.com.szedu.entity.TeacherInfo;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IClassInfoRepository;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.IStudentInfoRespository;
import cn.com.szedu.repository.ITeacherInfoRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    @Resource
    private IStudentInfoRespository studentRespository;
    @Resource
    private IStudentClassRelationRepository studentClassRelationRepository;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private ITeacherInfoRepository teacherInfoRepository;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private IClassInfoMapper classInfoMapper;


    /**
     * 新增学生
     * @param userInfo
     * @param model
     * @throws UserServiceException
     */
    public void insertStudent(UserInfoForToken userInfo, StudentModel model) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(model) ||StringUtils.isEmpty(model.getClassId())){throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        boolean ok=checkStudentId(model.getStudentId());
        if (ok==false){throw  new UserServiceException(ResultCode.STUDENTID_IS_EXIST);}//学号
       /* boolean s=checkStudent(model.getName(),model.getAccount());
        if (s==false){throw  new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);}*/
        Integer count=studentRespository.countByAccount(model.getAccount());
        if (count>0){throw  new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);}//账号
        Integer count2=studentRespository.countByAccountAndPhone(model.getAccount(),model.getPhone());
        if (count2>0){throw  new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);}//账号和手机号

        TeacherInfo tinfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
        model.setSchoolId(tinfo.getSchoolId());
        model.setSchoolName(tinfo.getSchoolName());
        StudentInfo studentInfo=MappingEntity3ModelCoverter.CONVERTERFROMSTUDENTINFO(model);
        StudentInfo info=studentRespository.save(studentInfo);
        StudentClassRelation screlation=new StudentClassRelation();
        screlation.setClassId(model.getClassId());
        screlation.setStudentId(info.getId());
        studentClassRelationRepository.save(screlation);
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加学生");
        opLogRepository.save(opLog);
    }

    /**
     * 修改学生
     * @param userInfo
     * @param model
     * @throws UserServiceException
     */
    public void upadteStudent(UserInfoForToken userInfo, StudentModel model) throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(model)){throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);}
       // boolean ok=checkStudentId(model.getStudentId());
        Integer count=studentRespository.countByAccount(model.getAccount());
        if (count>0){throw  new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);}
        Integer count2=studentRespository.countByAccountAndPhone(model.getAccount(),model.getPhone());
        if (count2>0){throw  new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);}
       StudentInfo info=studentRespository.findFirstByid(model.getId());
        if (info!=null){
            info.setName(model.getName());
            info.setStudentId(model.getStudentId());
            info.setPhone(model.getPhone());
            info.setAccount(model.getAccount());
            info.setPwd(model.getPwd());
            studentRespository.save(info);
        }
    }

    /**|
     * 查询学号是否重复
     * @param studentId
     * @return
     */
    public boolean checkStudentId(String studentId)throws UserServiceException{
        if (StringUtils.isEmpty(studentId)){throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        Integer count=studentRespository.countByStudentId(studentId);
        if (count>0){
            return false;
        }
        return true;
    }
    /**|
     * 查询学号是否重复
     * @param
     * @return
     */
    public boolean checkStudent(String studentName,String acount)throws UserServiceException{
        if (StringUtils.isEmpty(studentName)||StringUtils.isEmpty(acount)){throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        Integer count=studentRespository.countByNameAndAccount(studentName, acount);
        if (count>0){
            return false;
        }
        return true;
    }
    /**
     * 根据班级id得到学生
     * @param userInfo
     * @param classId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws UserServiceException
     */
    public PageInfo<StudentModel> getStudentByClassId(UserInfoForToken userInfo, String classId, int pageNum, int pageSize) throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(classId)){
            throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo classInfo=classInfoRepository.findExistById(classId);
        List<StudentClassRelation> relation=studentClassRelationRepository.findAllByClassId(classId);
        List<StudentModel> studentModels=new ArrayList<StudentModel>();
        List<StudentModel> models=new ArrayList<StudentModel>();
        if (relation!=null){
            for (StudentClassRelation s:relation) {
                StudentInfo info=studentRespository.findFirstByid(s.getStudentId());
                if (info!=null){
                    StudentModel model=MappingEntity3ModelCoverter.CONVERTERFROMSTUDENTMODEL(info);
                    model.setSpecialtity(classInfo.getSpeciality());
                    studentModels.add(model);
                }
            }
            models=classInfoMapper.getPageByClassId(classId,(pageNum-1)*pageSize,pageSize);
            PageInfo<StudentModel> slist=new PageInfo<>();
            slist.setList(studentModels);
            slist.setTotal(studentModels.size());
            slist.setPageNum(pageNum);
            slist.setPageSize(pageSize);
            return slist;
        }
        return null;
    }
    /**
     * 批量添加学生
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    public String saveExcelStudents(MultipartFile file, UserInfoForToken userInfo,String classId) throws UserServiceException {
        //TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
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
                        info=saveRowStudent(row,userAccounts);
                        userAccounts.add(info.getAccount());
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    userInfos.add(info);
                }
            }
            List<StudentInfo> info=studentRespository.saveAll(userInfos);
            for (StudentInfo s:info) {
                StudentClassRelation screlation=new StudentClassRelation();
                screlation.setClassId(classId);
                screlation.setStudentId(s.getId());
                studentClassRelationRepository.save(screlation);
            }

            OpLog opLog=new OpLog(userInfo.getUserName(),"添加","批量添加学生");
            opLogRepository.save(opLog);
            return JSONObject.toJSONString(errorRowNum.toArray());
        }catch (IOException e){
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }
    /**
     * 修改学生密码
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    public void upadteStudentPwd(UserInfoForToken userInfo,String studentId,String pwd) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(studentId)||StringUtils.isEmpty(pwd)){
            throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        StudentInfo info=studentRespository.findFirstByid(studentId);
        if (info!=null){
            info.setPwd(Md5Util.encodeByMd5(pwd));
            studentRespository.save(info);
        }
    }

    /**
     * 根据id查学生
     * @param userInfo
     * @param studentId
     * @return
     */
    public StudentModel getStudentById(UserInfoForToken userInfo,String studentId){
      StudentInfo info=  studentRespository.findFirstByid(studentId);
      if (info!=null){
          StudentModel model=MappingEntity3ModelCoverter.CONVERTERFROMSTUDENTMODEL(info);
          return model;
      }
        return null;
    }

    /**
     * 给学生添加新班级
     * @param userInfo
     * @param classId
     */
    public void addClassByStudent(UserInfoForToken userInfo,String[] classId,String studentId){
        for (int i=0;i<classId.length;i++){
            StudentClassRelation screlation=new StudentClassRelation();
            screlation.setClassId(classId[i]);
            screlation.setStudentId(studentId);
            studentClassRelationRepository.save(screlation);
        }
    }

    /**
     * 移动学生
     * @param userInfo
     * @param oldClassId
     * @param classId
     * @param studentId
     * @throws TecherException
     */
    public void addRemoveStudent(UserInfoForToken userInfo,String oldClassId,String classId,String studentId)throws TecherException {
      if (StringUtils.isEmpty(userInfo)||StringUtils.isEmpty(oldClassId) ||StringUtils.isEmpty(classId) ||StringUtils.isEmpty(studentId)){
          throw new TecherException(ResultCode.PARAM_MISS_MSG);
      }
        boolean count=studentClassRelationRepository.deleteByClassIdAndStudentId(oldClassId,studentId);
      if (count==true){
          StudentClassRelation screlation=new StudentClassRelation();
          screlation.setStudentId(studentId);
          screlation.setClassId(classId);
          studentClassRelationRepository.save(screlation);
      }

    }





    private StudentInfo saveRowStudent(HSSFRow row, List<String> accounts) throws UserServiceException{
        StudentInfo userInfo=new StudentInfo();
        String account = "";
        String userName = "";
        String phone="";
        String sexStr = "F";
        String schoolName = "";
        String pwd = "";
        String studentId = "";
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        for (int i = 0; i < physicalNumberOfCells; i++) {
            switch (i) {
                case 0:
                    userName = getCellString(row.getCell(i));
                    break;
                case 1:
                    studentId = getCellString(row.getCell(i));
                    break;
                case 2:
                    account = getCellNumber(row.getCell(i));
                    break;
                case 3:
                    phone = getCellString(row.getCell(i));
                    break;
                case 4:
                    pwd = getCellString(row.getCell(i));
                    break;
               /* case 5:
                    pwd = getCellString(row.getCell(i));
                    break;
                case 6:
                    studentId = getCellNumber(row.getCell(i));
                    break;*/
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
      /*  userInfo.setSchoolName(schoolName);*/
        userInfo.setRole("Student");
        userInfo.setStatus(Status.ACTIVATE.getName());
        userInfo.setStudentId(studentId);
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
