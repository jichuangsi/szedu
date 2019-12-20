package cn.com.szedu.controller;

import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.SchoolInfoModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.IBackSchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
}
