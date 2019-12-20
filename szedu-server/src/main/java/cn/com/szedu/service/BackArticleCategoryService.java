package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.Article;
import cn.com.szedu.entity.ArticleCategory;
import cn.com.szedu.entity.IntermediateTable.ArticleUserRelation;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IArticleCategoryRepository;
import cn.com.szedu.repository.IArticleRepository;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IArticleUserRelationRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class BackArticleCategoryService {
    @Resource
    private IArticleCategoryRepository articleCategoryRepository;
    @Resource
    private IArticleRepository articleRepository;
    @Resource
    private IOpLogRepository opLogRepository;
    @Resource
    private IArticleUserRelationRepository articleUserRelationRepository;

    //添加文章分类
    public void addArticleCategory(UserInfoForToken userInfo,ArticleCategory category)throws UserServiceException {
        if(StringUtils.isEmpty(category.getCategoryName())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        articleCategoryRepository.save(category);
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加文章分类");
        opLogRepository.save(opLog);
    }

    //修改文章分类
    public void updateArticleCategory(ArticleCategory category)throws UserServiceException {
        if(StringUtils.isEmpty(category.getCategoryName())|| StringUtils.isEmpty(category.getId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ArticleCategory articleCategory=articleCategoryRepository.findFirstByid(category.getId());
        articleCategory.setCategoryName(category.getCategoryName());
        articleCategoryRepository.save(articleCategory);
    }

    //删除文章分类
    public void deleteArticleCategory(UserInfoForToken userInfo,Integer id)throws UserServiceException {
        if(StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        //查询是否有文章....
        if (articleRepository.findByCategoryId(id).size()>0){
            throw new UserServiceException(ResultCode.CATEGORY_ISEXIT_MSG);
        }
        articleCategoryRepository.deleteById(id);
        OpLog opLog=new OpLog(userInfo.getUserName(),"删除","删除文章分类");
        opLogRepository.save(opLog);
    }

    //查询文章分类
    public List<ArticleCategory> getAllArticleCategory(){
        return articleCategoryRepository.findAll();
    }


    //添加文章
    public void addArticle(UserInfoForToken userInfo,Article article)throws UserServiceException {
        if(StringUtils.isEmpty(article.getArticleTitle())|| StringUtils.isEmpty(article.getCategory())
                || StringUtils.isEmpty(article.getCategoryId())|| StringUtils.isEmpty(article.getContent())
                || StringUtils.isEmpty(article.getPublishTime())|| StringUtils.isEmpty(article.getAuthorName())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Article article1=articleRepository.save(article);
        //插入中间表
        ArticleUserRelation relation=new ArticleUserRelation(article1.getId(),userInfo.getUserId());
        articleUserRelationRepository.save(relation);
        OpLog opLog=new OpLog(userInfo.getUserName(),"添加","添加文章");
        opLogRepository.save(opLog);
    }

    //修改文章
    public void updateArticle(Article article)throws UserServiceException {
        if(StringUtils.isEmpty(article.getArticleTitle())|| StringUtils.isEmpty(article.getCategory())
                || StringUtils.isEmpty(article.getCategoryId())|| StringUtils.isEmpty(article.getContent())
                || StringUtils.isEmpty(article.getPublishTime())|| StringUtils.isEmpty(article.getAuthorName())
                || StringUtils.isEmpty(article.getId())){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Article article1=articleRepository.findFirstById(article.getId());
        article1.setArticleTitle(article.getArticleTitle());
        article1.setAuthorId(article.getAuthorId());
        article1.setCategory(article.getCategory());
        article1.setCategoryId(article.getCategoryId());
        article1.setContent(article.getContent());
        article1.setPublishTime(article.getPublishTime());
        articleRepository.save(article);
    }

    //删除文章
    @Transactional
    public void deleteArticle(UserInfoForToken userInfo,Integer id)throws UserServiceException {
        if(StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        articleRepository.deleteByid(id);
        OpLog opLog=new OpLog(userInfo.getUserName(),"删除","删除文章");
        opLogRepository.save(opLog);
    }

    //查询全部文章
    public PageInfo<Article> getAllArticle(int pageNum,int pageSize){
        List<Article> articles=articleRepository.findAll();
        PageInfo<Article> pageInfo=new PageInfo<>();
        pageInfo.setList(articles);
        pageInfo.setTotal(articles.size());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        return pageInfo;
    }
}
