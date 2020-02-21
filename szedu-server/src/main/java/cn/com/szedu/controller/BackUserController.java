package cn.com.szedu.controller;

import cn.com.szedu.entity.BackUser;
import cn.com.szedu.entity.SystemMessage;
import cn.com.szedu.exception.BackUserException;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.BackUserLoginModel;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.BackUserService;
import cn.com.szedu.service.IBackSchoolService;
import cn.com.szedu.service.OpLogService;
import cn.com.szedu.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Api("后台用户相关功能")
@RestController
@RequestMapping("/backuser")
@CrossOrigin
public class BackUserController {
    @Resource
    private BackUserService backUserService;
    @Resource
    private IBackSchoolService backSchoolService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private OpLogService opLogService;

    @ApiOperation("后台用户注册")
    @ApiImplicitParams({})
    @PostMapping("/registBackUser")
    public ResponseModel registBackUser(@RequestBody BackUser backUser){
        try {
            backUserService.registBackUser(backUser);
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("后台用户登录")
    @ApiImplicitParams({})
    @PostMapping("/loginBackUser")
    public ResponseModel loginBackUser(@RequestBody BackUserLoginModel model){
        try {
            //return ResponseModel.sucess("",backUserService.loginBackUser(model));
            return ResponseModel.sucess("",userInfoService.loginBackUser(model));
        }catch (UserServiceException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("获取登录用户信息")
    @ApiImplicitParams({})
    @PostMapping("/getLoginBackUserInfo")
    public ResponseModel getLoginBackUserInfo(@ModelAttribute UserInfoForToken userInfoForToken){
        try {
            return ResponseModel.sucess("",backUserService.getBackUserById(userInfoForToken));
        }catch (BackUserException e){
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation("分页查询行为日志列表")
    @ApiImplicitParams({})
    @GetMapping("/getOpLogByNameAndPage")
    public ResponseModel getOpLogByNameAndPage(@ModelAttribute UserInfoForToken userInfo, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam(required = false) String name){
        return ResponseModel.sucess("",opLogService.getOpLogByNameAndPage(pageNum,pageSize,name));
    }


    @ApiOperation(value = "根据id删除行为日志", notes = "")
    @ApiImplicitParams({})
    @GetMapping("/deleteOpLog")
    public ResponseModel deleteOpLog(@ModelAttribute UserInfoForToken userInfo, @RequestParam Integer opId){
        opLogService.deleteOplog(opId);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "添加管理员", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveAdmin")
    public ResponseModel<Boolean> saveAdmin(@RequestBody BackUser user, @ModelAttribute UserInfoForToken userInfo) throws UserServiceException {

        return ResponseModel.sucess("", backSchoolService.saveAdmin(user,userInfo));
    }

    @ApiOperation(value = "更改管理员", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateAdmin")
    public ResponseModel<Boolean> updateAdmin(@RequestBody BackUser user, @ModelAttribute UserInfoForToken userInfo) throws UserServiceException {
        return ResponseModel.sucess("", backSchoolService.updateAdmin(user,userInfo));
    }

    @ApiOperation(value = "删除管理员", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/deleteAdmin")
    public ResponseModel<Boolean> deleteAdmin(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id) throws UserServiceException {
        return ResponseModel.sucess("", backSchoolService.deleteAdmin(userInfo,id));
    }

    @ApiOperation(value = "冻结管理员", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/frozenAdmin")
    public ResponseModel<Boolean> frozenAdmin(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id,@RequestParam String status) throws UserServiceException {
        return ResponseModel.sucess("", backSchoolService.frozenAdmin(userInfo,id,status));
    }

    @ApiOperation(value = "修改管理员密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/adminPwd")
    public ResponseModel<Boolean> adminPwd(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id,@RequestParam String pwd) throws UserServiceException {
        return ResponseModel.sucess("", backSchoolService.adminPwd(userInfo,id,pwd));
    }

    @ApiOperation(value = "管理员发送系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/sendSystemAdmin")
    public ResponseModel<Boolean> sendSystemAdmin(@ModelAttribute UserInfoForToken userInfo, @RequestBody SystemMessage message) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.sendSystemAdmin(userInfo,message));
    }

   /* @ApiOperation(value = "管理员查看系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getSystemAdmin")
    public ResponseModel getSystemAdmin(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolId,@RequestParam String admin,@RequestParam int pageNum,@RequestParam int pageSize) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.getSystemAdmin(userInfo,schoolId,admin,pageNum,pageSize));
    }*/

    @ApiOperation(value = "管理员查看系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getSystemAdmin")
    public ResponseModel getSystemAdmin(@ModelAttribute UserInfoForToken userInfo,@RequestParam int pageNum,@RequestParam int pageSize) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.getSystemAdmin(userInfo,pageNum,pageSize));
    }

   /* @ApiOperation(value = "管理员所有系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/getSystemSchoolId")
    public ResponseModel getSystemSchoolId(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolId,@RequestParam int pageNum,@RequestParam int pageSize) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.getSystemSchoolId(userInfo,schoolId,pageNum,pageSize));
    }*/

    @ApiOperation(value = "查询审核系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/checkSystem")
    public ResponseModel checkSystem(@ModelAttribute UserInfoForToken userInfo,@RequestParam String schoolId,@RequestParam int pageNum,@RequestParam int pageSize) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.checkMessage(userInfo,schoolId,pageNum,pageSize));
    }

    @ApiOperation(value = "审核系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/updateCheckMessage")
    public ResponseModel<Boolean> updateCheckMessage(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id,@RequestParam int check) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.updateCheckMessage(userInfo,id,check));
    }

    @ApiOperation(value = "删除系统信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping("/deleteCheckMessage")
    public ResponseModel<Boolean> deleteCheckMessage(@ModelAttribute UserInfoForToken userInfo,@RequestParam String id) throws UserServiceException {
        return ResponseModel.sucess("", backUserService.deleteCheckMessage(userInfo,id));
    }


}
