package cn.com.szedu.controller;

import cn.com.szedu.entity.Course;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.BackCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Course")
@Api("CourseConsoleController相关的api")
@CrossOrigin
public class CourseConsoleController {
    @Resource
    private BackCourseService backCourseService;

    @ApiOperation(value = "添加课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCourse")
    public ResponseModel saveCourse(@RequestBody Course course, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backCourseService.addCourse(userInfo,course);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "修改课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateCourse")
    public ResponseModel updateCourse(@RequestBody Course course, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backCourseService.updateCourse(course);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "删除课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteCourse")
    public ResponseModel deleteCourse(@RequestParam String id, @ModelAttribute UserInfoForToken userInfo) {
        try {
            backCourseService.deleteCourse(userInfo,id);
            return ResponseModel.sucessWithEmptyData("");
        }catch (UserServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "查询课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getAllCourse")
    public ResponseModel getAllCourse(@ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",backCourseService.getCourseList(name,pageNum,pageSize));
    }


}
