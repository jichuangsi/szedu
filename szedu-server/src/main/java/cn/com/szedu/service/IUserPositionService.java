package cn.com.szedu.service;

import cn.com.szedu.constant.Status;
import cn.com.szedu.dao.mapper.IUserPositionMapper;
import cn.com.szedu.entity.UserPosition;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IUserPositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IUserPositionService {
    @Resource
    private IUserPositionRepository iUserPositionRepository;
    @Resource
    private IUserPositionMapper userPositionMapper;
    /*@Resource
    private ISchoolClassService schoolClassService;

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;
    @Resource
    private IPhraseInfoRepository phraseInfoRepository;*/

    //添加
    /*@Transactional(rollbackFor = Exception.class)
    public void insertUserPosition(UserInfoForToken userInfoForToken, TeacherModel model){
        if(null!=model.getPrimarySubject()){
            UserPosition userPosition=new UserPosition();
            userPosition.setUserid(model.getId());
            userPosition.setStatus("1");
            userPosition.setUsername(model.getName());
            userPosition.setSubjectid(model.getPrimarySubject().getSubjectId());
            userPosition.setSubjectname(model.getPrimarySubject().getSubjectName());
            iUserPositionRepository.save(userPosition);
        }
        if (null != model.getSecondarySubjects() || model.getSecondarySubjects().size()!=0) {
            model.getSecondarySubjects().forEach(subject -> {
                UserPosition userPosition=new UserPosition();
                userPosition.setUserid(model.getId());
                userPosition.setStatus("1");
                userPosition.setUsername(model.getName());
                userPosition.setId("");
                userPosition.setSubjectid(subject.getSubjectId());
                userPosition.setSubjectname(subject.getSubjectName());
                iUserPositionRepository.save(userPosition);
            });
        }
        if (null==model.getPrimarySubject() && null == model.getSecondarySubjects() || model.getSecondarySubjects().size()==0 ) {
                UserPosition userPosition=new UserPosition();
                userPosition.setUserid(model.getId());
                userPosition.setStatus("1");
                userPosition.setUsername(model.getName());
                userPosition.setId("");
                iUserPositionRepository.save(userPosition);
        }
    }*/

    //添加老师
   /* @Transactional(rollbackFor = Exception.class)
    public void addUserPosition(UserInfoForToken userInfoForToken, TeacherInsertModel model, String teacherId)throws ClassServiceException {
        TransferTeacher teacher= userInfoService.getTeacherById(teacherId);
        UserPosition userPosition=new UserPosition();
        userPosition.setUserid(teacherId);
        userPosition.setUsername(teacher.getTeacherName());
        userPosition.setStatus("0");
        if(null!=model.getPrimaryClassId()){
            userPosition.setClassid(model.getPrimaryClassId());
            ClassDetailModel classDetailModel=schoolClassService.getClassDetail(model.getPrimaryClassId());
            userPosition.setGradeid(classDetailModel.getGradeId());
        }
        if(null!=model.getSecondaryClassId()){
            ClassDetailModel classDetailModel=schoolClassService.getClassDetail(model.getSecondaryClassId());
            userPosition.setGradeid(classDetailModel.getGradeId());
            userPosition.setClassid(model.getSecondaryClassId());
        }
        if(null!=model.getSubjectId()){
            userPosition.setSubjectid(model.getSubjectId());
        }
        if(null!=model.getSubjectName()){
            userPosition.setSubjectname(model.getSubjectName());
        }
        iUserPositionRepository.save(userPosition);
    }*/
    //修改
    /*@Transactional(rollbackFor = Exception.class)
    public void updateUserPosition(UserInfoForToken userInfoForToken, TeacherModel model){
        iUserPositionRepository.deleteByuserid(model.getId());
        insertUserPosition(userInfoForToken,model);
    }*/
    //查找全部
    public List<UserPosition> getAllUserPosition(UserInfoForToken userInfoForToken){
        return iUserPositionRepository.findByStatus(Status.ACTIVATE.getName());
    }
    //根据老师id查询
    public List<UserPosition> getAUserPositionByUserId(String userid){
        return iUserPositionRepository.findByUserid(userid);
    }
    //根据科目年级id查询
    public List<UserPosition> getUserPositionBySubidAndGradeId(String subjectid, String gradeId){
        return iUserPositionRepository.findBySubjectidAndGradeid(subjectid,gradeId);
    }

    //根据用户id和班级id删除记录
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserPosition(UserInfoForToken userInfoForToken, String userId, String classId){
        iUserPositionRepository.deleteByuseridAndAndClassid(userId,classId);
    }

    //根据老师id修改老师状态
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserPositionById(String userId){
        userPositionMapper.updatePositionByuserid(userId);
    }

    //根据b班级id修改老师状态
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPositionByClassId(String classId){
        userPositionMapper.updatePositionByGradeId(classId,"1");
    }

    //根据b班级id修改老师状态
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserPositionByGradeId(List<String> gradeIds){
        iUserPositionRepository.deleteByGradeidIn(gradeIds);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserPositionByClassId(List<String> gids){
        iUserPositionRepository.deleteByGradeidIn(gids);
       /* List<UserPosition> positions=null;
            positions=iUserPositionRepository.findAll((Root<UserPosition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
                List<Predicate> predicateList = new ArrayList<>();
                if(classId!=null && classId!=""){
                    predicateList.add(criteriaBuilder.equal(root.get("classid"), classId));
                }
                if(subjectId!=null && subjectId!=""){
                    predicateList.add(criteriaBuilder.equal(root.get("subjectid"), subjectId));
                }
                if(gradeId!=null && gradeId!=""){
                    predicateList.add(criteriaBuilder.equal(root.get("gradeId"), gradeId));
                }
                if(schoolId!=null && schoolId!=""){
                    SchoolInfo info = schoolInfoRepository.findFirstById(schoolId);
                    List<PhraseInfo> phraseInfos = phraseInfoRepository.findByDeleteFlagAndIdIn("0", info.getPhraseIds());
                    predicateList.add(criteriaBuilder.equal(root.get("gradeId"), schoolId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            });*/
       /*List<UserPosition> positions=iUserPositionRepository.findByGradeidIn(gids);
        for (UserPosition position:positions) {
            position.setStatus("1");
            iUserPositionRepository.save(position);
            iUserPositionRepository.deleteByGradeidIn();
        }*/
    }

    public List<UserPosition> findUserById(UserInfoForToken userInfoForToken, String teacherId){
        return iUserPositionRepository.findByUserid(teacherId);
    }
}
