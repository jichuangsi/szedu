package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.ExamClassRelation;
import cn.com.szedu.entity.IntermediateTable.StudentClassRelation;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.StudentRankModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.*;
import cn.com.szedu.repository.IClassInfoRepository;
import cn.com.szedu.repository.IExamRepository;
import cn.com.szedu.repository.IStudentAnswerCollectionRepository;
import cn.com.szedu.repository.IStudentInfoRespository;
import cn.com.szedu.repository.IntermediateTableRepository.IClassExamRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherAchievementService {
    @Resource
    private IClassExamRelationRepository classExamRelationRepository;
    @Resource
    private IExamRepository examRepository;
    @Resource
    private IStudentClassRelationRepository studentClassRelationRepository;
    /*  @Resource
      private IStudentExamRelationRepository studentExamRelationRepository;*/
    @Resource
    private IStudentAnswerCollectionRepository studentAnswerCollectionRepository;//学生答案
    @Resource
    private IClassInfoMapper classInfoMapper;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private IStudentInfoRespository studentInfoRespository;


    /**
     * 根据班级查看考试
     *
     * @param userInfo
     * @param classId
     * @param pageNum1
     * @param pageSize1
     * @return
     * @throws TecherException
     */
    public Page<Exam> getExamByClass(UserInfoForToken userInfo, String classId, int pageNum1, int pageSize1) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(classId)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        int pageNum = pageNum1 == 0 ? 1 : pageNum1;
        int pageSize = pageSize1 == 0 ? 5 : pageSize1;
        List<ExamModel> elist = new ArrayList<ExamModel>();
        ExamModel model = null;
        Exam exam = null;
        List<ExamClassRelation> examClassRelation = classExamRelationRepository.findByClassId(classId);
        if (examClassRelation.size() <= 0) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }

       /* for (ExamClassRelation e:examClassRelation) {
            exam=examRepository.findFirstByid(e.getExamId());
            model=new ExamModel();
            model.setExamId(exam.getId());
            model.setExamName(exam.getExamName());
            elist.add(model);
        }*/

      /*  PageInfo<ExamModel> pageInfo=new PageInfo<ExamModel>();
        pageInfo.setList(elist);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(elist.size());
        return pageInfo;*/
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "startTime"));
        Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);
        Page<Exam> exam2 = examRepository.findAll((Root<Exam> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            Path<Object> path = root.get("id");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (ExamClassRelation e : examClassRelation) {
                in.value(e.getExamId());
            }
            if (examClassRelation.size() > 0) {
                predicateList.add(criteriaBuilder.and(criteriaBuilder.and(in)));
            }
          /* if(!StringUtils.isEmpty(mmodel.getSubjectId())){
               predicateList.add(criteriaBuilder.equal(root.get("subjectId").as(Integer.class),mmodel.getSubjectId()));
           }
           if(!StringUtils.isEmpty(mmodel.getLessionType())){
               predicateList.add(criteriaBuilder.equal(root.get("lessonTypeName").as(String.class),mmodel.getLessionType()));
           }
           if(mmodel.getTime()!=0){
               predicateList.add(criteriaBuilder.equal(root.get("startTime").as(long.class),mmodel.getTime()));
           }*/
            //predicateList.add(criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startTime"))));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        }, pageable);
        return exam2;
    }

    /**
     * 查询考试详情
     *
     * @param userInfo
     * @param examId
     * @return
     * @throws TecherException
     */
    public TeacherExamDetailModel getExamDetail(UserInfoForToken userInfo, String examId) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(examId)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        Exam exam = examRepository.findFirstByid(examId);
        TeacherExamDetailModel detailModel = MappingEntity3ModelCoverter.CONVERTERFROMBACKEXAMDETAIL(exam);
        return detailModel;
    }

    /**
     * 班级情况总览
     *
     * @param userInfo
     * @param examId
     * @return
     * @throws TecherException
     */
    public ClassExamModel getClassExam(UserInfoForToken userInfo, String examId) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(examId)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        ClassExamModel classExamModel = new ClassExamModel();
        ClassGradeDistributionModel classGradeDistributionModel = new ClassGradeDistributionModel();
        List<ClassGradeDistributionModel> cmodel = new ArrayList<ClassGradeDistributionModel>();
        Integer sum = 0;//应考人数
        Integer actual = 0;//实考人数
        Integer miss;//缺考人数
        List<String> examClass = new ArrayList<String>();//考试班级
        List<String> aName = new ArrayList<String>();//实考人
        List<ExamClassRelation> examClassRelation = classExamRelationRepository.findByExamId(examId);//根据考试查出班级
        //判断班级是否为空，将班级加入考试班级
        if (examClassRelation.size() <= 0) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }
        for (ExamClassRelation e : examClassRelation) {
            examClass.add(e.getClassId());
        }
        classExamModel.setExamClass(examClass);
        //根据班级查出学生
        List<StudentClassRelation> student = studentClassRelationRepository.findByClassIdIn(examClass);
        //记录应考人数
        classExamModel.setSum(student.size());
        //跟据学生和考试的关系表，根据考试查出实考学生人数
        // List<StudentAnswerCollection> sa=studentAnswerCollectionRepository.
        List<StudentAnswerCollection> sa = classInfoMapper.getStudentByExam(examId);
        //记录实考人数
        classExamModel.setActual(sa.size());
        //记录实考学生姓名
        for (StudentAnswerCollection s : sa) {
            aName.add(s.getStudentName());
        }
        classExamModel.setaName(aName);


        //记录缺考人数=应考人数-实考人数
        classExamModel.setMiss(student.size() - sa.size());
        //根据考试ID查询学生成绩
        Integer score = 0;//分数
         Integer count=0;//人数
        Map<String, Object> sumSorce = new HashMap<String, Object>();
        for (StudentClassRelation sc : student) {
            score = classInfoMapper.sumScoreExam(sc.getStudentId(), examId);
            if (score >= 90) {
               // classGradeDistributionModel.setRank("A");
                //获取分数占比
                Object object = sumSorce.get("90-100");
                if (null == object) {//分数段人数
                    sumSorce.put("90-100", 1);
                } else {
                    sumSorce.put("90-100", (int) object + 1);
                }
            } else if (score >= 80 && score < 90) {
                //classGradeDistributionModel.setRank("B");
                //获取分数占比
                Object object = sumSorce.get("80-90");
                if (null == object) {//分数段人数
                    sumSorce.put("80-90", 1);
                } else {
                    sumSorce.put("80-90", (int) object + 1);
                }
            } else if (score >= 70 && score < 80) {
                //classGradeDistributionModel.setRank("C");
                //获取分数占比
                Object object = sumSorce.get("70-80");
                if (null == object) {//分数段人数
                    sumSorce.put("70-80", 1);
                } else {
                    sumSorce.put("70-80", (int) object + 1);
                }
            } else if (score >= 60 && score < 70) {
                //classGradeDistributionModel.setRank("D");
                //获取分数占比
                Object object = sumSorce.get("60-70");
                if (null == object) {//分数段人数
                    sumSorce.put("60-70", 1);
                } else {
                    sumSorce.put("60-70", (int) object + 1);
                }
            } else if (score >= 0 && score < 60) {
               // classGradeDistributionModel.setRank("E");
                //获取分数占比
                Object object = sumSorce.get("0-60");
                if (null == object) {//分数段人数
                    sumSorce.put("0-60", 1);
                } else {
                    sumSorce.put("0-60", (int) object + 1);
                }
            }
        }
        Integer countt=0;
        for(String key:sumSorce.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
            countt+=(Integer)sumSorce.get(key);
        }
        double rate=0d;
        double rate2=0d;
        //保留两位数字
        BigDecimal bg = null;
        //按分数段记录人数，等级
        for(String key:sumSorce.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
            if (key.equalsIgnoreCase("90-100")) {
                rate=(Integer)sumSorce.get(key)/countt;
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
               classGradeDistributionModel.setRank("A");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2);
                cmodel.add(classGradeDistributionModel);
            } else  if (key.equalsIgnoreCase("80-90")) {
                rate=(Integer)sumSorce.get(key)/countt;
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                classGradeDistributionModel.setRank("B");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2);
                cmodel.add(classGradeDistributionModel);
            }else  if (key.equalsIgnoreCase("70-80")) {
                rate=(Integer)sumSorce.get(key)/countt;
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                classGradeDistributionModel.setRank("C");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2);
                cmodel.add(classGradeDistributionModel);
            } else if (key.equalsIgnoreCase("60-70")) {
                rate=(Integer)sumSorce.get(key)/countt;
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                classGradeDistributionModel.setRank("D");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2);
                cmodel.add(classGradeDistributionModel);
            } else  if (key.equalsIgnoreCase("0-60")) {
                rate=(Integer)sumSorce.get(key)/countt;
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                classGradeDistributionModel.setRank("E");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2);
                cmodel.add(classGradeDistributionModel);
            }
        }
        classExamModel.setModel(cmodel);
        //计算分数段比例(分数段总人数/总学生*总题目)

        //添加到classExamModel
        //ClassExamModel classExamModel=new ClassExamModel();
        return classExamModel;
    }
    /**
     * 班级成绩单
     *
     * @param userInfo
     * @param examId
     * @param classId
     * @param pageNum1
     * @param pageSize1
     * @return
     * @throws TecherException
     */
    public List<ExamClassResultModel> getExamClassResult(UserInfoForToken userInfo, String examId, String classId, int pageNum1, int pageSize1) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(examId) || StringUtils.isEmpty(classId)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        int pageNum = pageNum1 == 0 ? 1 : pageNum1;
        int pageSize = pageSize1 == 0 ? 5 : pageSize1;

        ExamClassResultModel examClassResultModel=new ExamClassResultModel();
        List<ExamClassResultModel> emodel=new ArrayList<ExamClassResultModel>();
        //根据考试ID查出考试总题目
        List<TopQuestions> questions = classInfoMapper.getQuestionByExam(examId);
        Exam exam=examRepository.findFirstByid(examId);
        //根据考试查出班级//根据考试和班级
        //ExamClassRelation examClassRelation=classExamRelationRepository.findByClassIdAndExamId(classId,examId);
        //循环班级
        //班级学生成绩以及排名
        List<StudentRankModel> sumScoreAsc=classInfoMapper.sumScoreCount(examId);
        Map<String,Integer> ss=new HashMap<String,Integer>();
        Integer count=0;
        Integer rank2=0;
        //Integer rank3=0;
        for (StudentRankModel s:sumScoreAsc){//学生排名
            count++;
            ss.put(s.getStudentId(),s.getRank());
            if (count==sumScoreAsc.size()){//最后
                rank2=s.getRank();
            }
        }
        //根据班级查出学生
        List<StudentClassRelation> scr = studentClassRelationRepository.findAllByClassId(classId);
        //根据学生答案表，根据学生考试ID查询学生答案
        Map<String,Integer>  studentScore=new HashMap<String,Integer>();
        Integer score=0;
        Integer scoreNotNull=0;
        Integer sum=0;
        BigDecimal bg = null;
        double rate=0d;
        double rate2=0d;
        StudentInfo studentInfo=null;
        for (StudentClassRelation sc:scr) {
            List<StudentAnswerCollection> sac=studentAnswerCollectionRepository.findByExamIdAndStudentId(examId,sc.getStudentId());
            Integer rank=ss.get(sc.getStudentId());
            if (sac.size()>0) {
                sum = classInfoMapper.sumScoreExam(sc.getStudentId(), examId);
                studentScore.put(sc.getStudentId(), sum);//学生分数
                //score = classInfoMapper.sumScore(sc.getStudentId(), examId);//总
                scoreNotNull = classInfoMapper.sumScoreNotNull(sc.getStudentId(), examId);//正确
                rate = scoreNotNull / sum;
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                studentInfo = studentInfoRespository.findFirstById(sc.getStudentId());
                examClassResultModel.setExamId(examId);
                examClassResultModel.setStudentID(studentInfo.getStudentId());
                examClassResultModel.setStdentName(studentInfo.getName());
                examClassResultModel.setAccuracyRate(rate2);//正确率
                examClassResultModel.setRanking(rank);//排名
                examClassResultModel.setResult(sum);//分数
                emodel.add(examClassResultModel);
            }else if (sac.size()<=0){//缺考
                //rank3++;
                studentInfo=studentInfoRespository.findFirstById(sc.getStudentId());
                examClassResultModel.setExamId(examId);
                examClassResultModel.setStudentID(studentInfo.getStudentId());
                examClassResultModel.setStdentName(studentInfo.getName());
                examClassResultModel.setAccuracyRate(0d);
                examClassResultModel.setRemarks("缺考");//备注
                examClassResultModel.setRanking(rank2++);
                examClassResultModel.setResult(sum);
                emodel.add(examClassResultModel);
            }
        }
        //循环学生考试答案表

        //对比班级学生数是否等于学生答案表(不等为缺考)

        //将缺考学生加入列表无排名级分数，备注为“缺考”

        //记录答对题数

        //记录答错题

        //根据学生和考试关系表查出学生考试信息（分数）

        //根据升序排序sumScoreCount
       // List<StudentAnswerCollection> sumScoreAsc=classInfoMapper.sumScoreAsc(examId);
        //List<StudentAnswerCollection> sumScoreAsc=classInfoMapper.sumScoreCount(examId);
        //循环升序排序表，记录排名

        //判断题目总数与学生答案表题目是否相等(不等标记为错误；缺=总-答案表)

        //计算正确率(正确题数/总题数)保留两位小数


        return emodel;
    }
    /**
     * 根据考试查询班级
     *
     * @param userInfo
     * @param examId
     * @return
     */
    public List<ClassModel> getClassByExamId(UserInfoForToken userInfo, String examId) {
        ClassModel model = new ClassModel();
        List<ClassModel> models = new ArrayList<ClassModel>();
        List<ExamClassRelation> examClassRelations = classExamRelationRepository.findByExamId(examId);
        for (ExamClassRelation ec : examClassRelations) {
            ClassInfo classInfo = classInfoRepository.findExistById(ec.getClassId());//查询班纳吉信息
            model.setClassId(classInfo.getId());
            model.setClassName(classInfo.getClassName());
            models.add(model);
        }
        return models;
    }

    /*public List<Object[]> findAllTotalScore() {
        String sql = "SELECT study_no,sum(score) AS total from score s ";
        javax.persistence.Query query = EntityManager.class.createNativeQuery(sql);
        return query.getResultList();
    }*/

   /* private Map<String, Long> rank(List<Object[]> objects) {
        long previousRank = 1;
        Integer total = 0;
        //记录排名的map，map<study_no，排名>
        Map<String, Long> rankMap = new HashMap<>();
        for (int i = 0; i < objects.size(); i++) {
            Object[] object = objects.get(i);
            //计算名次，相同分数排名一样
            if (total ==(Integer)object[1]) {
                rankMap.put(object[0].toString(), previousRank);
            } else {
                rankMap.put(object[0].toString(), i + 1L);
                total = (Integer) object[1];
                previousRank = i + 1;
            }
        }
        return rankMap;
    }*/
}
