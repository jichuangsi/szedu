package cn.com.szedu.controller.teacher;

import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TeacherCourseModel;
import cn.com.szedu.model.teacher.TeacherLessonModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/TeacherLesson")
@Api("教师课堂相关的api")
@CrossOrigin
public class TeacherLessonController {

    @ApiOperation(value = "我的课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/teacherLesson")
    public ResponseModel<List<TeacherLessonModel>> TeacherLesson(@ModelAttribute UserInfoForToken userInfo) {
        List<TeacherLessonModel> modelList=new ArrayList<>();
        return  ResponseModel.sucess("",modelList);
    }

    @ApiOperation(value = "添加课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addLesson")
    public ResponseModel addLesson(@ModelAttribute UserInfoForToken userInfo ,@RequestBody TeacherLessonModel model) {
        return  ResponseModel.sucessWithEmptyData("");
    }
}
