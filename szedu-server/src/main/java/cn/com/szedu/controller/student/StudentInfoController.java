package cn.com.szedu.controller.student;

import cn.com.szedu.entity.Message;
import cn.com.szedu.entity.StudentInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.student.StudentClassInfoModel;
import cn.com.szedu.model.student.StudentInfoModel;
import cn.com.szedu.model.student.StudentIntegralModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.MessageModel;
import cn.com.szedu.service.StudentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/studentInfo")
@Api("学生相关的api")
@CrossOrigin
public class StudentInfoController {


    @Resource
    private StudentInfoService studentInfoService;

    @ApiOperation("登录")
    @ApiImplicitParams({})
    @PostMapping("/studentLogin")
    public ResponseModel<StudentInfoModel> loginBackUser(@RequestBody StudentInfoModel model)throws UserServiceException{
        try {
            return ResponseModel.sucess("",studentInfoService.loginStudent(model));
        }catch (UserServiceException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "留言", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/LeavingMessage")
    public ResponseModel<Boolean> LeavingMessage(@ModelAttribute UserInfoForToken userInfo, @RequestBody MessageModel message)throws UserServiceException{

        return ResponseModel.sucess("", studentInfoService.LeavingMessage(userInfo,message));
    }
    @ApiOperation(value = "获取留言", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getLeavingMessage")
    public ResponseModel<List<Message>> getLeavingMessage(@ModelAttribute UserInfoForToken userInfo)throws UserServiceException{
        return ResponseModel.sucess("", studentInfoService.getLeavingMessage(userInfo));
    }
    @ApiOperation(value = "学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/studentInfo")
    public ResponseModel<StudentInfo> studentInfo(@ModelAttribute UserInfoForToken userInfo, @RequestParam String studentId)throws UserServiceException{

        return ResponseModel.sucess("",studentInfoService.studentInfo(userInfo,studentId));
    }

    @ApiOperation(value = "学生个人中心", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getstudentInfo")
    public ResponseModel<StudentClassInfoModel> getstudentInfo(@ModelAttribute UserInfoForToken userInfo, @RequestParam String studentId)throws UserServiceException{

        return ResponseModel.sucess("",studentInfoService.getstudentInfo(userInfo,studentId));
    }

    @ApiOperation(value = "修改学生信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateStudent")
    public ResponseModel<Boolean> updateStudent(@ModelAttribute UserInfoForToken userInfo, @RequestBody StudentModel model)throws UserServiceException{

        return ResponseModel.sucess("",studentInfoService.updateStudent(userInfo,model));
    }

    @ApiOperation(value = "获取积分规则", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getintegralRule")
    public ResponseModel getintegralRule(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {

        return ResponseModel.sucess("", studentInfoService.integralRule(userInfo,pageNum,pageSize));
    }

    @ApiOperation(value = "学生积分排行", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/integralRanking")
    public ResponseModel<List<StudentIntegralModel>> integralRanking(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {

        return ResponseModel.sucess("", studentInfoService.integralRanking(userInfo,pageNum,pageSize));
    }

    @ApiOperation(value = "查看老师系统消息(学生通用)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getStudentMessage")
    public ResponseModel<List<Message>> getStudentMessage(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {

        return ResponseModel.sucess("", studentInfoService.getStudentMessage(userInfo, pageNum, pageSize));
    }

    @ApiOperation(value = "未读系统信息数(管理员)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/countSystemMessage")
    public ResponseModel<Integer> countSystemMessage(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolid) throws UserServiceException {
        return ResponseModel.sucess("", studentInfoService.countSystemMessage(userInfo,schoolid));
    }
}
