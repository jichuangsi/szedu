package cn.com.szedu.controller.student;

import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.student.StudyModel;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.model.teacher.MyAllLessionModel;
import cn.com.szedu.service.StudentLessonService;
import com.github.pagehelper.PageInfo;
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
        studentLessonService.addAttendace(userInfo, model);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "课堂评分", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addscore")
    public ResponseModel addscore(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer score,@RequestParam String courseId) throws UserServiceException {
        studentLessonService.addscore(userInfo, score, courseId);
        return  ResponseModel.sucessWithEmptyData("");
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

    @ApiOperation(value = "我的课堂详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getCourseDetail")
    public ResponseModel getCourseDetail(@ModelAttribute UserInfoForToken userInfo,@RequestParam String courseId) throws TecherException {

        return ResponseModel.sucess("", studentLessonService.getCourseDetail(userInfo,courseId));
    }

    @ApiOperation(value = "学习资料", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getStudy")
    public ResponseModel<PageInfo<StudyModel>> getStudy(@ModelAttribute UserInfoForToken userInfo,
                                                        @RequestBody MyAllLessionModel model) throws TecherException {
        return ResponseModel.sucess("", studentLessonService.getStudy(userInfo,model));
    }

    @ApiOperation(value = "学习资料10分钟", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/studyTime")
    public ResponseModel<Boolean> studyTime(@ModelAttribute UserInfoForToken userInfo) throws TecherException {
        return ResponseModel.sucess("", studentLessonService.studyTime(userInfo));
    }

    @ApiOperation(value = "资源详情加积分", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getCourseWareDetail")
    public ResponseModel<CourseWare> getCourseWareDetail(@ModelAttribute UserInfoForToken userInfo, @RequestParam String id) throws TecherException {
        return ResponseModel.sucess("", studentLessonService.getCourseWareDetail(userInfo,id));
    }
}
