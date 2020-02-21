package cn.com.szedu.controller;

import cn.com.szedu.entity.CurriculumResource;
import cn.com.szedu.exception.CourseWareException;
import cn.com.szedu.model.CurriculemResourceModel;
import cn.com.szedu.model.CurriculumModel;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.CurriculumConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@CrossOrigin
@Api("BackExamController相关的api")
@RequestMapping("/BackCurriculum")
public class BackCurriculumController {
    @Resource
    private CurriculumConsoleService curriculumConsoleService;

    @ApiOperation(value = "添加课程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCurriculum")
    public ResponseModel saveCurriculum(@RequestBody CurriculumModel model, @ModelAttribute UserInfoForToken userInfo) {
        curriculumConsoleService.saveCurriculumAndCurriculumResource(model);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "上传资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/uploadCurriculumResource")
    public ResponseModel saveAttachment(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("",  curriculumConsoleService.upLoadFile(userInfo,file));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "本地上传资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/localUploadCurriculumResource")
    public ResponseModel localUpload(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("",curriculumConsoleService.addCurriculumResource(userInfo,file));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "保存章节信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCurriculumResourceChapter")
    public ResponseModel saveCurriculumResource(@ModelAttribute UserInfoForToken userInfo, @RequestBody CurriculumResource curriculumResource) {
        try {
            curriculumConsoleService.addCurriculumResourceInfo(userInfo,curriculumResource);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "本地多文件上传资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/localUploadCurriculumResources")
    public ResponseModel localUploads(@RequestParam MultipartFile[] file, @ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("",curriculumConsoleService.addCurriculumResources(userInfo,file));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "保存章节信息（多文件）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCurriculumResourceChapters")
    public ResponseModel saveCurriculumResources(@ModelAttribute UserInfoForToken userInfo, @RequestBody CurriculemResourceModel model) {
        try {
            curriculumConsoleService.addCurriculumResourceInfos(userInfo,model);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "根据章节查询资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getResourceByChapter")
    public ResponseModel getResourceByChapter(@ModelAttribute UserInfoForToken userInfo,@RequestParam Integer chapterId,@RequestParam Integer curriculumId) {
        return ResponseModel.sucess("",curriculumConsoleService.getResourceByChapter(curriculumId,chapterId));
    }

    @ApiOperation(value = "上传课程图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/uploadQuestionImg")
    public ResponseModel uploadQuestionImg(@RequestParam MultipartFile file,@ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("",curriculumConsoleService.addCurriculumImg(file));
        }catch (IOException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "查询全部课程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAllCurriculum")
    public ResponseModel getAllCuriiculum(@ModelAttribute UserInfoForToken userInfo) {
         return ResponseModel.sucess("",curriculumConsoleService.getCurriculum());
    }

    /*@ApiOperation(value = "添加课程资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCurriculumResource")
    public ResponseModel saveCurriculumResource(@RequestBody CurriculumResource curriculumResource, @ModelAttribute UserInfoForToken userInfo) {
        try {
        curriculumConsoleService.addCurriculumResource(curriculumResource);
        return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }*/
    @ApiOperation(value = "添加学校访问课程权限", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCurriculumPowerBySchool")
    public ResponseModel saveCurriculumPowerBySchool(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolId,@RequestParam Integer curriculumId) {
        try {
            curriculumConsoleService.saveSchoolCurriculumPower(userInfo,schoolId,curriculumId);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "移除学校访问课程权限", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteCurriculumPowerBySchool")
    public ResponseModel deleteCurriculumPowerBySchool(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolId,@RequestParam Integer curriculumId) {
        try {
            curriculumConsoleService.deleteSchoolCurriculumPower(userInfo,curriculumId,schoolId);
            return ResponseModel.sucessWithEmptyData("");
        }catch (CourseWareException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "后台根据学校查询全部课程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAllCurriculumBySchool")
    public ResponseModel getAllCurriculumBySchool(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolId,@RequestParam int num,@RequestParam int size) {
        return ResponseModel.sucess("",curriculumConsoleService.getCurriculumBySchool(userInfo, schoolId, num, size));
    }

    @ApiOperation(value = "教师查询可访问课程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAllCurriculumByTeacher")
    public ResponseModel getAllCurriculumByTeacher(@ModelAttribute UserInfoForToken userInfo,@RequestParam int num,@RequestParam int size) {
        return ResponseModel.sucess("",curriculumConsoleService.getCurriculumByTeacherId(userInfo, num, size));
    }
}
