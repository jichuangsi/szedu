package cn.com.szedu.controller;

import javax.annotation.Resource;

import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.entity.CourseWareShare;
import cn.com.szedu.exception.CourseWareException;
import cn.com.szedu.model.*;
import cn.com.szedu.service.CourseWareConsoleService;
import cn.com.szedu.service.CourseWareShareService;
import cn.com.szedu.service.IUserPositionService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Resource
    private IUserPositionService userPositionService;
    @Resource
    private CourseWareShareService courseWareShareService;

    @ApiOperation(value = "上传附件", notes = "")
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
        courseWareConsoleService.saveCourseWare(courseWare);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据老师查询附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getCourseWareList/{teacherId}")
    public ResponseModel<Page<CourseWare>> getCourseWareList(@ModelAttribute UserInfoForToken userInfo, @PathVariable String teacherId, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.getCouserWareByTeacherIdAndPage(teacherId,pageNum,pageSize));
    }

    @ApiOperation(value = "统计", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/teacherResourseStatistics")
    public ResponseModel<PageInfo<UploadResouseModel>> teacherResourseStatistics(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.teacherResourseStatistics(pageNum,pageSize));
    }

    @ApiOperation(value = "下载附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/downLoadAttachment")
    public void downLoadAttachment(@ModelAttribute UserInfoForToken userInfo, @RequestParam String fileId,HttpServletResponse response)throws CourseWareException {
        courseWareConsoleService.downCourseWare(fileId,response);
    }

    @ApiOperation(value = "删除附件", notes = "")
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

    @ApiOperation(value = "资源审核", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/resourceCheck")
    public ResponseModel resourceCheck(@ModelAttribute UserInfoForToken userInfo, @RequestParam String resourceId, @RequestParam String status,@RequestParam String integral){
        try {
            courseWareConsoleService.updateIsCheck(resourceId,status,integral);
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

    @ApiOperation(value = "查询资源列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getCourseWareList2")
    public ResponseModel<PageInfo<CourseModel>> getCourseWareList2(@ModelAttribute UserInfoForToken userInfo,@RequestParam(required = false) String name, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResponseModel.sucess("",courseWareConsoleService.getCouserWareByPage2(name,pageNum,pageSize));
    }


    /*@ApiOperation(value = "查询年级和班级,课件id", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getShareElements")
    public ResponseModel<ShareElements> getShareElements(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestParam String schoolId, @RequestParam String teacherId){
        return ResponseModel.sucess("", courseWareConsoleService.getShareElements(userInfo,schoolId,teacherId));
    }*/

    @ApiOperation(value = "保存分享相关信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/SaveShareElements")
    public ResponseModel saveShareElements(@ModelAttribute @NotNull UserInfoForToken userInfo, @RequestBody CourseWareShare courseWareShare){
        courseWareShareService.saveCourseWareShare(courseWareShare);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据老师查询分享附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getCourseWareShareList/{teacherId}")
    public ResponseModel<List<CourseWareShare>> getCourseWareShareList(@ModelAttribute UserInfoForToken userInfo,@PathVariable String teacherId) {
        //return ResponseModel.sucess("",courseWareConsoleService.findCourseWare(courseWareShareService.findcourseWareIdsBy(userPositionService.findUserById(userInfo,teacherId)),pageNum,pageSize));
        return ResponseModel.sucess("",courseWareShareService.findcourseWareIdsBy(userPositionService.findUserById(userInfo,teacherId)));
    }

    @ApiOperation(value = "删除分享的附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/deleteShareAttachment/{shareAttachmentId}")
    public ResponseModel deleteShareAttachment(@ModelAttribute UserInfoForToken userInfo,@PathVariable String shareAttachmentId) {
        courseWareShareService.deleteCourseWareShare(shareAttachmentId);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查询课件模板", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getCourseWareTemplate")
    public ResponseModel deleteShareAttachment(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",courseWareConsoleService.findCourseWareByFileName());
    }

    @ApiOperation(value = "本地上传", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveHttpAttachment")
    public ResponseModel upload(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) {
        try {
            //获取文件名
            String fileName = file.getOriginalFilename();
            //获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //重新生成文件名
            fileName =UUID.randomUUID()+suffixName;
            //指定本地文件夹存储图片
            String filePath = "D:/SpringBoot/demo/src/main/resources/static/";
            file.transferTo(new File(filePath+fileName));
            return ResponseModel.sucessWithEmptyData("");
        }catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }
}
