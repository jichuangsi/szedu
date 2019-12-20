package cn.com.szedu.controller;

import cn.com.szedu.entity.Exam;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.BackExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@Api("BackExamController相关的api")
@RequestMapping("/backExam")
public class BackExamController {
    @Resource
    private BackExamService backExamService;

    @ApiOperation(value = "添加考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveExam")
    public ResponseModel saveExam(@RequestBody Exam exam, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backExamService.addExam(userInfo,exam);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "修改考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateExam")
    public ResponseModel updateExam(@RequestBody Exam exam, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backExamService.updateExam(exam);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "删除考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteExam")
    public ResponseModel deleteExam(@RequestParam String id, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backExamService.deleteExam(userInfo,id);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "查询考试", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllExam")
    public ResponseModel getAllExam(@ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",backExamService.getExamList(name,pageNum,pageSize));
    }
}
