package cn.com.szedu.controller;

import cn.com.szedu.entity.SelfQuestions;
import cn.com.szedu.entity.TopQuestions;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.QuestionsModelII;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.TopQuestionsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/TopQuestions")
@Api("精品题库相关的api")
@CrossOrigin
public class TopQuestionsController {
    @Resource
    private TopQuestionsService topQuestionsService;

    @ApiOperation(value = "查询全部题目", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllTopQuestions")
    public ResponseModel<PageInfo<TopQuestions>> getAllTopQuestions(@ModelAttribute UserInfoForToken userInfo, @RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        return ResponseModel.sucess("",topQuestionsService.getTopQuestion(pageSize,pageNum));
    }

    @ApiOperation(value = "上传试题内容图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/uploadQuestionContentImg")
    public ResponseModel uploadQuestionImg(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo, @RequestParam(required = false) Integer questionId) {
        try {
            return ResponseModel.sucess("",topQuestionsService.localUploadTopQuestionContentImg(userInfo,file,questionId));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "上传试题选项图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/uploadQuestionOptionImg")
    public ResponseModel uploadQuestionOptionImg(@RequestParam MultipartFile file,@ModelAttribute UserInfoForToken userInfo,@RequestParam String options,@RequestParam(required = false) Integer questionId) {
        try {
            return ResponseModel.sucess("",topQuestionsService.localUploadTopQuestionImg(userInfo,file,options,questionId));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "添加试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveTopQuestion")
    public ResponseModel saveSelfQuestion(@RequestBody QuestionsModelII model, @ModelAttribute UserInfoForToken userInfo) {
        topQuestionsService.saveQuestion(model,userInfo);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "excel批量保存单选题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping(value = "/excel/saveQuestionByExcel")
    public ResponseModel<String> saveQuestionByExcel(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",topQuestionsService.saveExcelQuestions(file,userInfo));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "excel批量保存多选题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping(value = "/excel/saveMultipleChoiceQuestionsByExcel")
    public ResponseModel<String> saveMultipleChoiceQuestionsByExcel(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",topQuestionsService.saveExcelMultipleChoiceQuestions(file,userInfo));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "excel批量保存判断题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping(value = "/excel/saveJudgementQuestionByExcel")
    public ResponseModel<String> saveJudgementQuestionByExcel(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",topQuestionsService.saveExcelJudgementQuestions(file,userInfo));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
