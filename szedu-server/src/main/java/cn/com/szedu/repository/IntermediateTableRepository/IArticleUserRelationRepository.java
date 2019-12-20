package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ArticleUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IArticleUserRelationRepository extends JpaRepository<ArticleUserRelation,Integer> {

}
