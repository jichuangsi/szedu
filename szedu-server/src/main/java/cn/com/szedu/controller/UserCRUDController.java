package cn.com.szedu.controller;

import cn.com.szedu.entity.UserInfo;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.*;
import cn.com.szedu.service.UserInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@Api("UserCRUDController相关的api")
@CrossOrigin
@RequestMapping("/UserInfoConsole")
public class UserCRUDController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "保存老师信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/saveTeacher")
    public ResponseModel saveTeacher(@ModelAttribute UserInfoForToken userInfo, @RequestBody TeacherModel model){
        try {
            userInfoService.saveTeacher(userInfo,model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "保存学生信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/saveStudent")
    public ResponseModel saveStudent(@ModelAttribute UserInfoForToken userInfo, @RequestBody StudentModel model){
        try {
            userInfoService.saveStudent(userInfo, model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改老师信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/updateTeacher")
    public ResponseModel updateTeacher(@ModelAttribute UserInfoForToken userInfo, @RequestBody TeacherModel model){
        try {
            userInfoService.updateTeacher(userInfo,model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改学生信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/updateStudent")
    public ResponseModel updateStudent(@ModelAttribute UserInfoForToken userInfo, @RequestBody StudentModel model){
        try {
            userInfoService.updateStudent(userInfo, model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "excel批量保存学生信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping(value = "/excel/saveStudentByExcel")
    public ResponseModel<String> saveStudentByExcel(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",userInfoService.saveExcelStudents(file,userInfo));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "excel批量保存老师信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping(value = "/excel/saveTeacherByExcel")
    public ResponseModel<String> saveTeacherByExcel(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",userInfoService.saveExcelTeachers(file,userInfo));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据条件查老师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/teacher/getTeacherByCondition")
    public ResponseModel<PageInfo<TeacherModel>> getTeacherByCondition(@ModelAttribute UserInfoForToken userInfo, @RequestBody UserConditionModel model){
        try {
            return ResponseModel.sucess("",userInfoService.getTeachersByCondition(userInfo, model));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }
    @ApiOperation(value = "查询全部老师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/teacher/getAllTeacher")
    public ResponseModel getAllTeacher(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",userInfoService.getAllTeacher());
    }


    @ApiOperation(value = "根据条件查学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/student/getStudentByCondition")
    public ResponseModel<PageInfo<StudentModel>> getStudentByCondition(@ModelAttribute UserInfoForToken userInfo, @RequestBody UserConditionModel model){
        try {
            return ResponseModel.sucess("",userInfoService.getStudentsByCondition(userInfo, model));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/deleteUserById")
    public ResponseModel<PageInfo<UserInfo>> deleteUserById(@ModelAttribute UserInfoForToken userInfo, @RequestParam String id){
        try {
            userInfoService.deleteUserInfo(userInfo, id);
            return ResponseModel.sucessWithEmptyData("");
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "根据id修改用户密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/updateUserPwdById")
    public ResponseModel updateUserPwdById(@ModelAttribute UserInfoForToken userInfo, @RequestParam String id, @RequestParam String newPwd, @RequestParam String pwd){
        try {
           userInfoService.updateStaffPwd(userInfo,id,newPwd,pwd);
            return ResponseModel.sucessWithEmptyData("");
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("后台用户登录")
    @ApiImplicitParams({})
    @PostMapping("/loginBackUser")
    public ResponseModel loginBackUser(@RequestBody BackUserLoginModel model){
        try {
            return ResponseModel.sucess("",userInfoService.loginBackUser(model));
        }catch (UserServiceException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }
}
