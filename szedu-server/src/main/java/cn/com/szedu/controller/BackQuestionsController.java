package cn.com.szedu.controller;

import cn.com.szedu.entity.SelfQuestions;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.AddExaminationModel;
import cn.com.szedu.model.QuestionsModelII;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.SelfQuestionsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/BackQuestions")
@Api("我的题库相关的api")
@CrossOrigin
public class BackQuestionsController {

    @Resource
    private SelfQuestionsService selfQuestionsService;

    /*@ApiOperation(value = "添加试题", notes = "")
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
    }*/

    /*@ApiOperation(value = "查询试题图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getQuestionImg")
    public ResponseModel getQuestionImg(@RequestParam Integer questionId,@ModelAttribute UserInfoForToken userInfo) {
         return ResponseModel.sucess("",selfQuestionsService.getImgByQuestionId(questionId));
    }*/

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

    @ApiOperation(value = "查询老师题库", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getQuestionsByTeacher")
    public ResponseModel<Page<SelfQuestions>> getQuestionsByTeacher(@ModelAttribute UserInfoForToken userInfo, @RequestParam String questionType, @RequestParam Integer pageSize, @RequestParam Integer pageNum) {
        return ResponseModel.sucess("",selfQuestionsService.getQuestionByTeacherId(userInfo,questionType,pageSize,pageNum));
    }

    @ApiOperation(value = "查询题目类型", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllQuestionCategory")
    public ResponseModel getAllArticleCategory(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",selfQuestionsService.getQuestionType());
    }

    @ApiOperation(value = "上传试题内容图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/uploadQuestionContentImg")
    public ResponseModel uploadQuestionImg(@RequestParam MultipartFile file,@ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) Integer questionId) {
        try {
            return ResponseModel.sucess("",selfQuestionsService.addSelfQuestionContentImg(userInfo,file,questionId));
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
            return ResponseModel.sucess("",selfQuestionsService.addSelfQuestionImg(userInfo,file,options,questionId));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "添加试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveSelfQuestion")
    public ResponseModel saveSelfQuestion(@RequestBody QuestionsModelII model,@ModelAttribute UserInfoForToken userInfo) {
        selfQuestionsService.saveQuestion(model,userInfo);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "excel批量保存单选题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping(value = "/excel/saveQuestionByExcel")
    public ResponseModel<String> saveQuestionByExcel(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",selfQuestionsService.saveExcelQuestions(file,userInfo));
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
            return ResponseModel.sucess("",selfQuestionsService.saveExcelMultipleChoiceQuestions(file,userInfo));
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
            return ResponseModel.sucess("",selfQuestionsService.saveExcelJudgementQuestions(file,userInfo));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师查询科目题数", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getQuestionsBySubjectAndTeacher")
    public ResponseModel getQuestionsBySubjectAndTeacher(@ModelAttribute UserInfoForToken userInfo,@RequestParam String subjectId) {
        return ResponseModel.sucess("",selfQuestionsService.getTestSubjectModel(userInfo,subjectId));
    }
}
