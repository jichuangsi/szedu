package cn.com.szedu.controller;

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
}
