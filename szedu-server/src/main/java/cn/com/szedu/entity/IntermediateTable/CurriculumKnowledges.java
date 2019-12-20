package cn.com.szedu.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "curriculumKnowledges")
public class CurriculumKnowledges {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer curriculumId;
    private String knowledgesId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(Integer curriculumId) {
        this.curriculumId = curriculumId;
    }

    public String getKnowledgesId() {
        return knowledgesId;
    }

    public void setKnowledgesId(String knowledgesId) {
        this.knowledgesId = knowledgesId;
    }
}
