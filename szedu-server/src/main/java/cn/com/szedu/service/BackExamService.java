package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.ICourseWareMapper;
import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.entity.Exam;
import cn.com.szedu.entity.IntermediateTable.ExamClassRelation;
import cn.com.szedu.entity.IntermediateTable.ExamUserRelation;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.entity.StudentAnswerCollection;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.ExamModel;
import cn.com.szedu.repository.IExamRepository;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.IStudentAnswerCollectionRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IExamClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IExamUserRelationRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackExamService {
    @Resource
    private IExamRepository examRepository;

    @Resource
    private ICourseWareMapper courseWareMapper;

    @Resource
    private IOpLogRepository opLogRepository;

    @Resource
    private IExamUserRelationRepository examUserRelationRepository;

    @Resource
    private IExamClassRelationRepository examClassRelationRepository;

    @Resource
    private IStudentAnswerCollectionRepository studentAnswerCollectionRepository;

    //添加考试
    @Transactional(rollbackFor = Exception.class)
    public void addExam(UserInfoForToken userInfo, Exam exam) throws UserServiceException {
        /*if (StringUtils.isEmpty(exam.getExamName())|| StringUtils.isEmpty(exam.getStartTime())
                || StringUtils.isEmpty(exam.getCourse()) || StringUtils.isEmpty(exam.getCreatorId())
                || StringUtils.isEmpty(exam.getTestTimeLength())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Exam exam1= examRepository.save(exam);
        //插入中间表
        ExamUserRelation relation=new ExamUserRelation(exam1.getId(),exam1.getCreatorId());
        examUserRelationRepository.save(relation);
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加考试");
        opLogRepository.save(opLog);*/
    }

    //添加考试
    @Transactional(rollbackFor = Exception.class)
    public void saveExam(UserInfoForToken userInfo, ExamModel model) throws UserServiceException {
        Exam exam=examRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMEXAMMODELTOEXAM(model));
        List<ExamClassRelation> relations=new ArrayList<>();
        model.getClassId().forEach(c ->{
            ExamClassRelation relation=new ExamClassRelation(exam.getId(),c);
            relations.add(relation);
        });
        examClassRelationRepository.saveAll(relations);
    }

    //修改考试信息
    public void updateExam(Exam exam) throws UserServiceException {
        /*if (StringUtils.isEmpty(exam.getExamName())|| StringUtils.isEmpty(exam.getStartTime())
                || StringUtils.isEmpty(exam.getCourse()) || StringUtils.isEmpty(exam.getTestTimeLength())
                || StringUtils.isEmpty(exam.getId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Exam exam1=examRepository.findFirstByid(exam.getId());
        exam1.setExamName(exam.getExamName());
        exam1.setCourse(exam.getCourse());
        exam1.setStartTime(exam.getStartTime());
        exam1.setTestTimeLength(exam.getTestTimeLength());
        examRepository.save(exam1);*/
    }

    //删除考试信息
    public void deleteExam(UserInfoForToken userInfo,String id) throws UserServiceException {
        if (StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        examRepository.deleteById(id);
        OpLog opLog=new OpLog(userInfo.getUserName(),"删除","删除考试");
        opLogRepository.save(opLog);
    }


    //考试列表
    public PageInfo<Exam> getExamList(String name,Integer pageNum,Integer pageSize){
        List<Exam> examList=null;
        if (!(StringUtils.isEmpty(name))){
            examList=courseWareMapper.getAllExamByname("%"+name+"%");
        }else {
            examList=courseWareMapper.getAllExam();
        }
        PageInfo page=new PageInfo();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        page.setTotal(examList.size());
        page.setList(examList);
        return page;
    }

    //保存学生回答答案
    public void saveStudentAnswer(UserInfoForToken userInfo, List<StudentAnswerCollection> collections) throws UserServiceException {
        studentAnswerCollectionRepository.saveAll(collections);
    }

    //我的考试
    public Page<Exam> getExamListByTeacher(UserInfoForToken userInfo,String name,String subjectId,String examType, Integer pageNum, Integer pageSize){
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<Exam> exams=examRepository.findAll((Root<Exam> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("creatorId"),userInfo.getUserId()));
            if(!StringUtils.isEmpty(name)){
                predicateList.add(criteriaBuilder.equal(root.get("name"),name));
            }
            if(!StringUtils.isEmpty(subjectId)){
                predicateList.add(criteriaBuilder.equal(root.get("subjectId"),subjectId));
            }
            if(!StringUtils.isEmpty(examType)){
                predicateList.add(criteriaBuilder.equal(root.get("examType"),examType));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return exams;
    }

    //考试详情
    public ExamModel getExamByExamId(UserInfoForToken userInfo,String examId){
        Exam exam=examRepository.findFirstByid(examId);
        ExamModel model=MappingEntity2ModelCoverter.CONVERTERFROMEXAMTOEXAMMODEL(exam);
        List<ExamClassRelation> relation=examClassRelationRepository.findByExamId(examId);
        List<String> classIds=new ArrayList<>();
        relation.forEach(examClassRelation -> {
            classIds.add(examClassRelation.getClassId());
        });
        //查找班级(显示)
        model.setClassId(classIds);
        return model;
    }


}
