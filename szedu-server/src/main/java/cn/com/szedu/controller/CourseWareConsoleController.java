package cn.com.szedu.controller;

import javax.annotation.Resource;

import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.entity.CourseWareShare;
import cn.com.szedu.exception.CourseWareException;
import cn.com.szedu.model.*;
import cn.com.szedu.model.teacher.PushResourceModel;
import cn.com.szedu.service.CourseWareConsoleService;
import cn.com.szedu.service.CourseWareShareService;
import cn.com.szedu.service.IUserPositionService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/CourseWare")
@Api("CourseWareConsoleController相关的api")
@CrossOrigin
public class CourseWareConsoleController {
    @Resource
    private CourseWareConsoleService courseWareConsoleService;

    @ApiOperation(value = "FastDfs上传附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveAttachment")
    public ResponseModel saveAttachment(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("",  courseWareConsoleService.upLoadFile(userInfo,file));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "保存附件相关信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCourse")
    public ResponseModel saveCourse(@RequestBody CourseWare courseWare, @ModelAttribute UserInfoForToken userInfo) {
        //courseWareConsoleService.saveCourseWare(courseWare);
        courseWareConsoleService.addCourseWareByLoacal(userInfo,courseWare);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据老师查询附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getCourseWareListByTeacher")
    public ResponseModel<Page<CourseWare>> getCourseWareListByTeacher(@ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) String type,@RequestParam(required = false) String label, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.getCouserWareByTeacherIdAndPage(userInfo,type,label,pageNum,pageSize));
    }

    @ApiOperation(value = "统计", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/teacherResourseStatistics")
    public ResponseModel<PageInfo<UploadResouseModel>> teacherResourseStatistics(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.teacherResourseStatistics(pageNum,pageSize));
    }

    @ApiOperation(value = "FastDfs下载附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/downLoadAttachment")
    public void downLoadAttachment(@ModelAttribute UserInfoForToken userInfo, @RequestParam String fileId,HttpServletResponse response)throws CourseWareException {
        courseWareConsoleService.downCourseWare(fileId,response);
    }

    @ApiOperation(value = "FastDfs删除附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteAttachment")
    public ResponseModel deleteAttachment(@ModelAttribute UserInfoForToken userInfo,@RequestParam String fileId) {
        try {
            courseWareConsoleService.deleteCourseWare(fileId);
            return ResponseModel.sucessWithEmptyData("");
        }catch (CourseWareException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询审核资源列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getCourseWareList")
    public ResponseModel<PageInfo<CourseModel>> getCourseWareList(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.getCouserWareByPage(pageNum,pageSize));
    }

    @ApiOperation(value = "查询分享审核资源列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getShareCourseWareList")
    public ResponseModel<PageInfo<CourseModel>> getShareCourseWareList(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.getShareCouserWareByPage(pageNum,pageSize));
    }

    @ApiOperation(value = "上传资源审核", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/resourceCheck")
    public ResponseModel resourceCheck(@ModelAttribute UserInfoForToken userInfo, @RequestParam String resourceId, @RequestParam String status){
        try {
            courseWareConsoleService.updateIsCheck(resourceId,status);
            return ResponseModel.sucessWithEmptyData("");
        }catch (CourseWareException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "分享资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/shareResource")
    public ResponseModel shareResource(@ModelAttribute UserInfoForToken userInfo, @RequestParam String resourceId, @RequestParam String status,@RequestParam Integer integral){
        try {
            courseWareConsoleService.updateIsShareCheckAndIntegral(resourceId,status,integral);
            return ResponseModel.sucessWithEmptyData("");
        }catch (CourseWareException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "分享资源审核", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/shareResourceCheck")
    public ResponseModel shareResourceCheck(@ModelAttribute UserInfoForToken userInfo, @RequestParam String resourceId, @RequestParam String status){
        try {
            courseWareConsoleService.updateIsShareCheck(resourceId,status);
            return ResponseModel.sucessWithEmptyData("");
        }catch (CourseWareException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "积分修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/updateIntegral")
    public ResponseModel updateIntegral(@ModelAttribute UserInfoForToken userInfo, @RequestParam String resourceId, @RequestParam Integer integral){
        try {
            courseWareConsoleService.updateIntegral(resourceId,integral);
            return ResponseModel.sucessWithEmptyData("");
        }catch (CourseWareException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询课件模板", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getCourseWareTemplate")
    public ResponseModel deleteShareAttachment(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",courseWareConsoleService.findCourseWareByFileName());
    }

    @ApiOperation(value = "查询资源类型", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getResourcesRule")
    public ResponseModel getResourcesRule(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",courseWareConsoleService.findResourcesRule(userInfo));
    }

    @ApiOperation(value = "查询资源标签", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getUploadLabel")
    public ResponseModel getUploadLabel(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",courseWareConsoleService.getUploadLabel(userInfo));
    }

    @ApiOperation(value = "本地上传资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/localUpload")
    public ResponseModel localUpload(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo,@RequestParam Integer resourceType,@RequestParam(required = false) String resourceId) {
        try {
            return ResponseModel.sucess("",courseWareConsoleService.localUpload(userInfo,file,resourceType,resourceId));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "本地上传封面", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/localUploadCover")
    public ResponseModel localUploadCover(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) String resourceId) {
        try {
            return ResponseModel.sucess("",courseWareConsoleService.localUploadCover(userInfo,file,resourceId));
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "本地下载", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/localDownload")
    public void localDownload(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id,HttpServletResponse response) {
        try {
            courseWareConsoleService.localDownLoad(id,response);
        } catch (CourseWareException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "根据资源id删除资源(本地)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteResourcesByid")
    public ResponseModel deleteResourcesByid(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id) {
        try {
            courseWareConsoleService.deleteCourseWareById(id);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "购买资源(本地复制)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/buyResourcesByid")
    public ResponseModel buyResourcesByid(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id) {
        try {
            courseWareConsoleService.copyResource(userInfo,id);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "推送资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/pushResourceToClass")
    public ResponseModel pushResourceToClass(@ModelAttribute UserInfoForToken userInfo,@RequestBody PushResourceModel model) {
        try {
            courseWareConsoleService.pushResouceToClass(userInfo,model);
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "公共资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getPublicResource")
    public ResponseModel getPublicResource(@ModelAttribute UserInfoForToken userInfo,@RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.getPublicResource(userInfo,pageNum,pageSize));
    }

    @ApiOperation(value = "公共资源（根据时间）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getPublicResourceByTime")
    public ResponseModel getPublicResourceByTime(@ModelAttribute UserInfoForToken userInfo,@RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.getPublicResourceByTime(userInfo,pageNum,pageSize));
    }

    @ApiOperation(value = "获取老师分享购买记录", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getBuyShareList")
    public ResponseModel getBuyShareList(@ModelAttribute UserInfoForToken userInfo,@RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.getBuyShareByUser(userInfo,pageNum,pageSize));
    }
}
