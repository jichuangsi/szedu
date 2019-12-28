package cn.com.szedu.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "testpaperQuestionRelation")
public class TestpaperQuestionRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer testPaper;
    private Integer questionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTestPaper() {
        return testPaper;
    }

    public void setTestPaper(Integer testPaper) {
        this.testPaper = testPaper;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public TestpaperQuestionRelation(Integer testPaper, Integer questionId) {
        this.testPaper = testPaper;
        this.questionId = questionId;
    }

    public TestpaperQuestionRelation() {
    }
}
