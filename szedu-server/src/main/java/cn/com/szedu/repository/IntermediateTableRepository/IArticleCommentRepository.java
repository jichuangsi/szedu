package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IArticleCommentRepository extends JpaRepository<ArticleComment,Integer> {
    ArticleComment findByid(Integer id);
}
