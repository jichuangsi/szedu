package cn.com.szedu.repository;

import cn.com.szedu.entity.IntermediateTable.ArticlePictures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IArticlePictureRepository extends JpaRepository<ArticlePictures,Integer> {
    List<ArticlePictures> findByArticleId(Integer articleId);
}
