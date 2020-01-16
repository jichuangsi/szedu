package cn.com.szedu.controller.teacher;

import cn.com.szedu.entity.AttendanceInClass;
import cn.com.szedu.entity.ClassInfo;
import cn.com.szedu.entity.StudentInfo;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.StudentModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.AttendanceModel;
import cn.com.szedu.model.teacher.ClassModel;
import cn.com.szedu.model.teacher.TeacherAddClassModel;
import cn.com.szedu.model.teacher.TeacherModel;
import cn.com.szedu.service.AttendanceInClassService;
import cn.com.szedu.service.StudentService;
import cn.com.szedu.service.TecherClassService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/classConsole")
@Api("班级相关的api")
@CrossOrigin
public class ClassConsoleController {
    @Resource
    private TecherClassService techerClassService;
    @Resource
    private StudentService studentService;
    @Resource
    private AttendanceInClassService attendanceInClassService;

    @ApiOperation(value = "创建班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addClass")
    public ResponseModel addClass(@ModelAttribute UserInfoForToken userInfo,@RequestBody ClassModel model) throws UserServiceException {
        techerClassService.insertClass(userInfo,model);
        //return  ResponseModel.sucess("",techerClassService.insertClass(userInfo,model));
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查询所有班级(用于添加未读班级)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAddClass")
    public ResponseModel< List<TeacherAddClassModel>> getAddClass(@ModelAttribute UserInfoForToken userInfo)throws TecherException {
        return  ResponseModel.sucess("",techerClassService.getAddClass(userInfo));
    }
    @ApiOperation(value = "我的班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getClassByTeacherId")
    public ResponseModel<PageInfo<ClassModel>> getClassByTeacherId(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum , @RequestParam int pageSize)throws UserServiceException {

        return  ResponseModel.sucess("",techerClassService.getAllClass(userInfo,pageNum,pageSize));
    }

    @ApiOperation(value = "新增我的班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addTeacherClass")
    public ResponseModel addTeacherClass(@ModelAttribute UserInfoForToken userInfo,@RequestBody String[] classId) throws UserServiceException {
        techerClassService.addTeacherClass(userInfo,classId);
        return  ResponseModel.sucessWithEmptyData("");
    }
    @ApiOperation(value = "修改班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateClass")
    public ResponseModel updateClass(@ModelAttribute UserInfoForToken userInfo,@RequestBody ClassModel model) throws UserServiceException {
       techerClassService.upadteClass(userInfo,model);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改班级状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/updateClassStatus")
    public ResponseModel updateClassStatus(@ModelAttribute UserInfoForToken userInfo,@RequestParam String classId,@RequestParam String status) throws UserServiceException {
        techerClassService.upadteClassStatus(userInfo, classId, status);
        return  ResponseModel.sucessWithEmptyData("");
        //return  ResponseModel.sucess("",techerClassService.upadteClassStatus(userInfo, classId, status));
    }

    @ApiOperation(value = "删除班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteClass")
    public ResponseModel deleteClass(@ModelAttribute UserInfoForToken userInfo,@RequestParam String classId) throws UserServiceException {
        techerClassService.deleteClass(userInfo, classId);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "移除班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/removeClass")
    public ResponseModel removeClass(@ModelAttribute UserInfoForToken userInfo,@RequestParam String classId) throws TecherException {
        techerClassService.removeClass(userInfo, classId);
        return  ResponseModel.sucessWithEmptyData("");
    }




    @ApiOperation(value = "新增学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addStudent")
    public ResponseModel addStudent(@ModelAttribute UserInfoForToken userInfo,@RequestBody StudentModel model) throws UserServiceException{
        studentService.insertStudent(userInfo,model);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateStudent")
    public ResponseModel updateStudent(@ModelAttribute UserInfoForToken userInfo,@RequestBody StudentModel model) throws UserServiceException{
        studentService.upadteStudent(userInfo,model);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查看学号", notes = "")
    @ApiImplicitParams({})
    @ResponseBody
    @PostMapping("/checkStudentId")
    public boolean checkStudentId(@RequestParam String studentId) throws UserServiceException{
        return  studentService.checkStudentId(studentId);
    }

    @ApiOperation(value = "根据班级查看学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getStudentByClassId")
    public ResponseModel<PageInfo<StudentModel>> getStudentByClassId(@ModelAttribute UserInfoForToken userInfo,@RequestParam String classId, @RequestParam int pageNum , @RequestParam int pageSize)throws UserServiceException {

        return  ResponseModel.sucess("",studentService.getStudentByClassId(userInfo,classId,pageNum,pageSize));
    }

    @ApiOperation(value = "excel批量保存学生信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping(value = "/excel/saveStudentByExcel")
    public ResponseModel<String> saveStudentByExcel(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo,@RequestParam String classId){
        try {
            return ResponseModel.sucess("",studentService.saveExcelStudents(file,userInfo,classId));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "修改学生密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateStudentPwd")
    public ResponseModel updateStudentPwd(@ModelAttribute UserInfoForToken userInfo,@RequestParam String newPwd,@RequestParam String studentId) throws UserServiceException{
        studentService.upadteStudentPwd(userInfo,studentId,newPwd);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据学生Id查看学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getStudentById")
    public ResponseModel<StudentModel> getStudentById(@ModelAttribute UserInfoForToken userInfo, @RequestParam String id)throws UserServiceException {
        return  ResponseModel.sucess("",studentService.getStudentById(userInfo,id));
    }

    @ApiOperation(value = "给学生添加进新班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addClassByStudent")
    public ResponseModel addClassByStudent(@ModelAttribute UserInfoForToken userInfo,@RequestParam String[] classId,@RequestParam String studentId) throws UserServiceException{
        studentService.addClassByStudent(userInfo,classId,studentId);
        return  ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "移动学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/addRemoveStudent")
    public ResponseModel addRemoveStudent(@ModelAttribute UserInfoForToken userInfo,@RequestParam String oldClassId,@RequestParam String classId,@RequestParam String studentId) throws TecherException{
        studentService.addRemoveStudent(userInfo,oldClassId,classId,studentId);
        return  ResponseModel.sucessWithEmptyData("");
    }


    /*@ApiOperation(value = "学生考勤", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAttendanceByName")
    public ResponseModel<PageInfo<AttendanceModel>> getAttendanceByName (@ModelAttribute UserInfoForToken userInfo,@RequestParam String studentName , @RequestParam int pageNum , @RequestParam int pageSize)throws UserServiceException {
        return  ResponseModel.sucess("",attendanceInClassService.getAttendanceByName(userInfo,studentName,pageNum,pageSize));
    }*/
    @ApiOperation(value = "学生考勤", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAttendance")
    public ResponseModel<PageInfo<AttendanceModel>> getAttendance (@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum , @RequestParam int pageSize)throws UserServiceException {
        return  ResponseModel.sucess("",attendanceInClassService.getAttendance(userInfo,pageNum,pageSize));
    }

    @ApiOperation(value = "查询所有班级", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getAllClass")
    public ResponseModel<List<ClassInfo>> getAllClass (@ModelAttribute UserInfoForToken userInfo)throws UserServiceException {
        return  ResponseModel.sucess("",techerClassService.getAllClass(userInfo));
    }
}
