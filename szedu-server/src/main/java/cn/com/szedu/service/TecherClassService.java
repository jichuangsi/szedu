package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.ClassRelation;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.ClassModel;
import cn.com.szedu.model.teacher.TeacherAddClassModel;
import cn.com.szedu.repository.IClassInfoRepository;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.ITeacherInfoRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.TecherClassRelationRepository;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TecherClassService {

    @Resource
    private TecherClassRelationRepository techerClassRelationRepository;
    @Resource
    private IClassInfoRepository classInfoRepository;

    @Resource
    private IStudentClassRelationRepository srelationRepository;
    @Resource
    private ITeacherInfoRepository teacherInfoRepository;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private IClassInfoMapper classInfoMapper;
   /* @Resource
    private MessageRepository messageRepository;*/


    /**
     * 学校管理员创建班级
     * @param userInfo
     * @param model
     * @throws UserServiceException
     */
    public void insertClass(UserInfoForToken userInfo,ClassModel model) throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(model)){
            throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        //判断是否存在改新增班级
        Integer count=classInfoRepository.countBySpecialityAndRuTimeAndEducationalSystemAndClassName(model.getSpecialty()
            ,model.getEnrollTime(),model.getEducationalSystem(),model.getClassName());
        if (count>0){
            throw new UserServiceException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
        model.setSchoolId(teacherInfo.getSchoolId());
        model.setSchoolName(teacherInfo.getSchoolName());
        String timeStr1=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        model.setCreateTime(timeStr1);
        ClassInfo info=MappingEntity3ModelCoverter.CONVERTERFROMCLASSMODEL(userInfo,model);
        ClassInfo info1= classInfoRepository.save(info);//新增班级
        ClassRelation classRelation=new ClassRelation();
        classRelation.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        classRelation.setClassId(info1.getId());//班级id
        classRelation.setTecherId(userInfo.getUserId());//老师id
        techerClassRelationRepository.save(classRelation);//班级老师关系表
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加班级");
        opLogRepository.save(opLog);

       /* //系统信息
        String messages="成功创建班级-----您创建了一个新班级，班级信息：专业-"+info.getSpeciality()+"  班级-"+info.getClassName();
        Message message=new Message(userInfo.getUserId(),userInfo.getUserName(),messages);
        messageRepository.save(message);*/
      /*  //返回信息
        ClassModel classModel=new ClassModel();
        classModel.setSpecialty(info.getSpeciality());
        classModel.setClassName(info.getClassName());
        classModel.setCreateTime(info.getCreateTime());
        return classModel;*/
     /* String message="您先添加了";
      return message;*/


    }

    /**
     * 获取我的班级
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    public PageInfo<ClassModel> getAllClass(UserInfoForToken userInfo, int pageNum, int pageSize) throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){throw new UserServiceException(ResultCode.PARAM_MISS_MSG); }
        List<ClassModel> classModels=new ArrayList<ClassModel>();
        List<ClassModel> classModels2=new ArrayList<ClassModel>();
        ClassModel classi=null;
       /* List<ClassRelation> classaId= techerClassRelationRepository.findByTecherId(userInfo.getUserId());//老师班级关系表
        if (classaId.size()<0){throw new UserServiceException(ResultCode.SELECT_NULL_MSG); }
        List<String> classid=new ArrayList<String>();
        for (ClassRelation c:classaId) {
            classid.add(c.getClassId());
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        List<ClassInfo> classInfos=classInfoRepository.getClassInfoByIdIn(sort,classid);//老师的班级
        if (classInfos.size()<0){throw new UserServiceException(ResultCode.SELECT_NULL_MSG);}
        List<ClassModel> classModels=new ArrayList<ClassModel>();
        ClassModel classi=null;
        for (ClassInfo info:classInfos) {
            classi=MappingEntity3ModelCoverter.CONVERTERFROMCLASSINFO(userInfo,info);
            List<StudentClassRelation> sclist=srelationRepository.findAllByClassId(info.getId());//查询班级所有学生
            classi.setStudent(sclist.size());
            classModels.add(classi);
        }*/
        classModels=classInfoMapper.getPageByTeacherId(userInfo.getUserId(),(pageNum-1)*pageSize,pageSize);
        for (ClassModel info:classModels) {
            List<StudentClassRelation> sclist=srelationRepository.findAllByClassId(info.getClassId());//查询班级所有学生
            info.setStudent(sclist.size());
            classModels2.add(info);
        }
        PageInfo<ClassModel> pageInfo=new PageInfo<ClassModel>();
        pageInfo.setTotal(classModels2.size());
        pageInfo.setList(classModels2);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages((classModels2.size()+pageSize-1)/pageSize);
        pageInfo.setSize(pageSize);
         return pageInfo;
    }

    /**
     * 获取所有班级
     * @param userInfo
     * @return
     * @throws TecherException
     */
    public List<TeacherAddClassModel> getAddClass(UserInfoForToken userInfo)throws TecherException {
        if (StringUtils.isEmpty(userInfo)){throw new TecherException(ResultCode.PARAM_MISS_MSG);}
        List<ClassRelation> classaId= techerClassRelationRepository.findByTecherId(userInfo.getUserId());//老师班级关系
        if (classaId.size()<0){throw new TecherException(ResultCode.SELECT_NULL_MSG); }
        List<String> classid=new ArrayList<String>();
        for (ClassRelation c:classaId) {
            classid.add(c.getClassId());
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        List<ClassInfo> classInfos=classInfoRepository.getClassInfoByIdIn(sort,classid);//老师已有班级
        if (classInfos.size()<0){throw new TecherException(ResultCode.SELECT_NULL_MSG);}
        TeacherInfo teacherInfo=teacherInfoRepository.findExsitById(userInfo.getUserId());
        List<ClassInfo> classInfosLiset=classInfoRepository.findBySchoolIdAndStatus(teacherInfo.getSchoolId(),"A");
        //List<ClassInfo> all=classInfoRepository.findAll();
        if (classInfosLiset.size()<=0){throw new TecherException(ResultCode.SELECT_NULL_MSG);}
        List<String> cid=new ArrayList<String>();
        List<TeacherAddClassModel> no=new ArrayList<TeacherAddClassModel>();//不属于老师
        TeacherAddClassModel model=null;
        for (ClassInfo c:classInfos ) {
            cid.add(c.getId());//老师已有列表
        }
        if (cid.size()<=0){throw new TecherException(ResultCode.SELECT_NULL_MSG);}
        for (ClassInfo cc:classInfosLiset) {////老师已有班级
            if (cid.contains(cc.getId())){//全部的班级id是否存在老师已有列表
                model=new TeacherAddClassModel();
                model.setClassId(cc.getId());
                model.setClassName(cc.getClassName());
                model.setSpeciality(cc.getSpeciality());
                model.setExist(true);
                no.add(model);
            }else {//不在老师已有列表的
                model=new TeacherAddClassModel();
                model.setClassId(cc.getId());
                model.setClassName(cc.getClassName());
                model.setSpeciality(cc.getSpeciality());
                model.setExist(false);
                no.add(model);
            }
        }
        return no;
    }
    /**
     * 老师新增自己班级()
     * @param userInfo
     * @param classId
     */
    public void addTeacherClass(UserInfoForToken userInfo,String[] classId)throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) ||classId.length<=0){throw new UserServiceException(ResultCode.PARAM_MISS_MSG);}
        for (int i=0;i<classId.length;i++) {
          Integer ok=techerClassRelationRepository.countByClassIdAndTecherId(classId[i],userInfo.getUserId());
          if (ok<=0){
              ClassRelation classRelation = new ClassRelation();
              classRelation.setId(UUID.randomUUID().toString().replaceAll("-", ""));
              classRelation.setClassId(classId[i]);//班级id
              classRelation.setTecherId(userInfo.getUserId());//老师id
             techerClassRelationRepository.save(classRelation);//班级老师关系表
              ClassInfo classInfo=classInfoRepository.findExistById(classId[i]);
            /*  //系统信息
              String messages="成功新增班级-----您新增了一个新班级，班级信息：专业-"+classInfo.getSpeciality()+"  班级-"+classInfo.getClassName();
              Message message=new Message(userInfo.getUserId(),userInfo.getUserName(),messages);
              messageRepository.save(message);*/
          }
        }
    }
    /**
     * 修改班级
     * @param userInfo
     * @param model
     * @throws UserServiceException
     */
    public void upadteClass(UserInfoForToken userInfo,ClassModel model) throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(model)){
            throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo info=classInfoRepository.findExistById(model.getClassId());
        info.setSpeciality(model.getSpecialty());
        info.setRuTime(model.getEnrollTime());
       info.setEducationalSystem(model.getEducationalSystem());
       info.setClassName(model.getClassName());
       classInfoRepository.save(info);
    }

    /**
     * 修改班级状态
     * @param userInfo
     * @param
     * @throws UserServiceException
     */
    public void upadteClassStatus(UserInfoForToken userInfo,String classId,String status) throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(classId) ||StringUtils.isEmpty(status)) {
            throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo info =classInfoRepository.findExistById(classId);
        info.setStatus(status);
        classInfoRepository.save(info);

       /* if (status.equalsIgnoreCase("A")){
            //系统信息
            String messages="启用-----您已启用班级的使用权限，班级信息：专业-"+info.getSpeciality()+"  班级-"+info.getClassName();
            Message message=new Message(userInfo.getUserId(),userInfo.getUserName(),messages);
            messageRepository.save(message);
        }else if (status.equalsIgnoreCase("B")){
            //系统信息
            String messages="停用-----您已停用班级的使用权限，班级信息：专业-"+info.getSpeciality()+"  班级-"+info.getClassName();
            Message message=new Message(userInfo.getUserId(),userInfo.getUserName(),messages);
            messageRepository.save(message);
        }*/
       /* ClassModel classModel=new ClassModel();
            classModel.setSpecialty(info.getSpeciality());
            classModel.setClassName(info.getClassName());
            classModel.setStatus(status);
            return classModel;*/
    }

    /**
     * 删除班级
     * @param userInfo
     * @param classId
     */
    public void deleteClass(UserInfoForToken userInfo,String classId)throws UserServiceException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(classId)) {
            throw  new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo info=new ClassInfo();
        info.setId(classId);
        classInfoRepository.delete(info);
    }


    /**
     * 将班级从老师班级中移走
     * @param userInfo
     * @param classId
     * @throws TecherException
     */
    public void removeClass(UserInfoForToken userInfo,String classId)throws TecherException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(classId)) {
            throw  new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo info=classInfoRepository.findExistById(classId);
        techerClassRelationRepository.deleteByClassIdAndAndTecherId(classId,userInfo.getUserId());

      /*  //系统信息
        String messages="删除-----您已将次班级从您的班级中移走，班级信息：专业-"+info.getSpeciality()+"  班级-"+info.getClassName();
        Message message=new Message(userInfo.getUserId(),userInfo.getUserName(),messages);
        messageRepository.save(message);*/

       /* ClassModel classModel=new ClassModel();
        classModel.setSpecialty(info.getSpeciality());
        classModel.setClassName(info.getClassName());
        classModel.setStatus(status);*/
    }

    /**
     * 查询全部
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    public List<ClassInfo> getAllClass(UserInfoForToken userInfo)throws UserServiceException {
        if (StringUtils.isEmpty(userInfo)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<ClassInfo> classInfos=classInfoRepository.findAll();
        return classInfos;
    }


}
