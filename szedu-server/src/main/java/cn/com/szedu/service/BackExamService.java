package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.ICourseWareMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.ExamClassRelation;
import cn.com.szedu.entity.IntermediateTable.ExamUserRelation;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.CourseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.student.StudentAnswerModel;
import cn.com.szedu.model.student.StudentAnswerModel2;
import cn.com.szedu.model.teacher.ExamModel;
import cn.com.szedu.model.teacher.StudentResultModel;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.IExamClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IExamUserRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static cn.com.szedu.service.TeacherLessonService.timeStamp2Date;

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

    @Resource
    private IClassInfoRepository classInfoRepository;

    @Resource
    private ISelfQuestionsRepository selfQuestionsRepository;

    @Resource
    private IStudentClassRelationRepository studentClassRelationRepository;

    @Resource
    private IStudentInfoRespository studentInfoRespository;

    @Resource
    private MessageRepository messageRepository;

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
        List<String> cf =model.getClassId();
        for (String cd:cf) {
            ExamClassRelation relation=new ExamClassRelation(exam.getId(),cd);
            relations.add(relation);
            examClassRelationRepository.save(relation);
        }
        /*model.getClassId().forEach(c ->{
            ExamClassRelation relation=new ExamClassRelation(exam.getId(),c);
            relations.add(relation);
            examClassRelationRepository.save(relation);
        });*/

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

    //我的考试
    public Page<Exam> getExamListByTeacher(UserInfoForToken userInfo,String name,String subjectId,String examType,int pageNum,int pageSize){
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
        /*List<ExamModel> examModels=new ArrayList<>();
        exams.forEach(exam -> {
            examModels.add(MappingEntity2ModelCoverter.CONVERTERFROMEXAMTOEXAMMODEL(exam));
        });*/
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
        Sort sort=new Sort(Sort.Direction.DESC,"createTime");
        model.setClassInfos(classInfoRepository.getClassInfoByIdIn(sort,classIds));
        return model;
    }

    /**
     * 修改考试状态(发布考试)
     * @param examId
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateExamStatus(UserInfoForToken userInfo,String examId,String status){
        Exam exam=examRepository.findFirstByid(examId);
        exam.setStatus(status);
        examRepository.save(exam);
        List<ExamClassRelation> examClassRelations=examClassRelationRepository.findByExamId(examId);
        List<String> classIds=new ArrayList<>();
        examClassRelations.forEach(examClassRelation -> {
            classIds.add(examClassRelation.getClassId());
        });
        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC, "createTime"));
        List<ClassInfo> classInfos=classInfoRepository.getClassInfoByIdIn(sort,classIds);
        classInfos.forEach(classInfo -> {
            String staertTime=timeStamp2Date(exam.getStartTime(),"yyyy-MM-dd HH:mm:ss");
            String message = userInfo.getUserName()+"刚发布了 1 门新的考试。考试信息：" + exam.getContent() + "；科目-" + exam.getSubjectName() +
                    "；开考时间-" +staertTime + "；考试时长-" + exam.getTestTimeLength() + " 分钟。" +
                    "同时，已向指定班级学生发送考试通知";
            Message message1 = new Message(userInfo.getUserId(), userInfo.getUserName(), message,"N");
            messageRepository.save(message1);
        });
    }


    /**
     * 根据学生获取考试
     * @param userInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Exam> getExamByStudentId(UserInfoForToken userInfo, Integer pageNum, Integer pageSize){
        //查询学生班级
       List<StudentClassRelation> studentClassRelations=studentClassRelationRepository.findByStudentId(userInfo.getUserId());
       List<String> classId=new ArrayList<>();
       studentClassRelations.forEach(studentClassRelation -> {
           classId.add(studentClassRelation.getClassId());
       });
       //班级拥有的考试
       List<ExamClassRelation> examClassRelations=examClassRelationRepository.findByClassIdIn(classId);
       List<String> examIds=new ArrayList<>();
       examClassRelations.forEach(examClassRelation -> {
           examIds.add(examClassRelation.getExamId());
       });
       List<Exam> examList=examRepository.getidInOrderByCreateTimeDesc(examIds,(pageNum-1)*pageSize,pageSize);
       examList.forEach(e->{//查询已答题考试
           if (!(e.getStatus().equalsIgnoreCase("4"))){//非结束状态
               if (studentAnswerCollectionRepository.countByStudentIdAndExamId(userInfo.getUserId(),e.getId())!=0){
                   e.setStatus("5");
               }
           }

       });
        PageInfo<Exam> page=new PageInfo<>();
        int count=examRepository.countByidIn(examIds);
        page.setTotal(count);
        page.setList(examList);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPages((count + pageSize - 1)/pageSize);
        return page;
    }

    /**
     * 保存学生答案
     * @param userInfo
     * @param model2
     * @throws UserServiceException
     */
   @Transactional(rollbackFor = Exception.class)
   public void saveStudentAnswer(UserInfoForToken userInfo, StudentAnswerModel2 model2) throws UserServiceException {
       if (studentAnswerCollectionRepository.countByStudentIdAndExamId(userInfo.getUserId(),model2.getExamId())!=0){
           throw new UserServiceException(ResultCode.TEST_IS_EXIST);
       }
       List<Integer> questionIds=new ArrayList<>();
       if (model2.getMultiple()!=null && model2.getMultiple().size()!=0){
           model2.getMultiple().forEach(m->{
               questionIds.add(m.getQuestionId());
           });
       }
       if (model2.getSingle()!=null && model2.getSingle().size()!=0){
           model2.getSingle().forEach(m->{
               questionIds.add(m.getQuestionId());
           });
       }
       if (model2.getJudge()!=null && model2.getJudge().size()!=0){
           model2.getJudge().forEach(m->{
               questionIds.add(m.getQuestionId());
           });
       }
        List<SelfQuestions> questions=selfQuestionsRepository.findByidIn(questionIds);
        List<StudentAnswerCollection> studentAnswerCollections=new ArrayList<>();
       if (model2.getMultiple()!=null && model2.getMultiple().size()!=0){//多选
           model2.getMultiple().forEach(m->{
               questions.forEach(questions1 -> {
                   if (m.getQuestionId()==questions1.getId()){
                       StudentAnswerCollection studentAnswerCollection=new StudentAnswerCollection();
                       if(StringUtils.isEmpty(m.getAnswer())){//未回答
                           studentAnswerCollection.setIsTure("P");
                           studentAnswerCollection.setScore(0);
                       }else {
                           if (m.getAnswer().contains(",")){
                               m.setAnswer(m.getAnswer().substring(m.getAnswer().lastIndexOf(",")-1));
                           }
                           if (m.getAnswer().equals(questions1.getAnswer())){//答案相等
                               studentAnswerCollection.setIsTure("C");
                               studentAnswerCollection.setScore(3);
                           }else {//回答错误
                               studentAnswerCollection.setIsTure("W");
                               studentAnswerCollection.setScore(0);
                           }
                       }
                       studentAnswerCollection.setQuestionId(m.getQuestionId());
                       studentAnswerCollection.setStudentAnswer(m.getAnswer());
                       studentAnswerCollection.setResult(questions1.getAnswer());
                       studentAnswerCollection.setExamId(model2.getExamId());
                       studentAnswerCollection.setStudentId(userInfo.getUserId());
                       studentAnswerCollection.setTestPaperId(model2.getTestPaperId());
                       studentAnswerCollection.setStudentName(userInfo.getUserName());
                       studentAnswerCollections.add(studentAnswerCollection);
                   }
               });
           });
       }
       if (model2.getSingle()!=null && model2.getSingle().size()!=0){//单选
           model2.getSingle().forEach(m->{
               questions.forEach(questions1 -> {
                   if (m.getQuestionId()==questions1.getId()){
                       StudentAnswerCollection studentAnswerCollection=new StudentAnswerCollection();
                       if(StringUtils.isEmpty(m.getAnswer())){//未回答
                           studentAnswerCollection.setIsTure("P");
                           studentAnswerCollection.setScore(0);
                       }else {
                           if (m.getAnswer().contains(",")){
                               m.setAnswer(m.getAnswer().substring(m.getAnswer().lastIndexOf(",")-1));
                           }
                           if (m.getAnswer().equals(questions1.getAnswer())){//答案相等
                               studentAnswerCollection.setIsTure("C");
                               studentAnswerCollection.setScore(2);
                           }else {//回答错误
                               studentAnswerCollection.setIsTure("W");
                               studentAnswerCollection.setScore(0);
                           }
                       }
                       studentAnswerCollection.setQuestionId(m.getQuestionId());
                       studentAnswerCollection.setStudentAnswer(m.getAnswer());
                       studentAnswerCollection.setResult(questions1.getAnswer());
                       studentAnswerCollection.setExamId(model2.getExamId());
                       studentAnswerCollection.setStudentId(userInfo.getUserId());
                       studentAnswerCollection.setTestPaperId(model2.getTestPaperId());
                       studentAnswerCollection.setStudentName(userInfo.getUserName());
                       studentAnswerCollections.add(studentAnswerCollection);
                   }
               });
           });
       }
       if (model2.getJudge()!=null && model2.getJudge().size()!=0){//判断
           model2.getJudge().forEach(m->{
               questions.forEach(questions1 -> {
                   if (m.getQuestionId()==questions1.getId()){
                       StudentAnswerCollection studentAnswerCollection=new StudentAnswerCollection();
                       if(StringUtils.isEmpty(m.getAnswer())){//未回答
                           studentAnswerCollection.setIsTure("P");
                           studentAnswerCollection.setScore(0);
                       }else {
                           if (m.getAnswer().contains(",")){
                               m.setAnswer(m.getAnswer().substring(m.getAnswer().lastIndexOf(",")-1));
                           }
                           if (m.getAnswer().equals(questions1.getAnswer())){//答案相等
                               studentAnswerCollection.setIsTure("C");
                               studentAnswerCollection.setScore(2);
                           }else {//回答错误
                               studentAnswerCollection.setIsTure("W");
                               studentAnswerCollection.setScore(0);
                           }
                       }
                       studentAnswerCollection.setQuestionId(m.getQuestionId());
                       studentAnswerCollection.setStudentAnswer(m.getAnswer());
                       studentAnswerCollection.setResult(questions1.getAnswer());
                       studentAnswerCollection.setExamId(model2.getExamId());
                       studentAnswerCollection.setStudentId(userInfo.getUserId());
                       studentAnswerCollection.setTestPaperId(model2.getTestPaperId());
                       studentAnswerCollection.setStudentName(userInfo.getUserName());
                       studentAnswerCollections.add(studentAnswerCollection);
                   }
               });
           });
       }
       studentAnswerCollectionRepository.saveAll(studentAnswerCollections);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveStudentResult(UserInfoForToken userInfo, StudentAnswerModel2 model2) throws UserServiceException {
        if (studentAnswerCollectionRepository.countByStudentIdAndExamId(userInfo.getUserId(),model2.getExamId())!=0){
            throw new UserServiceException(ResultCode.TEST_IS_EXIST);
        }
        List<Integer> questionIds=new ArrayList<>();
        if (model2.getMultiple()!=null && model2.getMultiple().size()!=0){
            model2.getMultiple().forEach(m->{
                questionIds.add(m.getQuestionId());
            });
        }
        if (model2.getSingle()!=null && model2.getSingle().size()!=0){
            model2.getSingle().forEach(m->{
                questionIds.add(m.getQuestionId());
            });
        }
        if (model2.getJudge()!=null && model2.getJudge().size()!=0){
            model2.getJudge().forEach(m->{
                questionIds.add(m.getQuestionId());
            });
        }
        List<SelfQuestions> questions=selfQuestionsRepository.findByidIn(questionIds);
        List<StudentAnswerCollection> studentAnswerCollections=new ArrayList<>();
        if (model2.getMultiple()!=null && model2.getMultiple().size()!=0){//多选
            model2.getMultiple().forEach(m->{
                questions.forEach(questions1 -> {
                    if (m.getQuestionId()==questions1.getId()){
                        StudentAnswerCollection studentAnswerCollection=new StudentAnswerCollection();
                        if(StringUtils.isEmpty(m.getAnswer())){//未回答
                            studentAnswerCollection.setIsTure("P");
                            studentAnswerCollection.setScore(0);
                        }else {
                            if (m.getAnswer().contains(",")){
                                m.setAnswer(m.getAnswer().substring(m.getAnswer().lastIndexOf(",")-1));
                            }
                            if (m.getAnswer().equals(questions1.getAnswer())){//答案相等
                                studentAnswerCollection.setIsTure("C");
                                studentAnswerCollection.setScore(3);
                            }else {//回答错误
                                studentAnswerCollection.setIsTure("W");
                                studentAnswerCollection.setScore(0);
                            }
                        }
                        studentAnswerCollection.setQuestionId(m.getQuestionId());
                        studentAnswerCollection.setStudentAnswer(m.getAnswer());
                        studentAnswerCollection.setResult(questions1.getAnswer());
                        studentAnswerCollection.setExamId(model2.getExamId());
                        studentAnswerCollection.setStudentId(userInfo.getUserId());
                        studentAnswerCollection.setTestPaperId(model2.getTestPaperId());
                        studentAnswerCollection.setStudentName(userInfo.getUserName());
                        studentAnswerCollections.add(studentAnswerCollection);
                    }
                });
            });
        }
        if (model2.getSingle()!=null && model2.getSingle().size()!=0){//单选
            model2.getSingle().forEach(m->{
                questions.forEach(questions1 -> {
                    if (m.getQuestionId()==questions1.getId()){
                        StudentAnswerCollection studentAnswerCollection=new StudentAnswerCollection();
                        if(StringUtils.isEmpty(m.getAnswer())){//未回答
                            studentAnswerCollection.setIsTure("P");
                            studentAnswerCollection.setScore(0);
                        }else {
                            if (m.getAnswer().contains(",")){
                                m.setAnswer(m.getAnswer().substring(m.getAnswer().lastIndexOf(",")-1));
                            }
                            if (m.getAnswer().equals(questions1.getAnswer())){//答案相等
                                studentAnswerCollection.setIsTure("C");
                                studentAnswerCollection.setScore(2);
                            }else {//回答错误
                                studentAnswerCollection.setIsTure("W");
                                studentAnswerCollection.setScore(0);
                            }
                        }
                        studentAnswerCollection.setQuestionId(m.getQuestionId());
                        studentAnswerCollection.setStudentAnswer(m.getAnswer());
                        studentAnswerCollection.setResult(questions1.getAnswer());
                        studentAnswerCollection.setExamId(model2.getExamId());
                        studentAnswerCollection.setStudentId(userInfo.getUserId());
                        studentAnswerCollection.setTestPaperId(model2.getTestPaperId());
                        studentAnswerCollection.setStudentName(userInfo.getUserName());
                        studentAnswerCollections.add(studentAnswerCollection);
                    }
                });
            });
        }
        if (model2.getJudge()!=null && model2.getJudge().size()!=0){//判断
            model2.getJudge().forEach(m->{
                questions.forEach(questions1 -> {
                    if (m.getQuestionId()==questions1.getId()){
                        StudentAnswerCollection studentAnswerCollection=new StudentAnswerCollection();
                        if(StringUtils.isEmpty(m.getAnswer())){//未回答
                            studentAnswerCollection.setIsTure("P");
                            studentAnswerCollection.setScore(0);
                        }else {
                            if (m.getAnswer().contains(",")){
                                m.setAnswer(m.getAnswer().substring(m.getAnswer().lastIndexOf(",")-1));
                            }
                            if (m.getAnswer().equals(questions1.getAnswer())){//答案相等
                                studentAnswerCollection.setIsTure("C");
                                studentAnswerCollection.setScore(2);
                            }else {//回答错误
                                studentAnswerCollection.setIsTure("W");
                                studentAnswerCollection.setScore(0);
                            }
                        }
                        studentAnswerCollection.setQuestionId(m.getQuestionId());
                        studentAnswerCollection.setStudentAnswer(m.getAnswer());
                        studentAnswerCollection.setResult(questions1.getAnswer());
                        studentAnswerCollection.setExamId(model2.getExamId());
                        studentAnswerCollection.setStudentId(userInfo.getUserId());
                        studentAnswerCollection.setTestPaperId(model2.getTestPaperId());
                        studentAnswerCollection.setStudentName(userInfo.getUserName());
                        studentAnswerCollections.add(studentAnswerCollection);
                    }
                });
            });
        }
        studentAnswerCollectionRepository.saveAll(studentAnswerCollections);
    }

    /**
     * 统计班级学生成绩
     * @param userInfo
     * @param classId
     * @param examId
     * @return
     */
    public List<StudentResultModel> settleStudentScoreByClass(UserInfoForToken userInfo,String classId,String examId){
        //查询学生
        List<StudentClassRelation> studentClassRelations=studentClassRelationRepository.findAllByClassId(classId);
        List<String> studentIds=new ArrayList<>();
        studentClassRelations.forEach(studentClassRelation -> {
            studentIds.add(studentClassRelation.getStudentId());
        });
        List<StudentInfo> studentInfos=studentInfoRespository.findByidIn(studentIds);
        List<StudentResultModel> studentResultModels=new ArrayList<>();
        StudentResultModel model =new StudentResultModel();
        for (StudentInfo studentInfo:studentInfos) {
            Integer totalScore=0;
            List<StudentAnswerCollection> studentAnswerCollection=studentAnswerCollectionRepository.findByExamIdAndStudentId(examId,studentInfo.getStudentId());
            model.setClassId(classId);
            model.setStudentId(studentInfo.getStudentId());
            model.setStudentName(studentInfo.getName());
            int right=0;
            int wrong=0;
            int rightQuestion=0;
            int wrongQuestion=0;
            for (StudentAnswerCollection sc:studentAnswerCollection) {
                totalScore+=sc.getScore();
                if (sc.getScore()>0){
                    right+=1;
                    rightQuestion+=1;
                }else{
                    wrong+=1;
                    wrongQuestion+=1;
                }
            }
            model.setResult(totalScore);
            if (rightQuestion==0){
                model.setAccuracyRate(0.0);
            }else {
                model.setAccuracyRate(Double.valueOf(rightQuestion/studentAnswerCollection.size()));
            }
            studentResultModels.add(model);
        }
        return studentResultModels;
    }
}
