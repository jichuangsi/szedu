package cn.com.szedu.repository;

import cn.com.szedu.entity.Article;
import cn.com.szedu.model.ArticleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IArticleRepository extends JpaRepository<Article,Integer> {
    Article findFirstById(Integer id);
    void deleteByid(Integer id);
    List<Article> findByCategoryId(Integer id);

    @Query(value = "SELECT a.id as articleId,ap.filename as filename,ap.filepath as filepath,a.article_title as articleTitle,a.category_id as categoryId,a.category as category,a.publish_time as publishTime,a.content as content,a.author_id as authorId,a.author_name as authorName FROM article a INNER JOIN article_pictures ap ON a.id=ap.article_id LIMIT ?1,?2",nativeQuery = true)
    List<ArticleModel> getAllArticleAndPicture(int num,int size);
}
