package cn.com.szedu.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "articleUserRelation")
public class ArticleUserRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer articleId;
    private String uid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArticleUserRelation(Integer articleId, String uid) {
        this.articleId = articleId;
        this.uid = uid;
    }
}
