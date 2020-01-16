package cn.com.szedu.controller;

import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.student.SchoolInfoModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.IBackSchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/BackSchoolInfo")
@Api("BackSchoolInfoController相关的api")
@CrossOrigin
public class BackSchoolInfoController {
    @Resource
    private IBackSchoolService backSchoolService;

    @ApiOperation(value = "添加学校", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveSchoolInfo")
    public ResponseModel saveSchoolInfo(@RequestBody SchoolInfoModel model, @ModelAttribute UserInfoForToken userInfo) {
       backSchoolService.addSchool(userInfo,model);
       return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "添加教师", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveTeacherBySchool")
    public ResponseModel saveTeacherBySchool(@RequestParam String schoolId,@RequestParam String teacherId, @ModelAttribute UserInfoForToken userInfo) {
        backSchoolService.saveTeacherBySchool(schoolId, teacherId);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "上传学校封面", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/schoolPic")
    public ResponseModel<Boolean> schoolPic(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo, @RequestParam String schoolId)throws IOException {
        ;
        return ResponseModel.sucess("",backSchoolService.schoolPic(file,userInfo,schoolId));
    }

    @ApiOperation(value = "编辑学校", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateSchoolInfo")
    public ResponseModel<Boolean> updateSchoolInfo(@RequestBody SchoolInfoModel model, @ModelAttribute UserInfoForToken userInfo) throws UserServiceException{

        return ResponseModel.sucess("", backSchoolService.updateSchoolInfo(userInfo,model));
    }
    @ApiOperation(value = "启用停用学校", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/schoolInfoStatus")
    public ResponseModel schoolInfoStatus(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolId,@RequestParam String status)throws UserServiceException {
        backSchoolService.schoolInfoStatus(userInfo,schoolId,status);
        return ResponseModel.sucessWithEmptyData("");
    }





}
