package cn.com.szedu.controller.teacher;

import cn.com.szedu.entity.*;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.SandMessageModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.MessageModel;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.service.TeacherInfoService;
import org.springframework.data.domain.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
@SessionAttributes
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
    public ResponseModel<TeacherModel> loginBackUser(@RequestBody TeacherModel model) {
        try {
            return ResponseModel.sucess("", teacherInfoService.loginTeacher(model));
        } catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "签到", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/signin")
    public ResponseModel<Integer> signin(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {
        //teacherInfoService.signin(userInfo);
        //return ResponseModel.sucessWithEmptyData("");
        return ResponseModel.sucess("", teacherInfoService.signin(userInfo));
    }

    @ApiOperation(value = "查看老师积分记录", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getTeacherIntegral")
    public ResponseModel<Page<IntegralRecord>> getTeacherIntegral(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.getTeacherIntegral(userInfo, pageNum, pageSize));
    }

    @ApiOperation(value = "查看老师系统消息(老师学生通用)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getTeacherMessage")
    public ResponseModel<Page<Message>> getTeacherMessage(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.getTeacherMessage(userInfo, pageNum, pageSize));
    }

    @ApiOperation(value = "查看老师消息未读数(系统+互动)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getTeacherMessageCount")
    public ResponseModel<Integer> getTeacherMessageCount(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.getTeacherMessageCount(userInfo));
    }

    @ApiOperation(value = "修改老师系统消息未读信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateTeacherMessage")
    public ResponseModel updateTeacherMessage(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {
        teacherInfoService.updateTeacherMessage(userInfo);
        return ResponseModel.sucessWithEmptyData("");
    }

   /* @ApiOperation(value = "获取用户是否登录30分钟", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getUserLogin")
    public ResponseModel getUserLogin(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {
        teacherInfoService.getUserLogin(userInfo);
        return ResponseModel.sucessWithEmptyData("");
    }*/
 @ApiOperation(value = "获取积分规则", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getintegralRule")
    public ResponseModel getintegralRule(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.integralRule(pageNum,pageSize));
    }

    @ApiOperation(value = "给老师发送系统消息(定时，返回发送信息数量)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/sendMessageByTeacher")
    public ResponseModel<Integer> sendMessageByTeacher(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.sendMessageByTeacher(userInfo));
    }

    @ApiOperation(value = "查看(老师,学生)互动消息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getTeacherInteractionMessage")
    public ResponseModel<List<Message>> getTeacherInteractionMessage(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {
    return ResponseModel.sucess("", teacherInfoService.getTeacherInteractionMessage(userInfo, pageNum, pageSize));
    }

    @ApiOperation(value = "发送互动消息（老师学生通用）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addInteractionMessage")
    public ResponseModel addInteractionMessage(@ModelAttribute UserInfoForToken userInfo,@RequestBody SandMessageModel model) throws UserServiceException {
        teacherInfoService.addInteractionMessage(userInfo,model);
        return ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation(value = "修改积分规则", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/updateintegralRule")
    public ResponseModel updateintegralRule(@ModelAttribute UserInfoForToken userInfo, @RequestBody IntegralRule integralRule){
        teacherInfoService.updateintegralRule(userInfo,integralRule);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查看所有老师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllTeacher")
    public ResponseModel<List<TeacherInfo>> getAllTeacher(@ModelAttribute UserInfoForToken userInfo)throws UserServiceException{
        teacherInfoService.getAllTeacher(userInfo);
        return ResponseModel.sucessWithEmptyData("");
    }

    //消息
    @ApiOperation(value = "发送信息", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @GetMapping("/addMessage")
    public ResponseModel addMessage(UserInfoForToken userInfo,MessageModel message)throws UserServiceException{
        teacherInfoService.addMessage(userInfo,message);
        return ResponseModel.sucessWithEmptyData("");
    }
    //积分
    @ApiOperation(value = "积分", notes = "")
   /* @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })*/
    @GetMapping("/addintegral")
    public ResponseModel addintegral(UserInfoForToken userInfo,IntegralRecord integralRecord)throws UserServiceException{
        teacherInfoService.addintegral(userInfo,integralRecord);
        return ResponseModel.sucessWithEmptyData("");
    }
}
