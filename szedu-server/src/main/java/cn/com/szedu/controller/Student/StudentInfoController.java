package cn.com.szedu.controller.Student;

import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.Student.StudentInfoModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.service.StudentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/studentInfo")
@Api("学生相关的api")
@CrossOrigin
public class StudentInfoController {


  /*  @Resource
    private StudentInfoService studentInfoService;*/
   /* @ApiOperation("登录")
    @ApiImplicitParams({})
    @PostMapping("/teacherLogin")
    public ResponseModel<StudentInfoModel> loginBackUser(@RequestBody StudentInfoModel model)throws UserServiceException{
        try {
            return ResponseModel.sucess("",studentInfoService.loginStudent(model));
        }catch (UserServiceException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }*/
}
