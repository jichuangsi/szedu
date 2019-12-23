package cn.com.szedu.controller.teacher;

import cn.com.szedu.entity.Course;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceCourseModel;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.model.teacher.TeacherCourseModel;
import cn.com.szedu.model.teacher.TeacherLessonModel;
import cn.com.szedu.service.TeacherLessonService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/TeacherLesson")
@Api("教师课堂相关的api")
@CrossOrigin
public class TeacherLessonController {
    @Resource
    private TeacherLessonService teacherLessonService;

    @ApiOperation(value = "我的课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/teacherLesson")
    public ResponseModel<PageInfo<TeacherLessonModel>> TeacherLesson(@ModelAttribute UserInfoForToken userInfo,
          @RequestParam String name, @RequestParam String lessionType, @RequestParam Date time,
           @RequestParam int pageNum, @RequestParam int PageSize)throws TecherException {

        return  ResponseModel.sucess("",teacherLessonService.getAllLesson(userInfo, name, lessionType, time, pageNum, PageSize));
    }

    @ApiOperation(value = "删除课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteLesson")
    public ResponseModel deleteLesson(@ModelAttribute UserInfoForToken userInfo ,@RequestParam String lessonId) throws TecherException{
        teacherLessonService.deleteLession(userInfo, lessonId);
        return  ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation(value = "添加课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addLesson")
    public ResponseModel addLesson(@ModelAttribute UserInfoForToken userInfo ,@RequestBody TeacherLessonModel model) throws TecherException{
            teacherLessonService.addLesson(userInfo, model);
        return  ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation(value = "上传课堂图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/uploadCourseImg")
    public ResponseModel uploadQuestionImg(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("",teacherLessonService.addCourseImg(file));
        }catch (IOException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }


    @ApiOperation(value = "课堂详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getCourseDetail")
    public ResponseModel<TeacherLessonModel> getCourseDetail(@ModelAttribute UserInfoForToken userInfo, @RequestParam String courseId) throws TecherException{
        teacherLessonService.getCourseDetail(userInfo, courseId);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "改变课堂状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateCourseStatus")
    public ResponseModel updateCourseStatus(@ModelAttribute UserInfoForToken userInfo, @RequestParam String courseId, @RequestParam String staus) throws TecherException{
        teacherLessonService.updateCourseStatus(userInfo, courseId,staus);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "复制课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/copyLession")
    public ResponseModel copyLession(@ModelAttribute UserInfoForToken userInfo, @RequestBody Course course) throws TecherException{
        teacherLessonService.copyLession(userInfo,course);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "学生考勤", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAttendanceByCourse")
    public ResponseModel<PageInfo<AttendanceCourseModel>> getAttendanceByCourse(@ModelAttribute UserInfoForToken userInfo,@RequestParam String couseId,@RequestParam int pageNum, @RequestParam int PageSize) throws TecherException{
        return ResponseModel.sucess("",teacherLessonService.getAttendanceByCourse(userInfo, couseId, pageNum, PageSize));
    }

}
