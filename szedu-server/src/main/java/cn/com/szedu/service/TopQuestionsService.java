package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.Menu;
import cn.com.szedu.entity.QuestionType;
import cn.com.szedu.entity.SelfQuestions;
import cn.com.szedu.entity.TopQuestions;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.QuestionsModelII;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.*;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TopQuestionsService {//精品题库

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

    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;

    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestion(Integer id) {
        questionOptionsRepository.deleteByQuestionId(id);
        knowledgesRepository.deleteByQuestionId(id);
        selfQuestionsRepository.deleteByid(id);
    }

    //全部精品试题
    public PageInfo<TopQuestions> getTopQuestion(Integer pageSize,Integer pageNum) {
        List<TopQuestions> topQuestions=topQuestionsRepository.findAll();
        PageInfo<TopQuestions> pageInfo=new PageInfo<>();
        pageInfo.setPageSize(topQuestions.size());
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageNum);
        pageInfo.setList(topQuestions);
        return  pageInfo;
    }

    //题目类型
    public List<QuestionType> getQuestionType() {
        return  questionTypeRepository.findAll();
    }

    //上传精品题目图片
    public Integer addTopQuestionContentImg(UserInfoForToken userInfoForToken,MultipartFile file,Integer questionId)throws IOException ,Exception{
        TopQuestions questions=new TopQuestions();
        if (!StringUtils.isEmpty(questionId)){
            questions=topQuestionsRepository.findByid(questionId);
        }
        StorePath storePath= fastDFSClientService.upLoadFile(file);
        questions.setContentPic(storePath.getFullPath());
        questions.setCreaterId(userInfoForToken.getUserId());
        questions.setCreaterName(userInfoForToken.getUserName());
        TopQuestions questions1=topQuestionsRepository.save(questions);
        return questions1.getId();
    }

    /**
     * 本地上传精品题目图片
     * @param userInfoForToken
     * @param file
     * @param questionId
     * @return
     * @throws IOException
     */
    public Integer localUploadTopQuestionContentImg(UserInfoForToken userInfoForToken,MultipartFile file,Integer questionId)throws IOException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        /*double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>2){
            throw new TecherException("图片过大！");
        }*/
        TopQuestions questions=new TopQuestions();
        if(!StringUtils.isEmpty(questionId)){
            questions=topQuestionsRepository.findByid(questionId);
        }
        questions.setCreaterId(userInfoForToken.getUserId());
        questions.setCreaterName(userInfoForToken.getUserName());
        //重新生成文件名
        fileName =UUID.randomUUID()+suffixName;
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        questions.setContentPic(uri+fileName);
        TopQuestions questions1=topQuestionsRepository.save(questions);
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
    public Integer localUploadTopQuestionImg(UserInfoForToken userInfoForToken,MultipartFile file,String options,Integer questionId)throws IOException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        /*double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>2){
            throw new TecherException("图片过大！");
        }*/
        TopQuestions questions=new TopQuestions();
        if(!StringUtils.isEmpty(questionId)){
            questions=topQuestionsRepository.findByid(questionId);
        }
        questions.setCreaterId(userInfoForToken.getUserId());
        questions.setCreaterName(userInfoForToken.getUserName());
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
        TopQuestions questions1=topQuestionsRepository.save(questions);
        return questions1.getId();
    }

    //上传选项图片
    public Integer addTopQuestionImg(UserInfoForToken userInfoForToken,MultipartFile file,String options,Integer questionId)throws IOException ,Exception{
        TopQuestions questions=new TopQuestions();
        if (!StringUtils.isEmpty(questionId)){
            questions=topQuestionsRepository.findByid(questionId);
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
        questions.setCreaterId(userInfoForToken.getUserId());
        questions.setCreaterName(userInfoForToken.getUserName());
        TopQuestions questions1=topQuestionsRepository.save(questions);
        return questions1.getId();
    }

    //保存题目
    public void saveQuestion(QuestionsModelII model,UserInfoForToken userInfoForToken) {
        TopQuestions questions=new TopQuestions();
        if (!StringUtils.isEmpty(model.getId())){
            questions=topQuestionsRepository.findByid(model.getId());
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
        questions.setCreaterId(userInfoForToken.getUserId());
        questions.setCreaterName(userInfoForToken.getUserName());
        topQuestionsRepository.save(questions);
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
            List<TopQuestions> questions=new ArrayList<>();
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
                    TopQuestions questions1;
                    try {
                        questions1=saveRowQuestion(userInfo,row,questionTye);
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    questions.add(questions1);
                }
            }
            topQuestionsRepository.saveAll(questions);
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
            List<TopQuestions> questions=new ArrayList<>();
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
                    TopQuestions questions1;
                    try {
                        questions1=saveRowQuestion(userInfo,row,questionTye);
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    questions.add(questions1);
                }
            }
            topQuestionsRepository.saveAll(questions);
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
            List<TopQuestions> questions=new ArrayList<>();
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
                    TopQuestions questions1;
                    try {
                        questions1=saveRowJudgementQuestion(userInfo,row,questionTye);
                    }catch (UserServiceException e){
                        errorRowNum.add((j + 1) + " ");
                        continue;
                    }
                    questions.add(questions1);
                }
            }
            topQuestionsRepository.saveAll(questions);
            return JSONObject.toJSONString(errorRowNum.toArray());
        }catch (IOException e){
            throw new UserServiceException(ResultCode.EXCEL_IMPORT_MSG);
        }
    }

    //判断题
    private TopQuestions saveRowJudgementQuestion(UserInfoForToken userInfo,HSSFRow row,String type) throws UserServiceException{
        TopQuestions questions=new TopQuestions();
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
        questions.setCreaterId(userInfo.getUserId());
        questions.setCreaterName(userInfo.getUserName());
        questions.setType(type);
        return questions;
    }

    private TopQuestions saveRowQuestion(UserInfoForToken userInfo,HSSFRow row,String type) throws UserServiceException{
        TopQuestions questions=new TopQuestions();
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
        questions.setCreaterId(userInfo.getUserId());
        questions.setCreaterName(userInfo.getUserName());
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
