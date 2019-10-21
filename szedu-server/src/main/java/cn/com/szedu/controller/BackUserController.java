package cn.com.szedu.controller;

import cn.com.szedu.entity.BackUser;
import cn.com.szedu.exception.BackUserException;
import cn.com.szedu.model.BackUserLoginModel;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.BackUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("后台用户相关功能")
@RestController
@RequestMapping("/backuser")
@CrossOrigin
public class BackUserController {
    @Resource
    private BackUserService backUserService;

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
            return ResponseModel.sucess("",backUserService.loginBackUser(model));
        }catch (BackUserException e){
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
}
