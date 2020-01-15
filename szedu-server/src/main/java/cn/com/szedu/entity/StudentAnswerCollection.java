package cn.com.szedu.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "studentAnswerCollection")
public class StudentAnswerCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String studentId;
    private String studentName;
    private String studentAnswer;//学生答案
    private String result;//正确答案
    private String isTure;//是否正确
    private Integer questionId;
    private long createdTime = new Date().getTime();
    private String examId;
    private String testPaperId;
    private Integer score;//分数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(String testPaperId) {
        this.testPaperId = testPaperId;
    }

    public String getIsTure() {
        return isTure;
    }

    public void setIsTure(String isTure) {
        this.isTure = isTure;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
