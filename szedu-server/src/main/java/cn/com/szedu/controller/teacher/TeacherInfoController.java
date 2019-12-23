package cn.com.szedu.controller.teacher;

import cn.com.szedu.entity.IntegralRecord;
import cn.com.szedu.entity.OpLog;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.service.TeacherInfoService;
import org.springframework.data.domain.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/teacherInfo")
@Api("老师相关的api")
@CrossOrigin
public class TeacherInfoController {

    @Resource
    private TeacherInfoService teacherInfoService;

    @ApiOperation("登录")
    @ApiImplicitParams({})
    @PostMapping("/teacherLogin")
    public ResponseModel<TeacherModel> loginBackUser(@RequestBody TeacherModel model){
        try {
            return ResponseModel.sucess("",teacherInfoService.loginTeacher(model));
        }catch (UserServiceException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

   /* @ApiOperation(value = "查看老师系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addClass")
    public ResponseModel<Page<OpLog>> getTeacherMessage(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum , @RequestParam int pageSize) throws UserServiceException {

        return  ResponseModel.sucess("",teacherInfoService.getTeacherMessage(userInfo, pageNum, pageSize));
    }*/
    @ApiOperation(value = "查看老师系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addClass")
    public ResponseModel<Page<IntegralRecord>> getTeacherMessage(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum , @RequestParam int pageSize) throws UserServiceException {

        return  ResponseModel.sucess("",teacherInfoService.getTeacherMessage(userInfo, pageNum, pageSize));
    }
}
