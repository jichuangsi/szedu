package cn.com.szedu.controller.student;

import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.model.teacher.MyAllLessionModel;
import cn.com.szedu.model.teacher.TeacherLessonModel;
import cn.com.szedu.service.StudentLessonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/studentLesson")
@Api("学生相关的api")
@CrossOrigin
public class StudentLessonController {

@Resource
private StudentLessonService studentLessonService;


    @ApiOperation(value = "课堂签到(考勤)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addAttendace")
    public ResponseModel addAttendace(@ModelAttribute UserInfoForToken userInfo, @RequestBody AttendanceModel model) throws TecherException {

        return  ResponseModel.sucess("", studentLessonService.addAttendace(userInfo, model));
    }

    @ApiOperation(value = "课堂评分", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addscore")
    public ResponseModel<Boolean> addscore(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer score,@RequestParam String courseId) throws UserServiceException {

        return  ResponseModel.sucess("",studentLessonService.addscore(userInfo, score, courseId));
    }

    @ApiOperation(value = "我的课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAllLesson")
    public ResponseModel TeacherLesson(@ModelAttribute UserInfoForToken userInfo,
                                                                 @RequestBody MyAllLessionModel model) throws TecherException {

        return ResponseModel.sucess("", studentLessonService.getAllLesson(userInfo,model));
    }

    /*@ApiOperation(value = "我的课堂总数", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAllLessonCount")
    public ResponseModel getAllLessonCount(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {

        return ResponseModel.sucess("", studentLessonService.allCount(userInfo));
    }*/


    @ApiOperation(value = "学习资料", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getStudy")
    public ResponseModel getStudy(@ModelAttribute UserInfoForToken userInfo,
                                       @RequestBody MyAllLessionModel model) throws TecherException {
        return ResponseModel.sucess("", studentLessonService.getStudy(userInfo,model));
    }

    @ApiOperation(value = "课堂详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getCourseDetail")
    public ResponseModel<TeacherLessonModel> getCourseDetail(@ModelAttribute UserInfoForToken userInfo, @RequestParam String courseId) throws TecherException {

        return ResponseModel.sucess("",studentLessonService.getCourseDetail(userInfo, courseId));
    }

    @ApiOperation(value = "查看学习资料10分钟获得积分", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/studyTime")
    public ResponseModel<Boolean> studyTime(@ModelAttribute UserInfoForToken userInfo) throws TecherException {
        return ResponseModel.sucess("", studentLessonService.studyTime(userInfo));
    }


    @ApiOperation(value = "根据id查看资源详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getCourseWareDetail")
    public ResponseModel<CourseWare> getCourseWareDetail(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id) throws TecherException {
        return ResponseModel.sucess("", studentLessonService.getCourseWareDetail(userInfo,id));
    }
}
