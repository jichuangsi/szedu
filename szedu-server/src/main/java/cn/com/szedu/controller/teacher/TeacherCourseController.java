package cn.com.szedu.controller.teacher;

import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.TeacherModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TeacherCourseModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/TeacherCourse")
@Api("精品课程相关的api")
@CrossOrigin
public class TeacherCourseController {

    @ApiOperation(value = "精品课程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/topCourse")
    public ResponseModel<List<TeacherCourseModel>> getCourse(@ModelAttribute UserInfoForToken userInfo) {
        List<TeacherCourseModel> modelList=new ArrayList<>();
        return  ResponseModel.sucess("",modelList);
    }
}
