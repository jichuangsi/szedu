package cn.com.szedu.controller.teacher;

import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.model.teacher.TeacherCourseModel;
import cn.com.szedu.model.teacher.TeacherLessonModel;
import cn.com.szedu.service.TeacherLessonService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/TeacherLesson")
@Api("教师课堂相关的api")
@CrossOrigin
public class TeacherLessonController {
    @Resource
    private TeacherLessonService teacherLessonService;

    @ApiOperation(value = "我的课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/teacherLesson")
    public ResponseModel<PageInfo<TeacherLessonModel>> TeacherLesson(@ModelAttribute UserInfoForToken userInfo,
          @RequestParam String name, @RequestParam String lessionType, @RequestParam Date time,
           @RequestParam int pageNum, @RequestParam int PageSize)throws TecherException {

        return  ResponseModel.sucess("",teacherLessonService.getAllLesson(userInfo, name, lessionType, time, pageNum, PageSize));
    }

    @ApiOperation(value = "删除课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteLesson")
    public ResponseModel deleteLesson(@ModelAttribute UserInfoForToken userInfo ,@RequestParam String lessonId) throws TecherException{
        teacherLessonService.deleteLession(userInfo, lessonId);
        return  ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation(value = "添加课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addLesson")
    public ResponseModel addLesson(@ModelAttribute UserInfoForToken userInfo ,@RequestBody TeacherLessonModel model) {
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "课堂签到(考勤)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addAttendace")
    public ResponseModel addAttendace(@ModelAttribute UserInfoForToken userInfo, @RequestBody AttendanceModel model) throws TecherException{
            teacherLessonService.addAttendace(userInfo, model);
        return  ResponseModel.sucessWithEmptyData("");
    }

}
