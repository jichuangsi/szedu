package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.ArticleLikeRelation;
import cn.com.szedu.entity.IntermediateTable.ArticlePictures;
import cn.com.szedu.entity.IntermediateTable.ArticleUserRelation;
import cn.com.szedu.exception.CourseWareException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ArticleModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IArticleCategoryRepository;
import cn.com.szedu.repository.IArticlePictureRepository;
import cn.com.szedu.repository.IArticleRepository;
import cn.com.szedu.repository.IOpLogRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IArticleCommentRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IArticleLikeRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IArticleUserRelationRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Resource
    private IArticlePictureRepository articlePictureRepository;
    @Resource
    private IArticleLikeRepository articleLikeRepository;
    @Resource
    private IArticleCommentRepository articleCommentRepository;

    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;

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

    /**
     * 本地上传多文件
     * @param userInfoForToken
     * @param files
     * @throws IOException
     */
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void localUpLoadFiles(UserInfoForToken userInfoForToken, MultipartFile[] files,Integer articleId) throws IOException {
        List<ArticlePictures> articlePictures=new ArrayList<>();
        for (MultipartFile file:files) {
            //获取文件名
            String fileName = file.getOriginalFilename();
            ArticlePictures articlePicture=new ArticlePictures();
            //获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            articlePicture.setFilename(fileName);
            //重新生成文件名
            fileName =UUID.randomUUID()+suffixName;
            File file1=new File(uploadPath+imagePath+fileName);
            if (!file1.exists()){
                //创建文件夹
                file1.getParentFile().mkdir();
            }
            file.transferTo(file1);
            articlePicture.setFilepath(uri+fileName);
            if (!StringUtils.isEmpty(articleId)){
                articlePicture.setArticleId(articleId);
            }
            articlePictures.add(articlePicture);
        }
        articlePictureRepository.saveAll(articlePictures);
    }

    //查询全部文章和图片
    public List<ArticleModel> getAllArticleAndPicture(int pageNum, int pageSize){
        return articleRepository.getAllArticleAndPicture(pageNum,pageSize);
    }

    /**
     * 删除文章和图片
     * @param userInfo
     * @param id
     * @throws UserServiceException
     */
    @Transactional
    public void deleteArticleAndPicture(UserInfoForToken userInfo,Integer id)throws UserServiceException {
        if(StringUtils.isEmpty(id)){
            throw new UserServiceException(ResultCode.PARAM_MISS_MSG);
        }
        articleRepository.deleteByid(id);
        List<ArticlePictures> articlePictures=articlePictureRepository.findByArticleId(id);
        for (ArticlePictures a:articlePictures) {
            File file=new File(a.getFilepath());
            if(file.exists()){
                //删除文件
                file.delete();
            }else {
                throw new UserServiceException(ResultCode.FILE_ISNOT_EXIST);
            }
        }
        articlePictureRepository.deleteAll(articlePictures);
        OpLog opLog=new OpLog(userInfo.getUserName(),"删除","删除文章");
        opLogRepository.save(opLog);
    }

    /**
     * 文章点赞
     * @param userInfo
     * @param articleId
     */
    public void saveUserLikeArticle(UserInfoForToken userInfo,Integer articleId){
        ArticleLikeRelation articleLikeRelation =new ArticleLikeRelation();
        articleLikeRelation.setArticleId(articleId);
        articleLikeRelation.setUid(userInfo.getUserId());
        articleLikeRepository.save(articleLikeRelation);
    }

    /**
     * 用户取消点赞
     * @param userInfo
     * @param likeId
     */
    public void deleteUserLike(UserInfoForToken userInfo,Integer likeId)throws UserServiceException{
        ArticleLikeRelation articleLikeRelation=articleLikeRepository.findByid(likeId);
        if(articleLikeRelation!=null){
            articleLikeRepository.delete(articleLikeRelation);
        }else {
            throw  new UserServiceException(ResultCode.USERLIKE_ISNOT_EXIST);
        }
    }

    /**
     * 文章评论
     * @param userInfo
     * @param comment
     */
    public void saveArticleCommon(UserInfoForToken userInfo,ArticleComment comment){
        comment.setUserId(userInfo.getUserId());
        articleCommentRepository.save(comment);
    }

    /**
     * 删除评论
     * @param userInfo
     * @param commonId
     */
    public void deleteArticleCommon(UserInfoForToken userInfo,Integer commonId){
        ArticleComment comment=articleCommentRepository.findByid(commonId);
        if(comment!=null){
            articleCommentRepository.delete(comment);
        }
    }
}
