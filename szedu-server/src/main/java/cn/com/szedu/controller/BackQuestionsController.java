package cn.com.szedu.controller;

import cn.com.szedu.entity.SelfQuestions;
import cn.com.szedu.model.QuestionsModelII;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.SelfQuestionsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/BackQuestions")
@Api("BackInformationDeliveryController相关的api")
@CrossOrigin
public class BackQuestionsController {

    @Resource
    private SelfQuestionsService selfQuestionsService;

    @ApiOperation(value = "添加试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveSelfQuestion")
    public ResponseModel saveSelfQuestion(@RequestBody QuestionsModelII model,@ModelAttribute UserInfoForToken userInfo) {
        selfQuestionsService.addQuestion(model);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "上传试题图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/uploadQuestionImg")
    public ResponseModel uploadQuestionImg(@RequestParam MultipartFile file,@ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("",selfQuestionsService.addQuestionImg(file));
        }catch (IOException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "查询试题图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getQuestionImg")
    public ResponseModel getQuestionImg(@RequestParam Integer questionId,@ModelAttribute UserInfoForToken userInfo) {
         return ResponseModel.sucess("",selfQuestionsService.getImgByQuestionId(questionId));
    }

    @ApiOperation(value = "删除试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteSelfQuestion")
    public ResponseModel saveSelfQuestion(@RequestParam Integer questionId, @ModelAttribute UserInfoForToken userInfo) {
       selfQuestionsService.deleteQuestion(questionId);
       return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查询全部题目", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllQuestions")
    public ResponseModel<PageInfo<SelfQuestions>> getAllQuestions(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer pageSize,@RequestParam Integer pageNum) {
        return ResponseModel.sucess("",selfQuestionsService.getQuestion(pageSize,pageNum));
    }

    @ApiOperation(value = "查询题目类型", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllQuestionCategory")
    public ResponseModel getAllArticleCategory(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",selfQuestionsService.getQuestionType());
    }
}
