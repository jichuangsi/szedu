package cn.com.szedu.model.teacher;

public class TestSubjectModel {
    private Integer id;
    private String name;
    private Integer singleChoice;//单选
    private Integer multipleChoice;//多选
    private Integer judgement;//判断

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSingleChoice() {
        return singleChoice;
    }

    public void setSingleChoice(Integer singleChoice) {
        this.singleChoice = singleChoice;
    }

    public Integer getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(Integer multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public Integer getJudgement() {
        return judgement;
    }

    public void setJudgement(Integer judgement) {
        this.judgement = judgement;
    }
}
