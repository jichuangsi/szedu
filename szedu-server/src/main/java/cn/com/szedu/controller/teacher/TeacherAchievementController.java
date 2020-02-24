package cn.com.szedu.controller.teacher;

import cn.com.szedu.entity.Exam;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.student.AnswerSituationModel;
import cn.com.szedu.model.student.PerformanceAnalysisModel;
import cn.com.szedu.model.teacher.*;
import cn.com.szedu.service.BackExamService;
import cn.com.szedu.service.TeacherAchievementService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/teacherAchievement")
@Api("教务成绩相关的api")
@CrossOrigin
public class TeacherAchievementController {

    @Resource
    private TeacherAchievementService teacherAchievementService;
    @Resource
    private BackExamService backExamService;


    @ApiOperation(value = "根据班级查询考试信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getClassByTeacherId")
    public ResponseModel getExamByClass(@ModelAttribute UserInfoForToken userInfo, @RequestParam String classId , @RequestParam int pageNum , @RequestParam int pageSize)throws TecherException {
        return  ResponseModel.sucess("",teacherAchievementService.getExamByClass(userInfo, classId, pageNum, pageSize));
    }

    @ApiOperation(value = "原题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getExamQuestion")
    public ResponseModel<ExamModel> getExamByClass(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId )throws TecherException {
        //ExamModel model=new ExamModel();

        return  ResponseModel.sucess("",backExamService.getExamByExamId(userInfo, examId));
    }

    @ApiOperation(value = "班级情况总览", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getClassOverview")
    public ResponseModel<ClassExamModel> getClassOverview(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId )throws TecherException {
        return  ResponseModel.sucess("",teacherAchievementService.getClassExam(userInfo,examId));
    }

    @ApiOperation(value = "班级成绩单", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getClassResult")
    public ResponseModel getClassResult(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId , @RequestParam String classId , @RequestParam int pageNum , @RequestParam int pageSize)throws TecherException {
        return  ResponseModel.sucess("",teacherAchievementService.getExamClassResult(userInfo, examId, classId, pageNum, pageSize));
    }

    /*@ApiOperation(value = "答题情况", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAnswerSituation")
    public ResponseModel<ExamClassResultModel> getAnswerSituation(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId , @RequestParam String classId , @RequestParam int pageNum , @RequestParam int pageSize)throws TecherException {
        return  ResponseModel.sucess("",null);
    }*/
    @ApiOperation(value = "答题情况", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAnswerSituation")
    public ResponseModel<List<AnswerSituationModel>> getAnswerSituation(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId , @RequestParam String classId)throws TecherException {
        return  ResponseModel.sucess("",teacherAchievementService.getAnswerSitution(userInfo, examId, classId));
    }
    @ApiOperation(value = "根据考试id查询考试详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getExamDetailByTeacherid")
    public ResponseModel getAllExam(@ModelAttribute UserInfoForToken userInfo,String examId) {
        return ResponseModel.sucess("",backExamService.getExamByExamId(userInfo,examId));
    }

    @ApiOperation(value = "根据考试id查询考试班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getClassByExamId")
    public ResponseModel<List<ClassModel>> getClassByExamId(@ModelAttribute UserInfoForToken userInfo,@RequestParam String examId) {
        return ResponseModel.sucess("",teacherAchievementService.getClassByExamId(userInfo,examId));
    }

    @ApiOperation(value = "学生考试成绩分析", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getStudnetExamAnaly")
    public ResponseModel<List<ScoreAnalysisModel>> getStudnetExamAnaly(@ModelAttribute UserInfoForToken userInfo, @RequestParam String studentId) throws TecherException{
        return ResponseModel.sucess("",teacherAchievementService.getStudnetExamAnaly(userInfo,studentId));
    }


    @ApiOperation(value = "原题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getExam")
    public ResponseModel<TestPaperModel> getExam(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId) throws TecherException{
        return ResponseModel.sucess("",teacherAchievementService.getExam(userInfo,examId));
    }
}
