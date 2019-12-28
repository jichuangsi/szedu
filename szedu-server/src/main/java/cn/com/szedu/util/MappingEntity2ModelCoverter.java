package cn.com.szedu.util;

import cn.com.szedu.commons.Md5Util;
import cn.com.szedu.constant.Status;
import cn.com.szedu.entity.*;
import cn.com.szedu.model.*;
import cn.com.szedu.model.role.StaticPageModel;
import cn.com.szedu.model.student.SchoolInfoModel;
import cn.com.szedu.model.teacher.ExamModel;
import cn.com.szedu.model.teacher.TestTimeModel;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MappingEntity2ModelCoverter {
    public final static UserInfoForToken CONVERTERFROMBACKUSERINFO(BackUser userInfo) {
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getUserName());
        userInfoForToken.setRoleId(userInfo.getRoleId());
        return userInfoForToken;
    }

    public final static UserInfoForToken CONVERTERFROMBACKUSERUSER(UserInfo userInfo) {
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getName());
        userInfoForToken.setRoleId(userInfo.getRole());
        return userInfoForToken;
    }

    public final static List<StaticPageModel> CONVERTERFROMStaticPage(List<StaticPage> staticPages) {
        List<StaticPageModel> modelList = new ArrayList<>();
        staticPages.forEach(model -> {
            StaticPageModel model1 = new StaticPageModel();
            model1.setPageId(model.getId());
            model1.setPageName(model.getPageName());
            model1.setPageUrl(model.getPageUrl());
            model1.setParentNode(model.getParentNode());
            modelList.add(model1);
        });
        return modelList;
    }

    public static UserInfo CONVERTEERFROMTEACHERMODEL(TeacherModel model) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(model.getId());
        userInfo.setAccount(model.getAccount());
        userInfo.setName(model.getName());
        if (!StringUtils.isEmpty(model.getPwd())) {
            userInfo.setPwd(Md5Util.encodeByMd5(model.getPwd()));
        }
        userInfo.setSex(model.getSex());
        userInfo.setStatus(Status.ACTIVATE.getName());
        userInfo.setSubject(model.getSubject());
        userInfo.setSchoolName(model.getSchoolName());
        userInfo.setRole("Teacher");
        userInfo.setIntegral(0);
        return userInfo;
    }

    public static UserInfo CONVERTEERFROMSTUDENTMODEL(StudentModel model) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(model.getId());
        userInfo.setAccount(model.getAccount());
        userInfo.setName(model.getName());
        if (!StringUtils.isEmpty(model.getPwd())) {
            userInfo.setPwd(Md5Util.encodeByMd5(model.getPwd()));
        }
        userInfo.setSex(model.getSex());
        userInfo.setStatus(Status.ACTIVATE.getName());
        userInfo.setPhone(model.getPhone());
        userInfo.setSchoolName(model.getSchoolName());
        userInfo.setRole("Student");
        return userInfo;
    }

    public final static TeacherModel CONVERTERFROMUSERINFO(UserInfo userInfo){
        TeacherModel model = new TeacherModel();
        model.setAccount(userInfo.getAccount());
        model.setId(userInfo.getId());
        model.setName(userInfo.getName());
        model.setSex(userInfo.getSex());
        model.setStatus(userInfo.getStatus());
        model.setSchoolName(userInfo.getSchoolName());
        model.setSubject(userInfo.getSubject());
        model.setIntegral(userInfo.getIntegral());
        return model;
    }

    public final static StudentModel CONVERTERFROMUSERINFOTOSTUDENT(UserInfo userInfo){
        StudentModel model = new StudentModel();
        model.setAccount(userInfo.getAccount());
        model.setId(userInfo.getId());
        model.setName(userInfo.getName());
        model.setSex(userInfo.getSex());
        model.setStatus(userInfo.getStatus());
        model.setSchoolName(userInfo.getSchoolName());
        model.setPhone(userInfo.getPhone());
        return model;
    }

    public final static SelfQuestions CONVERTERFROMQUESTIONSMODELIITOSELFQUESTIONS(QuestionsModelII modelII){
        SelfQuestions questions=new SelfQuestions();
        if (!StringUtils.isEmpty(modelII.getId())){
            questions.setId(modelII.getId());
        }
        questions.setAnswer(modelII.getAnswer());
        questions.setAnswerDetail(modelII.getAnswerDetail());
        questions.setTeacherId(modelII.getTeacherId());
        questions.setTeacherName(modelII.getTeacherName());
        questions.setSubject(modelII.getSubject());
        questions.setSubjectId(modelII.getSubjectId());
        questions.setCreateTime(new Date().getTime());
        questions.setIntegral(modelII.getIntegral());
        questions.setType(modelII.getType());
        questions.setContent(modelII.getTitle());
        return questions;
    }

    public final static QuestionsModelII CONVERTERFROMSELFQUESTIONSTOQUESTIONSMODELII(SelfQuestions questions){
       QuestionsModelII model=new QuestionsModelII();
       model.setId(questions.getId());
       model.setAnswer(questions.getAnswer());
       model.setAnswerDetail(questions.getAnswerDetail());
       model.setIntegral(questions.getIntegral());
       QuestionOptions options=new QuestionOptions();
       options.setA(questions.getAoption());
       options.setAoptionPic(questions.getAoptionPic());
       options.setB(questions.getAoption());
       options.setBoptionPic(questions.getAoptionPic());
       options.setC(questions.getAoption());
       options.setCoptionPic(questions.getAoptionPic());
       options.setD(questions.getAoption());
       options.setDoptionPic(questions.getAoptionPic());
       model.setOptions(options);
       model.setSubject(questions.getSubject());
       model.setSubjectId(questions.getSubjectId());
       model.setTeacherId(questions.getTeacherId());
       model.setTeacherName(questions.getTeacherName());
       model.setTitle(questions.getContent());
       model.setTitlePic(questions.getContentPic());
       model.setType(questions.getType());
       model.setChapter(questions.getChapter());
       return  model;
    }

    public final static Curriculum CONVERTERFROMCURRICULUMMODELCURRICULUM(CurriculumModel model){
        Curriculum curriculum=new Curriculum();
        if(StringUtils.isEmpty(curriculum.getId())){
            curriculum.setId(model.getId());
        }
        curriculum.setCurriculumName(model.getCurriculumName());
        curriculum.setContent(model.getContent());
        curriculum.setIntegral(model.getIntegral());
        curriculum.setIsCheck(model.getIsCheck());
        curriculum.setCurriculumPic(model.getCurriculumPic());
        curriculum.setSubject(model.getSubject());
        curriculum.setSubjectId(model.getSubjectId());
        curriculum.setTeacherid(model.getTeacherid());
        curriculum.setTeacherName(model.getTeacherName());
        return curriculum;
    }

    public final static CurriculumResource CONVERTERFROMCURRICULUMMODELCURRICULUMRESOUTCE(CurriculumModel model){
        CurriculumResource curriculum=new CurriculumResource();
        curriculum.setCurriculumId(model.getId());
        curriculum.setFilegroup(model.getFilegroup());
        curriculum.setFilename(model.getFilename());
        curriculum.setFilepath(model.getFilepath());
        curriculum.setTeacherid(model.getTeacherid());
        return curriculum;
    }

    public final static SchoolInfo CONVERTERFROMSCHOOLINFOMODELSCHOOLINFO(SchoolInfoModel model){
        SchoolInfo schoolInfo =new SchoolInfo();
        schoolInfo.setAddress(model.getAddress());
        schoolInfo.setBuyTime(model.getBuyTime());
        schoolInfo.setContacts(model.getContacts());
        schoolInfo.setContactsJob(model.getContactsJob());
        schoolInfo.setPhone(model.getPhone());
        schoolInfo.setRemarks(model.getRemarks());
        schoolInfo.setSchoolMotto(model.getSchoolMotto());
        schoolInfo.setSchoolName(model.getSchoolName());
        schoolInfo.setStatus(model.getStatus());
        schoolInfo.setType(model.getType());
        return schoolInfo;
    }

    public final static Exam CONVERTERFROMEXAMMODELTOEXAM(ExamModel model){
        Exam exam=new Exam();
        exam.setExamName(model.getExamName());
        exam.setContent(model.getContent());
        exam.setCreatorId(model.getCreatorId());
        exam.setCreatorName(model.getCreatorName());
        exam.setStatus(model.getStatus());
        exam.setTerm(model.getTerm());
        exam.setSubjectId(model.getSubjectId());
        exam.setSubjectName(model.getSubjectName());
        exam.setExamType(model.getExamType());
        exam.setIsOpenAnswer(model.getIsOpenAnswer());
        model.getModels().forEach(testTimeModel -> {
            if (testTimeModel.getId().equalsIgnoreCase("1")){
                exam.setStartTime(testTimeModel.getStartTime());
                exam.setEndTime(testTimeModel.getStartTime());
                exam.setTestTimeLength(testTimeModel.getTimeLength());
                exam.setTiqian(testTimeModel.getTiqian());
            }else {
                exam.setStartTime(testTimeModel.getStartTime());
                exam.setEndTime(testTimeModel.getEndTime());
                exam.setTestTimeLength(testTimeModel.getTimeLength());
            }
        });
        return exam;
    }

    public final static ExamModel CONVERTERFROMEXAMTOEXAMMODEL(Exam exam){
        ExamModel model=new ExamModel();
        model.setContent(exam.getContent());
        model.setExamId(exam.getId());
        model.setExamName(exam.getExamName());
        model.setCreatorId(exam.getCreatorId());
        model.setStatus(exam.getStatus());
        model.setTerm(exam.getTerm());
        model.setSubjectId(exam.getSubjectId());
        model.setSubjectName(exam.getSubjectName());
        model.setExamType(exam.getExamType());
        model.setIsOpenAnswer(exam.getIsOpenAnswer());
        TestTimeModel testTimeModel=new TestTimeModel();
        testTimeModel.setStartTime(exam.getStartTime());
        testTimeModel.setEndTime(exam.getEndTime());
        testTimeModel.setTimeLength(exam.getTestTimeLength());
        testTimeModel.setTiqian(exam.getTiqian());
        List<TestTimeModel> models=new ArrayList<>();
        models.add(testTimeModel);
        model.setModels(models);
        if (exam.getStatus().equalsIgnoreCase("1")){
            model.setStatusName("未发布");
        }else if (exam.getStatus().equalsIgnoreCase("2")){
            model.setStatusName("准备考试");
        }else if (exam.getStatus().equalsIgnoreCase("3")){
            model.setStatusName("正在考试");
        }else if (exam.getStatus().equalsIgnoreCase("4")){
            model.setStatusName("已结束");
        }
        return model;
    }

}
