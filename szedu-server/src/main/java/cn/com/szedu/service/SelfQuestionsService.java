package cn.com.szedu.service;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.constant.Status;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.entity.QuestionType;
import cn.com.szedu.entity.SelfQuestions;
import cn.com.szedu.entity.UserInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.QuestionsModelII;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IKnowledgesRepository;
import cn.com.szedu.repository.IQuestionOptionsRepository;
import cn.com.szedu.repository.IQuestionTypeRepository;
import cn.com.szedu.repository.ISelfQuestionsRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    //根据id查询图片
   /* public byte[] getImgByQuestionId(Integer id){
        return selfQuestionsRepository.findByid(id).getQuestionPic();
    }*/

    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestion(Integer id) {
        questionOptionsRepository.deleteByQuestionId(id);
        knowledgesRepository.deleteByQuestionId(id);
        selfQuestionsRepository.deleteByid(id);
    }

    public PageInfo<SelfQuestions> getQuestion(Integer pageSize,Integer pageNum) {
        List<SelfQuestions> selfQuestions=selfQuestionsRepository.findAll();
        PageInfo<SelfQuestions> pageInfo=new PageInfo<>();
        pageInfo.setPageSize(selfQuestions.size());
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        pageInfo.setList(selfQuestions);
        return  pageInfo;
    }

    //题目类型
    public List<QuestionType> getQuestionType() {
        return  questionTypeRepository.findAll();
    }

    //上传题目图片
    public Integer addSelfQuestionContentImg(MultipartFile file,Integer questionId)throws IOException ,Exception{
        SelfQuestions questions=new SelfQuestions();
        if (!StringUtils.isEmpty(questionId)){
            questions=selfQuestionsRepository.findByid(questionId);
        }
        StorePath storePath= fastDFSClientService.upLoadFile(file);
        questions.setContentPic(storePath.getFullPath());
        SelfQuestions questions1=selfQuestionsRepository.save(questions);
        return questions1.getId();
    }

    //上传选项图片
    public Integer addSelfQuestionImg(MultipartFile file,String options,Integer questionId)throws IOException ,Exception{
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
            }else if(options.equals("E")){
                questions.setEoptionPic(storePath.getFullPath());
            }else if(options.equals("F")){
                questions.setFoptionPic(storePath.getFullPath());
            }else if(options.equals("ture")){
                questions.setTureOptionPic(storePath.getFullPath());
            }else if(options.equals("false")){
                questions.setFalseOptionPic(storePath.getFullPath());
            }
        }
        SelfQuestions questions1=selfQuestionsRepository.save(questions);
        return questions1.getId();
    }

    //保存题目
    public void saveQuestion(QuestionsModelII model,UserInfoForToken userInfoForToken) {
        SelfQuestions questions=new SelfQuestions();
        if (!StringUtils.isEmpty(model.getId())){
            questions=selfQuestionsRepository.findByid(model.getId());
        }
        questions.setContent(model.getTitle());
        questions.setType(model.getType());
        questions.setChapter(model.getChapter());
        questions.setSubject(model.getSubject());
        questions.setSubjectId(model.getSubjectId());
        questions.setAoption(model.getOptions().getA());
        questions.setBoption(model.getOptions().getB());
        questions.setCoption(model.getOptions().getC());
        questions.setDoption(model.getOptions().getD());
        questions.setEoption(model.getOptions().getE());
        questions.setFoption(model.getOptions().getF());
        questions.setTureOption(model.getOptions().getTureOption());
        questions.setFalseOption(model.getOptions().getFalseOption());
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

    //批量上传单选题目
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

    //批量上传多选题目
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

    //批量上传判断题目
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

    //判断题
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
                    subject = getCellStringByType(row.getCell(i));
                    break;
                case 1:
                    content = getCellStringByType(row.getCell(i));
                    break;
                case 2:
                    A = getCellStringByType(row.getCell(i));
                    break;
                case 3:
                    B = getCellStringByType(row.getCell(i));
                    break;
                case 4:
                    answer = getCellStringByType(row.getCell(i));
                    break;
                case 5:
                    answerDetail = getCellStringByType(row.getCell(i));
                    break;
                default:
                    break;
            }
        }
        questions.setAoption(A);
        questions.setAoption(B);
        questions.setSubject(subject);
        questions.setContent(content);
        questions.setAnswer(answer);
        questions.setAnswerDetail(answerDetail);
        questions.setTeacherId(userInfo.getUserId());
        questions.setTeacherName(userInfo.getUserName());
        questions.setType(type);
        return questions;
    }

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
                    subject = getCellStringByType(row.getCell(i));
                    break;
                case 1:
                    content = getCellStringByType(row.getCell(i));
                    break;
                case 2:
                    A = getCellStringByType(row.getCell(i));
                    break;
                case 3:
                    B = getCellStringByType(row.getCell(i));
                    break;
                case 4:
                    C = getCellStringByType(row.getCell(i));
                    break;
                case 5:
                    D = getCellStringByType(row.getCell(i));
                    break;
                case 6:
                    answer = getCellStringByType(row.getCell(i));
                    break;
                case 7:
                    answerDetail = getCellStringByType(row.getCell(i));
                    break;
                default:
                    break;
            }
        }
        questions.setAoption(A);
        questions.setAoption(B);
        questions.setAoption(C);
        questions.setAoption(D);
        questions.setSubject(subject);
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
}
