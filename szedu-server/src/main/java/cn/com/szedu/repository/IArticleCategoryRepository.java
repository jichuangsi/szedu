package cn.com.szedu.repository;

import cn.com.szedu.entity.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IArticleCategoryRepository extends JpaRepository<ArticleCategory,Integer> {
    ArticleCategory findFirstByid(Integer id);
    void deleteById(Integer id);
}
