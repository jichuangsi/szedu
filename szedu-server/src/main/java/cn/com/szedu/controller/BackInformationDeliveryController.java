package cn.com.szedu.controller;

import cn.com.szedu.entity.Article;
import cn.com.szedu.entity.ArticleCategory;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.BackArticleCategoryService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/BackInformation")
@Api("BackInformationDeliveryController相关的api")
@CrossOrigin
public class BackInformationDeliveryController {
    @Resource
    private BackArticleCategoryService backArticleCategoryService;

    @ApiOperation(value = "添加文章分类", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveArticleCategory")
    public ResponseModel saveArticleCategory(@RequestBody ArticleCategory category, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backArticleCategoryService.addArticleCategory(userInfo,category);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "修改文章分类", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateArticleCategory")
    public ResponseModel updateArticleCategory(@RequestBody ArticleCategory category, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backArticleCategoryService.updateArticleCategory(category);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "删除文章分类", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteArticleCategory")
    public ResponseModel deleteArticleCategory(@RequestParam Integer id, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backArticleCategoryService.deleteArticleCategory(userInfo,id);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "查询文章分类", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllArticleCategory")
    public ResponseModel getAllArticleCategory(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",backArticleCategoryService.getAllArticleCategory());
    }

    @ApiOperation(value = "添加文章", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveArticle")
    public ResponseModel saveArticle(@RequestBody Article article, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backArticleCategoryService.addArticle(userInfo,article);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "修改文章", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateArticle")
    public ResponseModel updateArticle(@RequestBody Article article, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backArticleCategoryService.updateArticle(article);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "删除文章", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteArticle")
    public ResponseModel deleteArticle(@RequestParam Integer id, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backArticleCategoryService.deleteArticle(userInfo,id);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "查询全部文章", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllArticle")
    public ResponseModel<PageInfo<Article>> getAllArticle(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum , @RequestParam int pageSize) {
        return ResponseModel.sucess("",backArticleCategoryService.getAllArticle(pageNum,pageSize));
    }
}
