package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.TestpaperQuestionRelation;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.QuestionsModelII;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TestSubjectModel;
import cn.com.szedu.repository.*;
import cn.com.szedu.repository.IntermediateTableRepository.ITestpaperQuestionRelationRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.domain.StorePath;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class SelfQuestionsService {

    @Resource
    private IKnowledgesRepository knowledgesRepository;
    @Resource
    private ISelfQuestionsRepository selfQuestionsRepository;
    @Resource
    private IQuestionTypeRepository questionTypeRepository;
    @Resource
    private IQuestionOptionsRepository questionOptionsRepository;
    @Resource
    private IFastDFSClientService fastDFSClientService;
    @Resource
    private MenuRepository menuRepository;
    @Resource
    private ITopQuestionsRepository topQuestionsRepository;
    @Resource
    private ITestpaperQuestionRelationRepository testpaperQuestionRelationRepository;

    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;


    public void addQuestion(QuestionsModelII model) {
        SelfQuestions questions1=selfQuestionsRepository.findByid(model.getId());
        if (!(questions1==null)){
            //model.setImg(questions1.getQuestionPic());
        }
        SelfQuestions questions=selfQuestionsRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMQUESTIONSMODELIITOSELFQUESTIONS(model));
        if(model.getOptions()!=null){
            model.getOptions().setQuestionId(questions.getId());
            questionOptionsRepository.save(model.getOptions());//选项
        }
        /*if (model.getKnowledges()!=null && model.getKnowledges().size()!=0){
            for (Knowledges k:model.getKnowledges()) {
                k.setQuestionId(questions.getId());
            }
            knowledgesRepository.saveAll(model.getKnowledges());//保存知识点
        }*/
    }

    //上传图片
    public Integer addQuestionImg(MultipartFile file)throws IOException {
        SelfQuestions questions=new SelfQuestions();
        //questions.setQuestionPic(file.getBytes());
        SelfQuestions questions1=selfQuestionsRepository.save(questions);
        return questions1.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestion(Integer id) {
        questionOptionsRepository.deleteByQuestionId(id);
        knowledgesRepository.deleteByQuestionId(id);
        selfQuestionsRepository.deleteByid(id);
    }

    /**
     * 获取全部题目
     * @param pageSize
     * @param pageNum
     * @return
     */
    public Page<SelfQuestions> getQuestion(Integer pageSize,Integer pageNum) {
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<SelfQuestions> page=selfQuestionsRepository.findAll((Root<SelfQuestions> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        //pageNum=pageNum-1;
        /*List<SelfQuestions> selfQuestions=selfQuestionsRepository.findAllByTeacherIdAndTypeOrderByCreateTime(userInfoForToken.getUserId(),questionType);
        PageInfo<SelfQuestions> pageInfo=new PageInfo<>();
        pageInfo.setList(selfQuestions);
        pageInfo.setTotal(selfQuestions.size());
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPages((selfQuestions.size() + pageSize - 1)/pageSize);*/
        return  page;
    }

    /**
     * 根据老师id查询题目
     * @param userInfoForToken
     * @param questionType
     * @param pageSize
     * @param pageNum
     * @return
     */
    public Page<SelfQuestions> getQuestionByTeacherId(UserInfoForToken userInfoForToken, String questionType, Integer pageSize, Integer pageNum) {
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<SelfQuestions> page=selfQuestionsRepository.findAll((Root<SelfQuestions> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteriaBuilder.equal(root.get("teacherId"),userInfoForToken.getUserId()));
            if(questionType!=null && questionType!=""){
                predicateList.add(criteriaBuilder.equal(root.get("type"),questionType));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return  page;
    }

    /**
     * 题目类型
     * @return
     */
    public List<QuestionType> getQuestionType() {
        return  questionTypeRepository.findAll();
    }

    /**
     * 上传题目图片
     * @param userInfoForToken
     * @param file
     * @param questionId
     * @return
     * @throws IOException
     * @throws Exception
     */
    public Integer addSelfQuestionContentImg(UserInfoForToken userInfoForToken,MultipartFile file,Integer questionId)throws IOException ,Exception{
        SelfQuestions questions=new SelfQuestions();
        if (!StringUtils.isEmpty(questionId)){
            questions=selfQuestionsRepository.findByid(questionId);
        }
        StorePath storePath= fastDFSClientService.upLoadFile(file);
        questions.setContentPic(storePath.getFullPath());
        questions.setTeacherId(userInfoForToken.getUserId());
        questions.setTeacherName(userInfoForToken.getUserName());
        SelfQuestions questions1=selfQuestionsRepository.save(questions);
        return questions1.getId();
    }

    /**
     * 本地上传题目图片
     * @param userInfoForToken
     * @param file
     * @param questionId
     * @return
     * @throws IOException
     */
    public Integer localUploadSelfQuestionContentImg(UserInfoForToken userInfoForToken,MultipartFile file,Integer questionId)throws IOException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        /*double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>2){
            throw new TecherException("图片过大！");
        }*/
        SelfQuestions questions=new SelfQuestions();
        if(!StringUtils.isEmpty(questionId)){
            questions=selfQuestionsRepository.findByid(questionId);
        }
        questions.setTeacherId(userInfoForToken.getUserId());
        questions.setTeacherName(userInfoForToken.getUserName());
        //重新生成文件名
        fileName =UUID.randomUUID()+suffixName;
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        questions.setContentPic(uri+fileName);
        SelfQuestions questions1=selfQuestionsRepository.save(questions);
        return questions1.getId();
    }

    /**
     * 上传选项图片
     * @param userInfoForToken
     * @param file
     * @param options
     * @param questionId
     * @return
     * @throws IOException
     * @throws Exception
     */
    public Integer addSelfQuestionImg(UserInfoForToken userInfoForToken,MultipartFile file,String options,Integer questionId)throws IOException ,Exception{
        SelfQuestions questions=new SelfQuestions();
        if (!StringUtils.isEmpty(questionId)){
            questions=selfQuestionsRepository.findByid(questionId);
        }
        StorePath storePath= fastDFSClientService.upLoadFile(file);
        if (!StringUtils.isEmpty(options)){
            if(options.equals("A")){
                questions.setAoptionPic(storePath.getFullPath());
            }else if(options.equals("B")){
                questions.setBoptionPic(storePath.getFullPath());
            }else if(options.equals("C")){
                questions.setCoptionPic(storePath.getFullPath());
            }else if(options.equals("D")){
                questions.setDoptionPic(storePath.getFullPath());
            }
        }
        questions.setTeacherId(userInfoForToken.getUserId());
        questions.setTeacherName(userInfoForToken.getUserName());
        SelfQuestions questions1=selfQuestionsRepository.save(questions);
        return questions1.getId();
    }

    /**
     * 本地上传选项图片
     * @param userInfoForToken
     * @param file
     * @param options
     * @param questionId
     * @return
     * @throws IOException
     */
    public Integer localUploadSelfQuestionImg(UserInfoForToken userInfoForToken,MultipartFile file,String options,Integer questionId)throws IOException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        /*double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>2){
            throw new TecherException("图片过大！");
        }*/
        SelfQuestions questions=new SelfQuestions();
        if(!StringUtils.isEmpty(questionId)){
            questions=selfQuestionsRepository.findByid(questionId);
        }
        questions.setTeacherId(userInfoForToken.getUserId());
        questions.setTeacherName(userInfoForToken.getUserName());
        //重新生成文件名
        fileName =UUID.randomUUID()+suffixName;
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        if (!StringUtils.isEmpty(options)){
            if(options.equals("A")){
                questions.setAoptionPic(uri+fileName);
            }else if(options.equals("B")){
                questions.setBoptionPic(uri+fileName);
            }else if(options.equals("C")){
                questions.setCoptionPic(uri+fileName);
            }else if(options.equals("D")){
                questions.setDoptionPic(uri+fileName);
            }
        }
        SelfQuestions questions1=selfQuestionsRepository.save(questions);
        return questions1.getId();
    }

    /**
     * 保存题目
     * @param model
     * @param userInfoForToken
     */
    public void saveQuestion(QuestionsModelII model,UserInfoForToken userInfoForToken) {
        SelfQuestions questions=new SelfQuestions();
        if (!StringUtils.isEmpty(model.getId())){
            questions=selfQuestionsRepository.findByid(model.getId());
        }
        questions.setContent(model.getTitle());
        questions.setType(model.getType());
        questions.setChapter(model.getChapter());
        questions.setAnswerDetail(model.getAnswerDetail());
        questions.setAnswer(model.getAnswer());
        questions.setSubject(model.getSubject());
        questions.setSubjectId(model.getSubjectId());
        questions.setAoption(model.getOptions().getA());
        questions.setBoption(model.getOptions().getB());
        questions.setCoption(model.getOptions().getC());
        questions.setDoption(model.getOptions().getD());
        questions.setTeacherId(userInfoForToken.getUserId());
        questions.setTeacherName(userInfoForToken.getUserName());
        selfQuestionsRepository.save(questions);
        /*SelfQuestions questions=selfQuestionsRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMQUESTIONSMODELIITOSELFQUESTIONS(model));
        if(model.getOptions()!=null){
            model.getOptions().setQuestionId(questions.getId());
            questionOptionsRepository.save(model.getOptions());//选项
        }
        if (model.getKnowledges()!=null && model.getKnowledges().size()!=0){
            for (Knowledges k:model.getKnowledges()) {
                k.setQuestionId(questions.getId());
            }
            knowledgesRepository.saveAll(model.getKnowledges());//保存知识点
        }*/
    }

    /**
     * 批量上传单选题目
     * @param file
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    public String saveExcelQuestions(MultipartFile file, UserInfoForToken userInfo) throws UserServiceException {
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".xls")){
            System.out.println("文件不是.xls类型");
        }
        try{
            // 得到这个excel表格对象
            HSSFWorkbook workbook=new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            List<String> errorRowNum=new ArrayList<>();
            List<SelfQuestions> questions=new ArrayList<>();
            // 得到这个excel表格的sheet数量
            int numberOfSheets=workbook.getNumberOfSheets();
            String questionTye="单选题";
            for (int i=0;i<numberOfSheets;i++){
                //得到sheet
                HSSFSheet sheet=workbook.getSheetAt(i);
                //得到sheet里的总行数
                int physicalNumberOfRows=sheet.getPhysicalNumberOfRows();
                for(int j=1;j<physicalNumberOfRows;j++){
                    HSSFRow row=sheet.getRow(j);
                    SelfQuestions questions1;
                    try {
                        questions1=saveRowQuestion(userInfo,row,questionTye);
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    questions.add(questions1);
                }
            }
            selfQuestionsRepository.saveAll(questions);
            return JSONObject.toJSONString(errorRowNum.toArray());
        }catch (IOException e){
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }

    /**
     * 批量上传多选题目
     * @param file
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    public String saveExcelMultipleChoiceQuestions(MultipartFile file, UserInfoForToken userInfo) throws UserServiceException {
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".xls")){
            System.out.println("文件不是.xls类型");
        }
        try{
            // 得到这个excel表格对象
            HSSFWorkbook workbook=new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            List<String> errorRowNum=new ArrayList<>();
            List<SelfQuestions> questions=new ArrayList<>();
            String questionTye="多选题";
            // 得到这个excel表格的sheet数量
            int numberOfSheets=workbook.getNumberOfSheets();
            for (int i=0;i<numberOfSheets;i++){
                //得到sheet
                HSSFSheet sheet=workbook.getSheetAt(i);
                //得到sheet里的总行数
                int physicalNumberOfRows=sheet.getPhysicalNumberOfRows();
                for(int j=1;j<physicalNumberOfRows;j++){
                    HSSFRow row=sheet.getRow(j);
                    SelfQuestions questions1;
                    try {
                        questions1=saveRowQuestion(userInfo,row,questionTye);
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    questions.add(questions1);
                }
            }
            selfQuestionsRepository.saveAll(questions);
            return JSONObject.toJSONString(errorRowNum.toArray());
        }catch (IOException e){
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }

    /**
     * 批量上传判断题目
     * @param file
     * @param userInfo
     * @return
     * @throws UserServiceException
     */
    public String saveExcelJudgementQuestions(MultipartFile file, UserInfoForToken userInfo) throws UserServiceException {
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".xls")){
            System.out.println("文件不是.xls类型");
        }
        try{
            // 得到这个excel表格对象
            HSSFWorkbook workbook=new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            List<String> errorRowNum=new ArrayList<>();
            List<SelfQuestions> questions=new ArrayList<>();
            // 得到这个excel表格的sheet数量
            int numberOfSheets=workbook.getNumberOfSheets();
            String questionTye="判断题";
            for (int i=0;i<numberOfSheets;i++){
                //得到sheet
                HSSFSheet sheet=workbook.getSheetAt(i);
                //得到sheet里的总行数
                int physicalNumberOfRows=sheet.getPhysicalNumberOfRows();
                for(int j=1;j<physicalNumberOfRows;j++){
                    HSSFRow row=sheet.getRow(j);
                    SelfQuestions questions1;
                    try {
                        questions1=saveRowJudgementQuestion(userInfo,row,questionTye);
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    questions.add(questions1);
                }
            }
            selfQuestionsRepository.saveAll(questions);
            return JSONObject.toJSONString(errorRowNum.toArray());
        }catch (IOException e){
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }

    /**
     * 判断题
     * @param userInfo
     * @param row
     * @param type
     * @return
     * @throws UserServiceException
     */
    private SelfQuestions saveRowJudgementQuestion(UserInfoForToken userInfo,HSSFRow row,String type) throws UserServiceException{
        SelfQuestions questions=new SelfQuestions();
        String subject = "";
        String content = "";
        String A = "";
        String B = "";
        String answer="";
        String answerDetail = "";
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        for (int i = 0; i < physicalNumberOfCells; i++) {
            switch (i) {
                case 0:
                    //subject = getCellStringByType(row.getCell(i));
                    content = getCellStringByType(row.getCell(i));
                    break;
                case 1:
                    //content = getCellStringByType(row.getCell(i));
                    A = getCellStringByType(row.getCell(i));
                    break;
                case 2:
                    B = getCellStringByType(row.getCell(i));
                    //A = getCellStringByType(row.getCell(i));
                    break;
                case 3:
                    answer = getCellStringByType(row.getCell(i));
                    //B = getCellStringByType(row.getCell(i));
                    break;
                case 4:
                    //answer = getCellStringByType(row.getCell(i));
                    answerDetail = getCellStringByType(row.getCell(i));
                    break;
                /*case 5:
                    answerDetail = getCellStringByType(row.getCell(i));
                    break;*/
                default:
                    break;
            }
        }
        questions.setAoption(A);
        questions.setBoption(B);
        Menu menu=menuRepository.findFirstByTitle(subject);
        if(menu==null){
            menu=menuRepository.findFirstByTitle("无");
        }
        questions.setSubjectId(menu.getId().toString());
        questions.setSubject(menu.getTitle());
        questions.setContent(content);
        questions.setAnswer(answer);
        questions.setAnswerDetail(answerDetail);
        questions.setTeacherId(userInfo.getUserId());
        questions.setTeacherName(userInfo.getUserName());
        questions.setType(type);
        return questions;
    }

    /**
     * 单选题
     * @param userInfo
     * @param row
     * @param type
     * @return
     * @throws UserServiceException
     */
    private SelfQuestions saveRowQuestion(UserInfoForToken userInfo,HSSFRow row,String type) throws UserServiceException{
        SelfQuestions questions=new SelfQuestions();
        String subject = "";
        String content = "";
        String A = "";
        String B = "";
        String C = "";
        String D = "";
        String answer="";
        String answerDetail = "";
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        for (int i = 0; i < physicalNumberOfCells; i++) {
            switch (i) {
                case 0:
                    //subject = getCellStringByType(row.getCell(i));
                    content = getCellStringByType(row.getCell(i));
                    break;
                case 1:
                    //content = getCellStringByType(row.getCell(i));
                    A = getCellStringByType(row.getCell(i));
                    break;
                case 2:
                    //A = getCellStringByType(row.getCell(i));
                    B = getCellStringByType(row.getCell(i));
                    break;
                case 3:
                    //B = getCellStringByType(row.getCell(i));
                    C = getCellStringByType(row.getCell(i));
                    break;
                case 4:
                    //C = getCellStringByType(row.getCell(i));
                    D = getCellStringByType(row.getCell(i));
                    break;
                case 5:
                    //D = getCellStringByType(row.getCell(i));
                    answer = getCellStringByType(row.getCell(i));
                    break;
                case 6:
                    //answer = getCellStringByType(row.getCell(i));
                    answerDetail = getCellStringByType(row.getCell(i));
                    break;
                /*case 7:
                    answerDetail = getCellStringByType(row.getCell(i));
                    break;*/
                default:
                    break;
            }
        }
        questions.setAoption(A);
        questions.setBoption(B);
        questions.setCoption(C);
        questions.setDoption(D);
        Menu menu=menuRepository.findFirstByTitle(subject);
        if(menu==null){
            menu=menuRepository.findFirstByTitle("无");
        }
        questions.setSubjectId(menu.getId().toString());
        questions.setSubject(menu.getTitle());
        questions.setContent(content);
        questions.setAnswer(answer);
        questions.setAnswerDetail(answerDetail);
        questions.setTeacherId(userInfo.getUserId());
        questions.setTeacherName(userInfo.getUserName());
        questions.setType(type);
        return questions;
    }

    private String getCellStringByType(HSSFCell cell) throws UserServiceException {
        if (null == cell) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
        }
        return "";
    }

    private String getCellNumber(HSSFCell cell) throws UserServiceException {
        if (null == cell) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        return NumberToTextConverter.toText(cell.getNumericCellValue());
    }

    private String getCellString(HSSFCell cell) throws UserServiceException {
        if (null == cell) {
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        return cell.getStringCellValue();
    }

    /**
     * 随机组卷
     * @param userInfo
     * @param subList
     * @param testType
     * @return
     */
    public void addExamination(UserInfoForToken userInfo,List<String> subList,List<String> chapterList,String testType,Integer testPaperId){
        List<Integer> questionsId=new ArrayList<>();
        if(testType.equalsIgnoreCase("1")){//使用精品题库组卷
            if(subList!=null&&subList.size()!=0){
                List<TopQuestions> topQuestions=randomQuestion(topQuestionsRepository.findAllBySubjectIdInAndType(subList,"单选题"),10);
                topQuestions.forEach(t ->{
                    questionsId.add(t.getId());
                });
                topQuestions=randomQuestion(topQuestionsRepository.findAllBySubjectIdInAndType(subList,"多选题"),10);
                topQuestions.forEach(t ->{
                    questionsId.add(t.getId());
                });
                topQuestions=randomQuestion(topQuestionsRepository.findAllBySubjectIdInAndType(subList,"判断题"),10);
                topQuestions.forEach(t ->{
                    questionsId.add(t.getId());
                });
            }
            if(chapterList!=null&&chapterList.size()!=0){//章节
                List<TopQuestions> topQuestions=randomQuestion(topQuestionsRepository.findAllByChapterInAndType(chapterList,"单选题"),10);
                topQuestions.forEach(t ->{
                    questionsId.add(t.getId());
                });
                topQuestions=randomQuestion(topQuestionsRepository.findAllByChapterInAndType(chapterList,"多选题"),10);
                topQuestions.forEach(t ->{
                    questionsId.add(t.getId());
                });
                topQuestions=randomQuestion(topQuestionsRepository.findAllByChapterInAndType(chapterList,"判断题"),10);
                topQuestions.forEach(t ->{
                    questionsId.add(t.getId());
                });
            }

        }else if(testType.equalsIgnoreCase("2")){//我的题库
            if(subList!=null&&subList.size()!=0){
                List<SelfQuestions> selfQuestions=randomSelfQuestion(selfQuestionsRepository.findByTeacherIdAndTypeAndSubjectIdIn(userInfo.getUserId(),"单选题",subList),10);
                selfQuestions.forEach(s->{
                    questionsId.add(s.getId());
                });
                selfQuestions=randomSelfQuestion(selfQuestionsRepository.findByTeacherIdAndTypeAndSubjectIdIn(userInfo.getUserId(),"多选题",subList),10);
                selfQuestions.forEach(s->{
                    questionsId.add(s.getId());
                });
                selfQuestions=randomSelfQuestion(selfQuestionsRepository.findByTeacherIdAndTypeAndSubjectIdIn(userInfo.getUserId(),"判断题",subList),10);
                selfQuestions.forEach(s->{
                    questionsId.add(s.getId());
                });
            }
            if(chapterList!=null&&chapterList.size()!=0){//章节
                List<SelfQuestions> selfQuestions=randomSelfQuestion(selfQuestionsRepository.findByTeacherIdAndTypeAndChapterIn(userInfo.getUserId(),"单选题",chapterList),10);
                selfQuestions.forEach(s->{
                    questionsId.add(s.getId());
                });
                selfQuestions=randomSelfQuestion(selfQuestionsRepository.findByTeacherIdAndTypeAndChapterIn(userInfo.getUserId(),"多选题",chapterList),10);
                selfQuestions.forEach(s->{
                    questionsId.add(s.getId());
                });
                selfQuestions=randomSelfQuestion(selfQuestionsRepository.findByTeacherIdAndTypeAndChapterIn(userInfo.getUserId(),"判断题",chapterList),10);
                selfQuestions.forEach(s->{
                    questionsId.add(s.getId());
                });
             }

        }
        List<TestpaperQuestionRelation> testpaperQuestionRelations=new ArrayList<>();
        questionsId.forEach(q->{
            TestpaperQuestionRelation relation=new TestpaperQuestionRelation(testPaperId,q);
            testpaperQuestionRelations.add(relation);
        });
        testpaperQuestionRelationRepository.saveAll(testpaperQuestionRelations);
    }

    /**
     * 老师题库随机选题
     * @param list
     * @param num
     * @return
     */
    public List<SelfQuestions> randomSelfQuestion(List<SelfQuestions> list,Integer num){
        List<SelfQuestions> selfQuestions=new ArrayList<>();
        for (int i=0;i<num-1;i++){
            Random random=new Random();
            if (list.size()==0){
                return selfQuestions;
            }
            int j=random.nextInt(list.size());
            SelfQuestions questions=list.get(j);
            selfQuestions.add(questions);
            list.remove(questions);
        }
        return selfQuestions;
    }

    /**
     * 精品题库随机选题
     * @param list
     * @param num
     * @return
     */
    public List<TopQuestions> randomQuestion(List<TopQuestions> list,Integer num){
        List<TopQuestions> topQuestions=new ArrayList<>();
        for (int i=0;i<num-1;i++){
            Random random=new Random();
            if (list.size()==0){
                return topQuestions;
            }
            int j=random.nextInt(list.size());
            TopQuestions questions=list.get(j);
            topQuestions.add(questions);
            list.remove(questions);
        }
        return topQuestions;
    }

    /**
     * 根据老师和科目查询题目数量
     * @param userInfoForToken
     * @param id
     * @return
     */
    public TestSubjectModel getTestSubjectModel(UserInfoForToken userInfoForToken,String id){
        TestSubjectModel model=new TestSubjectModel();
        model.setSingleChoice(selfQuestionsRepository.countBySubjectIdAndTeacherIdAndType(id,userInfoForToken.getUserId(),"单选题"));
        model.setMultipleChoice(selfQuestionsRepository.countBySubjectIdAndTeacherIdAndType(id,userInfoForToken.getUserId(),"多选题"));
        model.setJudgement(selfQuestionsRepository.countBySubjectIdAndTeacherIdAndType(id,userInfoForToken.getUserId(),"判断题题"));
        return model;
    }

    /**
     * 根据老师查询全部题目数量
     * @param userInfoForToken
     * @return
     */
    public List<TestSubjectModel> getAllTestSubjectModel(UserInfoForToken userInfoForToken){
        List<TestSubjectModel> modelList=new ArrayList<>();
        List<Menu> subject=menuRepository.findByPid(0);
        subject.forEach(s->{
            TestSubjectModel model=new TestSubjectModel();
            model.setId(s.getId());
            model.setName(s.getTitle());
            model.setSingleChoice(selfQuestionsRepository.countBySubjectIdAndTeacherIdAndType(s.getId().toString(),userInfoForToken.getUserId(),"单选题"));
            model.setMultipleChoice(selfQuestionsRepository.countBySubjectIdAndTeacherIdAndType(s.getId().toString(),userInfoForToken.getUserId(),"多选题"));
            model.setJudgement(selfQuestionsRepository.countBySubjectIdAndTeacherIdAndType(s.getId().toString(),userInfoForToken.getUserId(),"判断题题"));
            modelList.add(model);
        });
        return modelList;
    }
}
