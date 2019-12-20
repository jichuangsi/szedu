package cn.com.szedu.service;


import cn.com.szedu.entity.IntermediateTable.Knowledges;
import cn.com.szedu.entity.QuestionType;
import cn.com.szedu.entity.SelfQuestions;
import cn.com.szedu.model.QuestionsModelII;
import cn.com.szedu.repository.IKnowledgesRepository;
import cn.com.szedu.repository.IQuestionOptionsRepository;
import cn.com.szedu.repository.IQuestionTypeRepository;
import cn.com.szedu.repository.ISelfQuestionsRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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

    public void addQuestion(QuestionsModelII model) {
        SelfQuestions questions1=selfQuestionsRepository.findByid(model.getId());
        if (!(questions1==null)){
            model.setImg(questions1.getQuestionPic());
        }
        SelfQuestions questions=selfQuestionsRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMQUESTIONSMODELIITOSELFQUESTIONS(model));
        if(model.getOptions()!=null){
            model.getOptions().setQuestionId(questions.getId());
            questionOptionsRepository.save(model.getOptions());//选项
        }
        if (model.getKnowledges()!=null && model.getKnowledges().size()!=0){
            for (Knowledges k:model.getKnowledges()) {
                k.setQuestionId(questions.getId());
            }
            knowledgesRepository.saveAll(model.getKnowledges());//保存知识点
        }
    }

    //上传图片
    public Integer addQuestionImg(MultipartFile file)throws IOException {
        SelfQuestions questions=new SelfQuestions();
        questions.setQuestionPic(file.getBytes());
        SelfQuestions questions1=selfQuestionsRepository.save(questions);
        return questions1.getId();
    }

    //根据id查询图片
    public byte[] getImgByQuestionId(Integer id){
        return selfQuestionsRepository.findByid(id).getQuestionPic();
    }

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
}
