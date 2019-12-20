package cn.com.szedu.repository;

import cn.com.szedu.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IArticleRepository extends JpaRepository<Article,Integer> {
    Article findFirstById(Integer id);
    void deleteByid(Integer id);
    List<Article> findByCategoryId(Integer id);
}
