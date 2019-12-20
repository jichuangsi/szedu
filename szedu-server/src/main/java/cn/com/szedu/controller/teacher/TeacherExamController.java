package cn.com.szedu.controller.teacher;

import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TestDetail;
import cn.com.szedu.model.teacher.ExamModel;
import cn.com.szedu.model.teacher.SelfQuestionsModel;
import cn.com.szedu.model.teacher.TestPaperModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/TeacherExam")
@Api("试卷相关的api")
@CrossOrigin
public class TeacherExamController {

    @ApiOperation(value = "添加试题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addQuestions")
    public ResponseModel getCourse(@ModelAttribute UserInfoForToken userInfo, @RequestParam SelfQuestionsModel model) {

        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "我的考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getMyExam")
    public ResponseModel<List<ExamModel>> getMyExam(@ModelAttribute UserInfoForToken userInfo) {
        List<ExamModel> models=new ArrayList<>();
        return  ResponseModel.sucess("",models);
    }

    @ApiOperation(value = "我的试题库", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getSelfQuestions")
    public ResponseModel<List<SelfQuestionsModel>> getSelfQuestions(@ModelAttribute UserInfoForToken userInfo) {
        List<SelfQuestionsModel> models=new ArrayList<>();
        return  ResponseModel.sucess("",models);
    }

    @ApiOperation(value = "精品试题库", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getTopQuestions")
    public ResponseModel<List<SelfQuestionsModel>> getTopQuestions(@ModelAttribute UserInfoForToken userInfo) {
        List<SelfQuestionsModel> models=new ArrayList<>();
        return  ResponseModel.sucess("",models);
    }

    @ApiOperation(value = "组卷", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addTestPaper")
    public ResponseModel addTestPaper(@ModelAttribute UserInfoForToken userInfo, @RequestParam TestPaperModel model) {
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "试卷库", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getTestPaper")
    public ResponseModel<List<SelfQuestionsModel>> getTestPaper(@ModelAttribute UserInfoForToken userInfo) {
        List<SelfQuestionsModel> models=new ArrayList<>();
        return  ResponseModel.sucess("",models);
    }

    @ApiOperation(value = "考试详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getTestDetail")
    public ResponseModel<List<TestDetail>> getTestDetail(@ModelAttribute UserInfoForToken userInfo) {
        List<TestDetail> models=new ArrayList<>();
        return  ResponseModel.sucess("",models);
    }
}
