package cn.com.szedu.model.student;

import java.util.ArrayList;
import java.util.List;

public class AnswerSituationModel {

    private List<SingleChoiceQuestionModel> single=new ArrayList<SingleChoiceQuestionModel>();//单选题
    private List<SingleChoiceQuestionModel> multiple=new ArrayList<SingleChoiceQuestionModel>();//多选题
    private List<SingleChoiceQuestionModel> judgement=new ArrayList<SingleChoiceQuestionModel>();//判断题

    public List<SingleChoiceQuestionModel> getSingle() {
        return single;
    }

    public void setSingle(List<SingleChoiceQuestionModel> single) {
        this.single = single;
    }

    public List<SingleChoiceQuestionModel> getMultiple() {
        return multiple;
    }

    public void setMultiple(List<SingleChoiceQuestionModel> multiple) {
        this.multiple = multiple;
    }

    public List<SingleChoiceQuestionModel> getJudgement() {
        return judgement;
    }

    public void setJudgement(List<SingleChoiceQuestionModel> judgement) {
        this.judgement = judgement;
    }
}
