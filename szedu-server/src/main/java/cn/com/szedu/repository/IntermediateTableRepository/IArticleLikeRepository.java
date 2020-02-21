package cn.com.szedu.repository.IntermediateTableRepository;

import cn.com.szedu.entity.IntermediateTable.ArticleLikeRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IArticleLikeRepository extends JpaRepository<ArticleLikeRelation,Integer> {
    ArticleLikeRelation findByid(Integer id);
}
