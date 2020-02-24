package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.IClassInfoMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.*;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.StudentRankModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.student.AnswerSituationModel;
import cn.com.szedu.model.student.PerformanceAnalysisModel;
import cn.com.szedu.model.student.SingleChoiceQuestionModel;
import cn.com.szedu.model.teacher.*;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.IClassExamRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentClassRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IStudentExamRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ITestpaperQuestionRelationRepository;
import cn.com.szedu.util.MappingEntity3ModelCoverter;
import com.github.pagehelper.PageInfo;
import org.hibernate.boot.spi.InFlightMetadataCollector;
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
import java.util.*;

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
    @Resource
    private ITestPaperRepository testPaperRepository;
    @Resource
    private ITestpaperQuestionRelationRepository testpaperQuestionRelationRepository;
    @Resource
    private ISelfQuestionsRepository selfQuestionsRepository;
    @Resource
    private IQuestionTypeRepository questionTypeRepository;

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
        Integer passingNumber=0;//及格人数
        Double passingRate=0d;///及格率
        Double excellentRate;//优秀率
        Double avg=0d;//平均分
        Double max;//最高分
        Double min;//最低分
        List<String> examClass = new ArrayList<String>();//考试班级
        List<String> examClassName = new ArrayList<String>();//考试班级
        List<String> aName = new ArrayList<String>();//实考人
        List<String> aName1 = new ArrayList<String>();//实考人
        List<Integer> maxSorce=new ArrayList<Integer>();

        Exam exam=examRepository.findFirstByid(examId);
        classExamModel.setExamId(exam.getId());
        classExamModel.setExamName(exam.getExamName());
        classExamModel.setExamType(exam.getExamType());
        classExamModel.setStartTime(exam.getStartTime());
        classExamModel.setSubjectId(exam.getSubjectId());
        classExamModel.setSubjiectName(exam.getSubjectName());
        List<ExamClassRelation> examClassRelation = classExamRelationRepository.findByExamId(examId);//根据考试查出班级
        //判断班级是否为空，将班级加入考试班级
        if (examClassRelation.size() <= 0) {
            throw new TecherException(ResultCode.SELECT_NULL_MSG);
        }
        for (ExamClassRelation e : examClassRelation) {
            examClass.add(e.getClassId());
        }
        List<ClassInfo> classInfo=classInfoRepository.findByIdIn(examClass);
        for (ClassInfo c:classInfo){
            examClassName.add(c.getClassName());
        }
        classExamModel.setExamClass(examClassName);
        //根据班级查出学生
        List<StudentClassRelation> student = studentClassRelationRepository.findByClassIdIn(examClass);
        //记录应考人数
        classExamModel.setSum(student.size());
        //跟据学生和考试的关系表，根据考试查出实考学生人数
        // List<StudentAnswerCollection> sa=studentAnswerCollectionRepository.
        List<DistinctModel> sa = classInfoMapper.getStudentByExam(examId);
        //记录实考人数
        classExamModel.setActual(sa.size());
        //记录实考学生姓名
        for (DistinctModel s : sa) {
            //aName.add(s.getStudentName());
            aName.add(s.getStudentId());
        }
        List<StudentClassRelation> studentClass= studentClassRelationRepository.findByClassIdInAndStudentIdNotIn(examClass,aName);
        for (StudentClassRelation ss:studentClass){
            StudentInfo studentInfo=studentInfoRespository.findFirstByid(ss.getStudentId());
            if (studentInfo!=null) {
                aName1.add(studentInfo.getName());
            }
        }
        classExamModel.setaName(aName1);


        //记录缺考人数=应考人数-实考人数
        classExamModel.setMiss(student.size() - sa.size());
        //根据考试ID查询学生成绩
        Integer score = 0;//分数
        Integer count=0;//人数
        Map<String, Object> sumSorce = new HashMap<String, Object>();
        for (StudentClassRelation sc : student) {
            score = classInfoMapper.sumScoreExam(sc.getStudentId(), examId);
            if (score==null){
                score=0;
            }
            maxSorce.add(score);
            if (score >= 90) {
                passingNumber++;
                //获取分数占比
                Object object = sumSorce.get("90-100");
                if (null == object) {//分数段人数
                    sumSorce.put("90-100", 1);
                } else {
                    sumSorce.put("90-100", (int) object + 1);
                }
            } else if (score >= 80 && score < 90) {
                passingNumber++;
                //获取分数占比
                Object object = sumSorce.get("80-90");
                if (null == object) {//分数段人数
                    sumSorce.put("80-90", 1);
                } else {
                    sumSorce.put("80-90", (int) object + 1);
                }
            } else if (score >= 70 && score < 80) {
                passingNumber++;
                //获取分数占比
                Object object = sumSorce.get("70-80");
                if (null == object) {//分数段人数
                    sumSorce.put("70-80", 1);
                } else {
                    sumSorce.put("70-80", (int) object + 1);
                }
            } else if (score >= 60 && score < 70) {
                passingNumber++;
                //获取分数占比
                Object object = sumSorce.get("60-70");
                if (null == object) {//分数段人数
                    sumSorce.put("60-70", 1);
                } else {
                    sumSorce.put("60-70", (int) object + 1);
                }
            } else if (score >= 0 && score < 60) {
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
                classGradeDistributionModel=new ClassGradeDistributionModel();
                rate=Double.valueOf((Integer)sumSorce.get(key)/countt);
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                classGradeDistributionModel.setRank("A");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2*100);
                cmodel.add(classGradeDistributionModel);
            } else  if (key.equalsIgnoreCase("80-90")) {
                classGradeDistributionModel=new ClassGradeDistributionModel();
                rate=Double.valueOf((Integer)sumSorce.get(key)/countt);
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                classGradeDistributionModel.setRank("B");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2*100);
                cmodel.add(classGradeDistributionModel);
            }else  if (key.equalsIgnoreCase("70-80")) {
                classGradeDistributionModel=new ClassGradeDistributionModel();
                rate=Double.valueOf((Integer)sumSorce.get(key)/countt);
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                classGradeDistributionModel.setRank("C");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2*100);
                cmodel.add(classGradeDistributionModel);
            } else if (key.equalsIgnoreCase("60-70")) {
                classGradeDistributionModel=new ClassGradeDistributionModel();
                rate=Double.valueOf((Integer)sumSorce.get(key)/countt);
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                classGradeDistributionModel.setRank("D");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2*100);
                cmodel.add(classGradeDistributionModel);
            } else  if (key.equalsIgnoreCase("0-60")) {
                classGradeDistributionModel=new ClassGradeDistributionModel();
                rate=Double.valueOf((Integer)sumSorce.get(key)/countt);
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                classGradeDistributionModel.setRank("E");
                //获取分数占比
                classGradeDistributionModel.setCount((Integer)sumSorce.get(key));
                classGradeDistributionModel.setFractionSegment(key);
                classGradeDistributionModel.setRate(rate2*100);
                cmodel.add(classGradeDistributionModel);
            }
        }

        if ((Object)sumSorce.get("90-100")==null){
           // ClassGradeDistributionModel classGradeDistributionModel1=new ClassGradeDistributionModel();
            classGradeDistributionModel=new ClassGradeDistributionModel();
            classGradeDistributionModel.setRank("A");
            //获取分数占比
            classGradeDistributionModel.setCount(0);
            classGradeDistributionModel.setFractionSegment("90-100");
            classGradeDistributionModel.setRate(0d);
            cmodel.add(classGradeDistributionModel);
        }
        if ((Object)sumSorce.get("80-90")==null){
           // ClassGradeDistributionModel classGradeDistributionModel1=new ClassGradeDistributionModel();
            classGradeDistributionModel=new ClassGradeDistributionModel();
            classGradeDistributionModel.setRank("B");
            //获取分数占比
            classGradeDistributionModel.setCount(0);
            classGradeDistributionModel.setFractionSegment("80-90");
            classGradeDistributionModel.setRate(0d);
            cmodel.add(classGradeDistributionModel);
        }
        if ((Object)sumSorce.get("70-80")==null){
            classGradeDistributionModel=new ClassGradeDistributionModel();
            classGradeDistributionModel.setRank("C");
            //获取分数占比
            classGradeDistributionModel.setCount(0);
            classGradeDistributionModel.setFractionSegment("70-80");
            classGradeDistributionModel.setRate(0d);
            cmodel.add(classGradeDistributionModel);
        }
        if ((Object)sumSorce.get("60-70")==null){
            classGradeDistributionModel=new ClassGradeDistributionModel();
            classGradeDistributionModel.setRank("D");
            //获取分数占比
            classGradeDistributionModel.setCount(0);
            classGradeDistributionModel.setFractionSegment("60-70");
            classGradeDistributionModel.setRate(0d);
            cmodel.add(classGradeDistributionModel);
        }
        if ((Object)sumSorce.get("0-60")==null){
            classGradeDistributionModel=new ClassGradeDistributionModel();
            classGradeDistributionModel.setRank("E");
            //获取分数占比
            classGradeDistributionModel.setCount(0);
            classGradeDistributionModel.setFractionSegment("0-60");
            classGradeDistributionModel.setRate(0d);
            cmodel.add(classGradeDistributionModel);
        }
        max= Double.valueOf(Collections.max(maxSorce));
        min= Double.valueOf(Collections.min(maxSorce));
        double rate3=0d;double rate4=0d;
        if(passingNumber==0){
            rate3=0d;
        }else {
            passingRate=Double.valueOf(passingNumber)/Double.valueOf(sum);//及格率
            bg = new BigDecimal(passingRate);
            rate3 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        if ((Object)sumSorce.get("90-100")==null){
            rate4=0d;
        }else {
            excellentRate=Double.valueOf((Integer)sumSorce.get("90-100")/sum);
            bg = new BigDecimal(excellentRate);
            rate4 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        }
        classExamModel.setPassingNumber(passingNumber);
        classExamModel.setExcellentRate(rate4*100);
        classExamModel.setPassingRate(rate3*100);
        avg=classInfoMapper.avgScoreExam(examId);
        if (avg==null){
            avg=0d;
        }
        classExamModel.setAvg(avg);
        classExamModel.setMax(max);
        classExamModel.setMin(min);
        classExamModel.setModel(cmodel);
        //计算分数段比例(分数段总人数/总学生*总题目)

        //添加到classExamModel
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
        ClassInfo classInfo=classInfoRepository.findExistById(classId);
        int pageNum = pageNum1 == 0 ? 1 : pageNum1;
        int pageSize = pageSize1 == 0 ? 5 : pageSize1;

        /*  ExamClassResultModel examClassResultModel=new ExamClassResultModel();*/
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
        Integer rank3=1;
        for (StudentRankModel s:sumScoreAsc){//学生排名
            count++;
            ss.put(s.getStudentId(),rank3++);
            if (count==sumScoreAsc.size()){//最后
                rank2=rank3-1;
            }
        }
        //根据班级查出学生
        List<StudentClassRelation> scr = studentClassRelationRepository.findAllByClassId(classId);
        //根据学生答案表，根据学生考试ID查询学生答案
        Map<String,Integer>  studentScore=new HashMap<String,Integer>();
        Integer score=0;
        Integer scoreNotNull=0;
        Integer sum=0;
        Integer sum1=0;
        String grade="";
        BigDecimal bg = null;
        double rate=0d;
        double rate2=0d;
        StudentInfo studentInfo=null;
        for (StudentClassRelation sc:scr) {
            List<StudentAnswerCollection> sac=studentAnswerCollectionRepository.findByExamIdAndStudentId(examId,sc.getStudentId());
            Integer rank=ss.get(sc.getStudentId());
            if (sac.size()>0) {
                sum = classInfoMapper.sumRightExam(sc.getStudentId(), examId);
                sum1=classInfoMapper.sumScoreExam(sc.getStudentId(), examId);
                studentScore.put(sc.getStudentId(), sum);//学生分数
                //score = classInfoMapper.sumScore(sc.getStudentId(), examId);//总
                scoreNotNull = classInfoMapper.sumRightNotNull(sc.getStudentId(), examId);//正确
                rate =Double.valueOf(scoreNotNull)/Double.valueOf(sum);
                bg = new BigDecimal(rate);
                rate2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                /*if (rate2==1){
                    rate2=100;
                }else {
                    rate2=rate2*100;
                }*/

                if (sum1 >= 90) {
                    grade="A";
                } else if (score >= 80 && score < 90) {
                    grade="B";
                } else if (score >= 70 && score < 80) {
                    grade="C";
                } else if (score >= 60 && score < 70) {
                    grade="D";
                } else if (score >= 0 && score < 60) {
                    grade="E";
                }
                ExamClassResultModel examClassResultModel=new ExamClassResultModel();
                studentInfo = studentInfoRespository.findFirstById(sc.getStudentId());
                examClassResultModel.setExamId(examId);
                examClassResultModel.setStudentID(studentInfo.getStudentId());
                examClassResultModel.setStdentName(studentInfo.getName());
                examClassResultModel.setAccuracyRate(rate2*100);//正确率
                examClassResultModel.setRanking(rank);//排名
                examClassResultModel.setResult(sum1);//分数
                examClassResultModel.setClassName(classInfo.getClassName());
                examClassResultModel.setRank(grade);
                emodel.add(examClassResultModel);
            }else if (sac.size()<=0){//缺考
                //rank3++;
                ExamClassResultModel examClassResultModel=new ExamClassResultModel();
                studentInfo=studentInfoRespository.findFirstById(sc.getStudentId());
                examClassResultModel.setExamId(examId);
                examClassResultModel.setStudentID(studentInfo.getStudentId());
                examClassResultModel.setStdentName(studentInfo.getName());
                examClassResultModel.setAccuracyRate(0d);
                examClassResultModel.setRemarks("缺考");//备注
                examClassResultModel.setRanking(rank3);
                examClassResultModel.setResult(sum1);
                examClassResultModel.setRank("E");
                examClassResultModel.setClassName(classInfo.getClassName());
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

    /**
     * 学生考试成绩分析
     *
     * @param userInfo
     * @return
     */
   /* public List<PerformanceAnalysisModel> getStudnetExamAnaly(UserInfoForToken userInfo) throws TecherException{
        if (StringUtils.isEmpty(userInfo)){
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        PerformanceAnalysisModel model = new PerformanceAnalysisModel();
        List<PerformanceAnalysisModel> models = new ArrayList<PerformanceAnalysisModel>();
        List<ExamClassRelation> esr=new ArrayList<ExamClassRelation>();
        List<String> classId=new ArrayList<String>();
        List<String> examId=new ArrayList<String>();
        Map<String,Integer> examScore=new HashMap<String,Integer>();

        int max = 0;
        int min = 0;
        int value=0;
        Integer score2=0;
        Integer scoreNotNull=0;
        Integer sum=0;
        BigDecimal bg = null;
        double rate=0d;
        double rate2=0d;
        //查询学生的班级的考试
        List<StudentClassRelation> scr=studentClassRelationRepository.findByStudentId(userInfo.getUserId());
        if (scr.size()>0) {
            for (StudentClassRelation e : scr) {
                classId.add(e.getClassId());
            }
            esr = classExamRelationRepository.findByClassIdIn(classId);
            if (esr.size() > 0) {
                for (ExamClassRelation c : esr) {//正确率，等级
                    Exam exam = examRepository.findFirstByid(c.getExamId());
                    Integer score = classInfoMapper.sumScoreExam(userInfo.getUserId(), c.getExamId());
                    examScore.put(exam.getExamName(), score);

                    List<StudentAnswerCollection> sac = studentAnswerCollectionRepository.findByExamIdAndStudentId(c.getExamId(), userInfo.getUserId());
                    model.setExamType(exam.getExamType());//类型
                    if (sac.size() > 0) {
                        sum = classInfoMapper.sumScoreExam(userInfo.getUserId(), c.getExamId());

                        if (score >= 90) {
                            model.setRank("A");
                        } else if (score >= 80 && score < 90) {
                            model.setRank("B");
                        } else if (score >= 70 && score < 80) {
                            model.setRank("C");
                        } else if (score >= 60 && score < 70) {
                            model.setRank("D");
                        } else if (score >= 0 && score < 60) {
                            model.setRank("E");
                        }

                        scoreNotNull = classInfoMapper.sumScoreNotNull(userInfo.getUserId(), c.getExamId());//正确
                        rate = scoreNotNull / sum;
                        bg = new BigDecimal(rate);
                        rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                        model.setExamId(c.getExamId());
                        model.setExamName(exam.getExamName());
                        model.setAccuracyRate(rate2);
                        model.setResult(sum);//分数
                        model.setStartTime(exam.getStartTime());
                        model.setTerm(exam.getTerm());
                    }

                    for (String key : examScore.keySet()) {//最大值
                        value = examScore.get(key);
                        if (max < value) {
                            max = value;
                        }
                    }
                    for (String key : examScore.keySet()) {//最小值
                        value = examScore.get(key);
                        if (max > value) {
                            min = value;
                        }
                    }
                    model.setMaxScore(max);
                    model.setMinScore(min);

                    models.add(model);
                }
            }
        }

        return models;
    }
   */
    /**
     * 学生考试成绩分析
     *
     * @param userInfo
     * @return
     */
    public List<ScoreAnalysisModel> getStudnetExamAnaly(UserInfoForToken userInfo,String studentId) throws TecherException{
        if (StringUtils.isEmpty(userInfo)){
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        ScoreAnalysisModel smodel =new ScoreAnalysisModel();
        List<ScoreAnalysisModel> smodels = new ArrayList<ScoreAnalysisModel>();
        StudentInfo studentInfo=studentInfoRespository.findFirstByid(studentId);
        PerformanceAnalysisModel model1 = new PerformanceAnalysisModel();
        List<PerformanceAnalysisModel> models = new ArrayList<PerformanceAnalysisModel>();
        List<ExamClassRelation> esr=new ArrayList<ExamClassRelation>();
        List<String> classId=new ArrayList<String>();
        List<String> examId=new ArrayList<String>();
        Map<String,Integer> examScore=new HashMap<String,Integer>();
        //List<Double> maxSorce=new ArrayList<Double>();
        List<Integer> maxSorce=new ArrayList<Integer>();
        int max = 0;
        int min = 0;
       /* Double max1 = 0d;
        Double min1 = 0d;*/
        int value=0;
        Integer score2=0;
        Integer scoreNotNull=0;
        Integer sum=0;
        BigDecimal bg = null;
        double rate=0d;
        double rate2=0d;
        //查询学生的班级的考试
        List<StudentClassRelation> scr=studentClassRelationRepository.findByStudentId(studentId);
        //if (scr.size()>0) {
        for (StudentClassRelation e : scr) {
            classId.add(e.getClassId());
        }
        esr = classExamRelationRepository.findByClassIdIn(classId);
        if (esr.size() > 0) {
            for (ExamClassRelation c : esr) {//正确率，等级
                PerformanceAnalysisModel model = new PerformanceAnalysisModel();

                Exam exam = examRepository.findFirstByid(c.getExamId());
                Integer score = classInfoMapper.sumScoreExam(studentId, c.getExamId());
                //examScore.put(exam.getExamName(), score);
                //  maxSorce.add(Double.valueOf(score));
                if (score==null){
                    score=0;
                }
                maxSorce.add(score);
                List<StudentAnswerCollection> sac = studentAnswerCollectionRepository.findByExamIdAndStudentId(c.getExamId(), studentId);
                if (sac.size() > 0) {
                    model.setExamType(exam.getExamType());//类型
                    // sum = classInfoMapper.sumScoreExam(userInfo.getUserId(), c.getExamId());
                    sum = classInfoMapper.sumRightExam(studentId, c.getExamId());
                    if (score >= 90) {
                        model.setRank("A");
                    } else if (score >= 80 && score < 90) {
                        model.setRank("B");
                    } else if (score >= 70 && score < 80) {
                        model.setRank("C");
                    } else if (score >= 60 && score < 70) {
                        model.setRank("D");
                    } else if (score >= 0 && score < 60) {
                        model.setRank("E");
                    }
                    scoreNotNull = classInfoMapper.sumRightNotNull(studentId, c.getExamId());//正确
                    rate = Double.valueOf(scoreNotNull) / Double.valueOf(sum);
                    bg = new BigDecimal(rate);
                    rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                //scoreNotNull = classInfoMapper.sumScoreNotNull(studentId, c.getExamId());//正确

                model.setExamId(c.getExamId());
                model.setExamName(exam.getExamName());
                model.setAccuracyRate(rate2*100);
                model.setResult(sum);//分数
                model.setStartTime(exam.getStartTime());
                model.setTerm(exam.getTerm());
                           /* model.setStdentName(studentInfo.getName());
                             model.setStudentID(studentInfo.getStudentId());*/
                model.setSubjectId(exam.getSubjectId());
                model.setSubjectName(exam.getSubjectName());
                model.setStartTime(exam.getStartTime());
                model.setExamType(exam.getExamType());
                models.add(model);

            }

                       /* for (String key : examScore.keySet()) {//最大值
                            value = examScore.get(key);
                            if (max < value) {
                                max = value;
                            }
                        }
                        for (String key : examScore.keySet()) {//最小值
                            value = examScore.get(key);
                            if (max > value) {
                                min = value;
                            }
                        }*/
            max= Collections.max(maxSorce);
            min= Collections.min(maxSorce);
            smodel.setMaxScore(max);
            smodel.setMinScore(min);
            smodel.setStdentName(studentInfo.getName());
            smodel.setStudentID(studentInfo.getStudentId());
            smodel.setModels(models);
            smodels.add(smodel);
            // }
        }
        return smodels;
    }

    /**
     * 答题情况
     * @param examId
     * @param classId
     * @return
     * @throws TecherException
     */
   /* public List<AnswerSituationModel> getAnswerSitution(UserInfoForToken userInfo, String examId ,String classId)throws TecherException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(examId) ||StringUtils.isEmpty(classId)){
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        List<QuestionType> questionType=questionTypeRepository.findAll();

        SingleChoiceQuestionModel singleChoiceQuestionModel=null;
        List<SingleChoiceQuestionModel> singleChoiceQuestionModels=new ArrayList<SingleChoiceQuestionModel>();
        AnswerSituationModel model=new AnswerSituationModel();
        List<AnswerSituationModel> models=new ArrayList<AnswerSituationModel>();
        int truepeople=0;int falsepeople=0;int sumpeople=0; BigDecimal bg = null;double rate=0d; double rate2=0d;

        Exam exam=examRepository.findFirstByid(examId);
        Integer testpaper=Integer.parseInt(exam.getTestPaperId());
        TestPaper testPaper=testPaperRepository.findByid(testpaper);
        List<TestpaperQuestionRelation> tqr=testpaperQuestionRelationRepository.findByTestPaper(testPaper.getId());//题数
        List<Integer> id=new ArrayList<Integer>();
        List<Integer> sameid=new ArrayList<Integer>();
       SelfQuestions selfQuestions=null;
        if (tqr.size()>0){
            for (TestpaperQuestionRelation tq:tqr) {
                id.add(tq.getQuestionId());
                }
            for (QuestionType t:questionType) {
                //题型和题目
                List<SelfQuestions> selfQuestions1=selfQuestionsRepository.findAllByTypeidAndIdIn(String.valueOf(t.getId()),id);
              //题型
               QuestionType q = questionTypeRepository.findAllById(t.getId());
               for (SelfQuestions sq:selfQuestions1){
                   //同一题型的题目
                   sameid.add(sq.getId());
               }
               if (sameid.size()>0) {
                   List<StudentAnswerCollection> saw = studentAnswerCollectionRepository.findAllByExamIdAndQuestionIdIn(examId, sameid);

                   //正确
                   List<StudentAnswerCollection> saw1 = studentAnswerCollectionRepository.findAllByExamIdAndQuestionIdInAndIsTure(examId, sameid, "C");
                   if (saw.size() > 0) {
                       rate = saw1.size() / saw.size();
                       bg = new BigDecimal(rate);
                       rate2 = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                   } else {
                       rate2 = 0;
                   }
                   singleChoiceQuestionModel = new SingleChoiceQuestionModel();
                   singleChoiceQuestionModel.setExamId(examId);
                   singleChoiceQuestionModel.setExamName(exam.getExamName());
                   singleChoiceQuestionModel.setType(q.getType());
                   singleChoiceQuestionModel.setSubject(exam.getSubjectName());
                   singleChoiceQuestionModel.setSubjectId(exam.getSubjectId());
                   singleChoiceQuestionModel.setAccuracyRate(rate2);//正确率
                   singleChoiceQuestionModel.setTurepeople(saw1.size());//正确人数
                   singleChoiceQuestionModel.setFalsepeople(saw.size() - saw1.size());//错误人数
                   singleChoiceQuestionModels.add(singleChoiceQuestionModel);
               }
            }
            }

        model.setSingle(singleChoiceQuestionModels);
        models.add(model);


    return models;

    }
*/
    /**
     * 答题情况
     * @param examId
     * @param classId
     * @return
     * @throws TecherException
     */
    public List<AnswerSituationModel> getAnswerSitution(UserInfoForToken userInfo, String examId ,String classId)throws TecherException{
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(examId) ||StringUtils.isEmpty(classId)){
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        List<QuestionType> questionType=questionTypeRepository.findAll();

        SingleChoiceQuestionModel singleChoiceQuestionModel=null;
        List<SingleChoiceQuestionModel> singleChoiceQuestionModels=new ArrayList<SingleChoiceQuestionModel>();
        SingleChoiceQuestionModel multipleModel=null;
        List<SingleChoiceQuestionModel> multipleModels=new ArrayList<SingleChoiceQuestionModel>();
        SingleChoiceQuestionModel judgementModel=null;
        List<SingleChoiceQuestionModel> judgementModels=new ArrayList<SingleChoiceQuestionModel>();

        AnswerSituationModel model=new AnswerSituationModel();
        List<AnswerSituationModel> models=new ArrayList<AnswerSituationModel>();
        int truepeople=0;int falsepeople=0;int sumpeople=0; BigDecimal bg = null;double rate=0d; double rate2=0d;

        Exam exam=examRepository.findFirstByid(examId);
        Integer testpaper=Integer.parseInt(exam.getTestPaperId());
        TestPaper testPaper=testPaperRepository.findByid(testpaper);
        List<TestpaperQuestionRelation> tqr=testpaperQuestionRelationRepository.findByTestPaper(testPaper.getId());//题数
        List<Integer> id=new ArrayList<Integer>();//问题id
        List<Integer> sameid=new ArrayList<Integer>();
        SelfQuestions selfQuestions=null;
        if (tqr.size()>0){
            for (TestpaperQuestionRelation tq:tqr) {
                id.add(tq.getQuestionId());
            }

            for (QuestionType t:questionType) {

                //for (int i=0;i<questionType.size();i++){
                //题型和题目
                List<SelfQuestions> selfQuestions1 = selfQuestionsRepository.findAllByTypeidAndIdIn(String.valueOf(t.getId()), id);
                if (selfQuestions1.size() > 0) {
                    //题型
                    QuestionType q = questionTypeRepository.findAllById(t.getId());
                    for (SelfQuestions sq : selfQuestions1) {
                        //同一题型的题目
                        sameid.add(sq.getId());
                    }
                    if (sameid.size() > 0) {
                        for (SelfQuestions sq : selfQuestions1) {
                       /* List<StudentAnswerCollection> saw = studentAnswerCollectionRepository.findAllByExamIdAndQuestionIdIn(examId, sameid);

                        //正确
                        List<StudentAnswerCollection> saw1 = studentAnswerCollectionRepository.findAllByExamIdAndQuestionIdInAndIsTure(examId, sameid, "C");
                       */
                            List<StudentAnswerCollection> saw = studentAnswerCollectionRepository.findByExamIdAndQuestionId(examId, sq.getId());

                            //正确
                            List<StudentAnswerCollection> saw1 = studentAnswerCollectionRepository.findByExamIdAndQuestionIdAndIsTure(examId, sq.getId(), "C");

                            if (saw.size() > 0) {
                                rate = Double.valueOf(saw1.size()) / Double.valueOf(saw.size());
                                bg = new BigDecimal(rate);
                                rate2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            } else {
                                rate2 = 0;
                            }

                            if (q.getType().equalsIgnoreCase("单选题")) {
                                singleChoiceQuestionModel = new SingleChoiceQuestionModel();
                                singleChoiceQuestionModel.setExamId(examId);
                                singleChoiceQuestionModel.setExamName(exam.getExamName());
                                singleChoiceQuestionModel.setType(q.getType());
                                singleChoiceQuestionModel.setSubject(exam.getSubjectName());
                                singleChoiceQuestionModel.setSubjectId(exam.getSubjectId());
                                singleChoiceQuestionModel.setAccuracyRate(rate2 * 100);//正确率
                                singleChoiceQuestionModel.setTurepeople(saw1.size());//正确人数
                                singleChoiceQuestionModel.setFalsepeople(saw.size() - saw1.size());//错误人数
                                singleChoiceQuestionModels.add(singleChoiceQuestionModel);
                            } else if (q.getType().equalsIgnoreCase("多选题")) {
                                multipleModel = new SingleChoiceQuestionModel();
                                multipleModel.setExamId(examId);
                                multipleModel.setExamName(exam.getExamName());
                                multipleModel.setType(q.getType());
                                multipleModel.setSubject(exam.getSubjectName());
                                multipleModel.setSubjectId(exam.getSubjectId());
                                multipleModel.setAccuracyRate(rate2);//正确率
                                multipleModel.setTurepeople(saw1.size());//正确人数
                                multipleModel.setFalsepeople(saw.size() - saw1.size());//错误人数
                                multipleModels.add(multipleModel);
                            } else if (q.getType().equalsIgnoreCase("判断题")) {
                                judgementModel = new SingleChoiceQuestionModel();
                                judgementModel.setExamId(examId);
                                judgementModel.setExamName(exam.getExamName());
                                judgementModel.setType(q.getType());
                                judgementModel.setSubject(exam.getSubjectName());
                                judgementModel.setSubjectId(exam.getSubjectId());
                                judgementModel.setAccuracyRate(rate2);//正确率
                                judgementModel.setTurepeople(saw1.size());//正确人数
                                judgementModel.setFalsepeople(saw.size() - saw1.size());//错误人数
                                judgementModels.add(judgementModel);
                            }

                        }
                    }
                }
            }
        }

        model.setSingle(singleChoiceQuestionModels);
        model.setMultiple(multipleModels);
        model.setJudgement(judgementModels);
        models.add(model);


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

   public TestPaperModel getExam(UserInfoForToken userInfo,String examId)throws TecherException{
       if (StringUtils.isEmpty(userInfo) ||StringUtils.isEmpty(examId)){
           throw new TecherException(ResultCode.PARAM_MISS_MSG);
       }
       Exam exam=examRepository.findFirstByid(examId);
       if (StringUtils.isEmpty(exam.getTestPaperId())){
           throw new TecherException(ResultCode.PARAM_MISS);
       }
       TestPaper testPaper=testPaperRepository.findByid(Integer.parseInt(exam.getTestPaperId()));
       TestPaperModel model=new TestPaperModel();
       model.setCreateTime(testPaper.getCreateTime());
       model.setGrade(testPaper.getGrade());
       model.setId(testPaper.getId());
       model.setRemark(testPaper.getRemark());
       model.setTeacherId(testPaper.getTeacherId());
       model.setTeacherName(testPaper.getTeacherName());
       model.setTestPaperName(testPaper.getTestPaperName());
       model.setType(testPaper.getType());
       model.setUpdateTime(testPaper.getUpdateTime());
       model.setTestTimeLength(exam.getTestTimeLength());
       List<Integer> questionIds=new ArrayList<>();
       testpaperQuestionRelationRepository.findByTestPaper(testPaper.getId()).forEach(t->{
           questionIds.add(t.getQuestionId());
       });
       List<SelfQuestions> selfQuestions=selfQuestionsRepository.findByidIn(questionIds);
       selfQuestions.forEach(selfQuestions1->{
           selfQuestions1.setAnswer(selfQuestions1.getAnswer());
           selfQuestions1.setAnswerDetail(selfQuestions1.getAnswerDetail());
       });
       model.setQuestionsModels(selfQuestions);
       return model;
   }

}
