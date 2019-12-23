package cn.com.szedu.controller.teacher;

import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.ClassExamModel;
import cn.com.szedu.model.teacher.ExamClassResultModel;
import cn.com.szedu.model.teacher.ExamModel;
import cn.com.szedu.service.BackExamService;
import cn.com.szedu.service.TeacherAchievementService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/teacherAchievement")
@Api("班级相关的api")
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
    public ResponseModel<PageInfo<ExamModel>> getExamByClass(@ModelAttribute UserInfoForToken userInfo, @RequestParam String classId , @RequestParam int pageNum , @RequestParam int pageSize)throws TecherException {
        return  ResponseModel.sucess("",teacherAchievementService.getExamByClass(userInfo, classId, pageNum, pageSize));
    }

    @ApiOperation(value = "原题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getExamQuestion")
    public ResponseModel<ExamModel> getExamByClass(@ModelAttribute UserInfoForToken userInfo, @RequestParam String exanId )throws TecherException {
        ExamModel model=new ExamModel();
        return  ResponseModel.sucess("",model);
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
    @PostMapping("/getClassResult")
    public ResponseModel<PageInfo<ExamClassResultModel>> getClassResult(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId , @RequestParam String classId , @RequestParam int pageNum , @RequestParam int pageSize)throws TecherException {
        return  ResponseModel.sucess("",teacherAchievementService.getExamClassResult(userInfo, examId, classId, pageNum, pageSize));
    }

    @ApiOperation(value = "答题情况", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAnswerSituation")
    public ResponseModel<ExamClassResultModel> getAnswerSituation(@ModelAttribute UserInfoForToken userInfo, @RequestParam String examId , @RequestParam String classId , @RequestParam int pageNum , @RequestParam int pageSize)throws TecherException {
        return  ResponseModel.sucess("",null);
    }

    @ApiOperation(value = "根据考试id查询考试详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getExamDetailByTeacherid")
    public ResponseModel getAllExam(@ModelAttribute UserInfoForToken userInfo,String examId) {
        return ResponseModel.sucess("",backExamService.getExamByExamId(userInfo,examId));
    }
}
