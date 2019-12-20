package cn.com.szedu.controller.teacher;

import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.ClassModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/TeacherResource")
@Api("班级相关的api")
@CrossOrigin
public class ClassConsoleController {

    @ApiOperation(value = "我的班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getClassByTeacherId")
    public ResponseModel<List<ClassModel>> getClassByTeacherId(@ModelAttribute UserInfoForToken userInfo) {
        List<ClassModel> modelList=new ArrayList<>();
        return  ResponseModel.sucess("",modelList);
    }

    @ApiOperation(value = "新增班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addClass")
    public ResponseModel getClassByTeacherId(@ModelAttribute UserInfoForToken userInfo,@RequestParam ClassModel model) {
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "新增学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addStudent")
    public ResponseModel addStudent(@ModelAttribute UserInfoForToken userInfo,@RequestParam StudentModel model) {
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateStudent")
    public ResponseModel updateStudent(@ModelAttribute UserInfoForToken userInfo,@RequestParam StudentModel model) {
        return  ResponseModel.sucessWithEmptyData("");
    }
}
