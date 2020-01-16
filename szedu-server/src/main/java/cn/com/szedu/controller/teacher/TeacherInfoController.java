package cn.com.szedu.controller.teacher;

import cn.com.szedu.entity.*;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.SandMessageModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.ClassModel;
import cn.com.szedu.model.teacher.MessageModel;
import cn.com.szedu.model.teacher.TeacherInfoModel;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.service.BackUserService;
import cn.com.szedu.service.TeacherInfoService;
import org.springframework.data.domain.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
@SessionAttributes
@RestController
@RequestMapping("/teacherInfo")
@Api("老师相关的api")
@CrossOrigin
public class TeacherInfoController {

    @Resource
    private TeacherInfoService teacherInfoService;
    @Resource
    private BackUserService backUserService;
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
    public ResponseModel<List<Message>> getTeacherMessage(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.getTeacherMessage(userInfo, pageNum, pageSize));
    }

    @ApiOperation(value = "查看老师消息未读数(系统+互动)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getTeacherMessageCount")
    public ResponseModel<Integer> getTeacherMessageCount(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.getTeacherMessageCount(userInfo));
    }

    @ApiOperation(value = "查看消息未读数(系统)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getSystemMessageCount")
    public ResponseModel<Integer> getSystemMessageCount(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.getSystemMessageCount(userInfo));
    }

    @ApiOperation(value = "查看消息未读数(互动)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/gethuMessageCount")
    public ResponseModel<Integer> gethuMessageCount(@ModelAttribute UserInfoForToken userInfo) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.gethuMessageCount(userInfo));
    }
    @ApiOperation(value = "修改老师系统消息未读信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateTeacherMessage")
    public ResponseModel updateTeacherMessage(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer id) throws UserServiceException {

        return ResponseModel.sucess("", teacherInfoService.updateTeacherMessage(userInfo,id));
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

        return ResponseModel.sucess("",teacherInfoService.addInteractionMessage(userInfo,model));
    }
    @ApiOperation(value = "修改积分规则", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateintegralRule")
    public ResponseModel updateintegralRule(@ModelAttribute UserInfoForToken userInfo, @RequestBody IntegralRule integralRule){
        teacherInfoService.updateintegralRule(userInfo,integralRule);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查看所有老师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllTeacher")
    public ResponseModel<List<TeacherModel>> getAllTeacher(@ModelAttribute UserInfoForToken userInfo)throws UserServiceException{

        return ResponseModel.sucess("",teacherInfoService.getAllTeacher(userInfo));
    }
    @ApiOperation(value = "根据学生查询班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllStudent")
    public ResponseModel<List<ClassModel>> getAllStudent(@ModelAttribute UserInfoForToken userInfo)throws UserServiceException{

        return ResponseModel.sucess("",teacherInfoService.getAllStudent(userInfo));
    }
    //消息
    @ApiOperation(value = "发送信息(系统)", notes = "")
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
    @ApiOperation(value = "删除系统消息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/deleteMessage")
    public ResponseModel deleteMessage(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer id)throws UserServiceException{
        teacherInfoService.deleteMessage(id);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "删除互动消息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/deleteInteractionMessage")
    public ResponseModel deleteInteractionMessage(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer id)throws UserServiceException{
        teacherInfoService.deleteInteractionMessage(userInfo,id);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据发信息的人获取互动消息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getMessageBysenderid")
    public ResponseModel<List<Message>> getMessageBysenderid(@ModelAttribute UserInfoForToken userInfo,@RequestParam String sendId, @RequestParam int pageNum, @RequestParam int pageSize) throws UserServiceException {
        return ResponseModel.sucess("", teacherInfoService.getMessageBysenderid(userInfo,sendId, pageNum, pageSize));
    }

    @ApiOperation(value = "老师个人中心", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/teacherinfo")
    public ResponseModel<TeacherInfoModel> teacherinfo(@ModelAttribute UserInfoForToken userInfo,@RequestParam String teacherId) throws UserServiceException {
        return ResponseModel.sucess("", teacherInfoService.teacherinfo(userInfo,teacherId));
    }

    @ApiOperation(value = "修改老师信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateteacherinfo")
    public ResponseModel<Boolean> updateteacherinfo(@ModelAttribute UserInfoForToken userInfo,@RequestBody  TeacherInfo teacherInfo) throws UserServiceException {

        return ResponseModel.sucess("",teacherInfoService.updateteacherinfo(userInfo,teacherInfo));
    }
    @ApiOperation(value = "上传老师头像", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/teacherinfoPic")
    public ResponseModel<Boolean> teacherinfoPic(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo,@RequestParam String teacherId)throws IOException {
        ;
        return ResponseModel.sucess("",teacherInfoService.teacherinfoPic(file,userInfo,teacherId));
    }

    @ApiOperation(value = "我的班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getClassByTeacherId")
    public ResponseModel<List<ClassModel>> getClassByTeacherId(@ModelAttribute UserInfoForToken userInfo)throws UserServiceException {

        return  ResponseModel.sucess("",teacherInfoService.getAllClass(userInfo));
    }

    @ApiOperation(value = "根据班级查看学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getStudentByClassId")
    public ResponseModel<List<StudentModel>> getStudentByClassId(@ModelAttribute UserInfoForToken userInfo, @RequestParam String classId)throws UserServiceException {

        return  ResponseModel.sucess("",teacherInfoService.getStudentByClassId(userInfo,classId));
    }


    @ApiOperation(value = "查看所有老师(学生所在班级的老师)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getStudentTeacher")
    public ResponseModel<List<TeacherModel>> getStudentTeacher(@ModelAttribute UserInfoForToken userInfo)throws UserServiceException{

        return ResponseModel.sucess("",teacherInfoService.getStudentTeacher(userInfo));
    }


    @ApiOperation(value = "老师留言反馈", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/teacherMessage")
    public ResponseModel<Boolean> teacherMessage(@ModelAttribute UserInfoForToken userInfo,@RequestBody MessageFeedback feedback)throws UserServiceException{
    return ResponseModel.sucess("",teacherInfoService.teacherMessage(userInfo,feedback));
    }

    @ApiOperation(value = "查看系统信息(管理员)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getSystemAdmin")
    public ResponseModel getSystemAdmin(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolId,@RequestParam int pageNum,@RequestParam int pageSize) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.getSystemSchoolId(userInfo,schoolId,pageNum,pageSize));
    }

    @ApiOperation(value = "修改已读系统信息(管理员)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/updateSystemAlready")
    public ResponseModel updateSystemAlready(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.updateSystemAlready(userInfo,id));
    }

    @ApiOperation(value = "未读系统信息数(管理员)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/countSystemMessage")
    public ResponseModel<Integer> countSystemMessage(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolid) throws UserServiceException {
        return ResponseModel.sucess("", teacherInfoService.countSystemMessage(userInfo,schoolid));
    }
}
