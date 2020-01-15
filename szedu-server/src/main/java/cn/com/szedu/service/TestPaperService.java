package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.Exam;
import cn.com.szedu.entity.SelfQuestions;
import cn.com.szedu.entity.TestPaper;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.AddExaminationModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TestPaperModel;
import cn.com.szedu.repository.IExamRepository;
import cn.com.szedu.repository.ISelfQuestionsRepository;
import cn.com.szedu.repository.ITestPaperRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ITestpaperQuestionRelationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestPaperService {
    @Resource
    private ITestPaperRepository testPaperRepository;
    @Resource
    private SelfQuestionsService selfQuestionsService;
    @Resource
    private ITestpaperQuestionRelationRepository testpaperQuestionRelationRepository;
    @Resource
    private ISelfQuestionsRepository selfQuestionsRepository;
    @Resource
    private IExamRepository examRepository;

    /**
     * 添加试卷
     * @param userInfo
     * @param model
     */
    @Transactional(rollbackFor = Exception.class)
    public void addTestPaper(UserInfoForToken userInfo,AddExaminationModel model){
        TestPaper testPaper=new TestPaper();
        testPaper.setGrade(model.getGrade());
        testPaper.setRemark(model.getRemark());
        testPaper.setTeacherId(userInfo.getUserId());
        testPaper.setTeacherName(userInfo.getUserName());
        testPaper.setTestPaperName(model.getTestPaperName());
        testPaper.setType(model.getType());//考试类型
        TestPaper testPaper1=testPaperRepository.save(testPaper);
        selfQuestionsService.addExamination(userInfo,model.getSubList(),model.getChapter(),model.getWay(),testPaper1.getId());
    }

    /**
     * 教师试卷列表
     * @param userInfo
     * @return
     */
    public List<TestPaper> getTestpaperListByTeacherId(UserInfoForToken userInfo){
        return testPaperRepository.findByTeacherIdOrderByCreateTimeDesc(userInfo.getUserId());
    }

    /**
     * 根据试卷id查找试卷
     * @param testPaperid
     * @param examId
     * @return
     */
    public TestPaperModel getTestPaperByid(Integer testPaperid,String examId){
        Exam exam=examRepository.findFirstByid(examId);
        TestPaper testPaper=testPaperRepository.findByid(testPaperid);
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
            selfQuestions1.setAnswer("");
            selfQuestions1.setAnswerDetail("");
        });
        model.setQuestionsModels(selfQuestions);
        return model;
    }

    /**
     * 删除试卷
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void  deleteTestPaperId(Integer id)throws TecherException {
        if(examRepository.countByTestPaperId(id.toString())){
            throw new TecherException(ResultCode.EXAMTESTPAPER_ISEXIT_MSG);
        }
        testpaperQuestionRelationRepository.deleteByTestPaper(id);
        testPaperRepository.deleteByid(id);
    }
}
