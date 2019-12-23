package cn.com.szedu.controller.Student;

import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.service.StudentLessonService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/studentInfo")
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
}
