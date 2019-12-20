package cn.com.szedu.controller.teacher;

import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.service.TeacherInfoService;
import io.swagger.annotations.Api;
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
}
