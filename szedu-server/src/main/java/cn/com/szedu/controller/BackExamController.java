package cn.com.szedu.controller;

import cn.com.szedu.entity.Exam;
import cn.com.szedu.entity.SelfQuestions;
import cn.com.szedu.entity.StudentAnswerCollection;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.AddExaminationModel;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.student.StudentAnswerModel2;
import cn.com.szedu.model.teacher.ExamModel;
import cn.com.szedu.service.BackExamService;
import cn.com.szedu.service.TestPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@Api("考试相关的api")
@RequestMapping("/backExam")
public class BackExamController {
    @Resource
    private BackExamService backExamService;
    @Resource
    private TestPaperService testPaperService;

    /*@ApiOperation(value = "添加考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveExam")
    public ResponseModel saveExam(@RequestBody Exam exam, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backExamService.addExam(userInfo,exam);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "修改考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateExam")
    public ResponseModel updateExam(@RequestBody Exam exam, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backExamService.updateExam(exam);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "删除考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteExam")
    public ResponseModel deleteExam(@RequestParam String id, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backExamService.deleteExam(userInfo,id);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "查询考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllExam")
    public ResponseModel getAllExam(@ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",backExamService.getExamList(name,pageNum,pageSize));
    }*/

    @ApiOperation(value = "添加考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveExam")
    public ResponseModel saveExam(@RequestBody ExamModel model, @ModelAttribute UserInfoForToken userInfo) {
        try {
             backExamService.saveExam(userInfo,model);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "根据老师查询考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getExamByTeacher")
    public ResponseModel getExamByTeacher(@ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) String name,@RequestParam(required = false)String subjectId,@RequestParam(required = false)String examType,int pageNum,int pageSize) {
        return ResponseModel.sucess("",backExamService.getExamListByTeacher(userInfo,name,subjectId,examType,pageNum,pageSize));
    }

    @ApiOperation(value = "根据考试id查询考试详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getExamDetailByExamid")
    public ResponseModel getAllExam(@ModelAttribute UserInfoForToken userInfo,@RequestParam String examId) {
        return ResponseModel.sucess("",backExamService.getExamByExamId(userInfo,examId));
    }

    @ApiOperation(value = "发布考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/publishingexamination")
    public ResponseModel publishingexamination(@ModelAttribute UserInfoForToken userInfo,@RequestParam String examId,@RequestParam String status) {
        backExamService.updateExamStatus(userInfo,examId,status);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "组卷", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addTestpaper")
    public ResponseModel addTestpaper(@ModelAttribute UserInfoForToken userInfo, @RequestBody AddExaminationModel model) {
        testPaperService.addTestPaper(userInfo,model);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据试卷id查询试卷", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getTestPaperDetailByid")
    public ResponseModel getTestPaperDetailByid(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer testPaperId,@RequestParam String examId) {
        return ResponseModel.sucess("",testPaperService.getTestPaperByid(testPaperId,examId));
    }

    @ApiOperation(value = "根据老师id查询老师试卷", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getTestPaperByTeacherId")
    public ResponseModel getTestPaperByTeacherId(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",testPaperService.getTestpaperListByTeacherId(userInfo));
    }

    @ApiOperation(value = "删除试卷", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteTestpaper")
    public ResponseModel deleteTestpaper(@ModelAttribute UserInfoForToken userInfo, @RequestParam Integer id) {
        try {
            testPaperService.deleteTestPaperId(id);
        }catch (TecherException e){
            ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "学生提交试卷", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/submitTestPaper")
    public ResponseModel submitTestPaper(@ModelAttribute UserInfoForToken userInfo, @RequestBody StudentAnswerModel2 model2) {
        try {
            backExamService.saveStudentAnswer(userInfo,model2);
        }catch (UserServiceException e){
            ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucess("","true");
    }

    @ApiOperation(value = "根据学生查询考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getExamByStudent")
    public ResponseModel getExamByStudent(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
        return ResponseModel.sucess("",backExamService.getExamByStudentId(userInfo,pageNum,pageSize));
    }
}
