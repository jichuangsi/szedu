package cn.com.szedu.controller.teacher;

import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TeacherResourceModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/TeacherResource")
@Api("教师资源相关的api")
@CrossOrigin
public class TeacherResourceController {
    @ApiOperation(value = "我的资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getTeacherResource")
    public ResponseModel<List<TeacherResourceModel>> getTeacherResource(@ModelAttribute UserInfoForToken userInfo) {
        List<TeacherResourceModel> modelList=new ArrayList<>();
        return  ResponseModel.sucess("",modelList);
    }
    @ApiOperation(value = "公共资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/publicTeacherResource")
    public ResponseModel<List<TeacherResourceModel>> publicTeacherResource(@ModelAttribute UserInfoForToken userInfo) {
        List<TeacherResourceModel> modelList=new ArrayList<>();
        return  ResponseModel.sucess("",modelList);
    }

    @ApiOperation(value = "添加资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addTeacherResource")
    public ResponseModel addTeacherResource(@ModelAttribute UserInfoForToken userInfo ,@RequestBody TeacherResourceModel model) {
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "批量添加资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/BatchAddTeacherResource")
    public ResponseModel BatchAddTeacherResource(@ModelAttribute UserInfoForToken userInfo ,@RequestBody List<TeacherResourceModel> modelList) {
        return  ResponseModel.sucessWithEmptyData("");
    }
}
