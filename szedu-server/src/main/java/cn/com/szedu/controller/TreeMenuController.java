package cn.com.szedu.controller;

import cn.com.szedu.entity.Menu;
import cn.com.szedu.exception.UserServiceException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(description = "树状菜单接口API")
@CrossOrigin
@RequestMapping("/TreeMenu")
public class TreeMenuController {

    @Resource
    private MenuService menuService;

    @ApiOperation(value = "保存节点信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/saveTreeNode")
    public ResponseModel saveTreeNode(@ModelAttribute UserInfoForToken userInfo, @RequestBody Menu model){
        try {
            menuService.addNode(model);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "修改节点信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/updateTreeNode")
    public ResponseModel updateTreeNode(@ModelAttribute UserInfoForToken userInfo, @RequestParam Integer id,@RequestParam String title){
        try {
            menuService.updateNode(id,title);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "删除节点信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/deleteTreeNode")
    public ResponseModel deleteTreeNode(@ModelAttribute UserInfoForToken userInfo, @RequestParam Integer id){
        try {
            menuService.deleteNode(id);
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据条件查节点", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getTreeMenuByPid")
    public ResponseModel<List<Menu>> getTreeMenuByPid(@ModelAttribute UserInfoForToken userInfo, @RequestParam Integer pid){
        try {
            return ResponseModel.sucess("",menuService.getMenuByPid(pid));
        } catch (UserServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "查询全部节点", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getAllTreeMenu")
    public ResponseModel<List<Menu>> getAllTreeMenu(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",menuService.getAllMenu());
    }
}
