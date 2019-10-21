package cn.com.szedu.controller;

import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UrlModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.service.BackRoleUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("后台角色相关")
@CrossOrigin
@RestController
@RequestMapping("/backRoleConsole")
public class BackRoleConsoleController {
    @Resource
    private BackRoleUrlService backRoleUrlService;

    @ApiOperation("获取全部父节点")
    @ApiImplicitParams({})
    @GetMapping("/getAllParentNode")
    public ResponseModel getParentNodes(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backRoleUrlService.getAllParentNode());
    }

    @ApiOperation("获取全部页面")
    @ApiImplicitParams({})
    @GetMapping("/getAllStaticPage")
    public ResponseModel getAllStaticPage(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backRoleUrlService.getAllStaticPage());
    }

    /*@ApiOperation("批量增加角色可以访问的页面")
    @ApiImplicitParams({})
    @PostMapping("/batchAddRoleUrl")
    public ResponseModel batchAddRoleUrl(@ModelAttribute UserInfoForToken userInfo,@RequestBody UrlModel model){
        backRoleUrlService.batchInsertUrlRelation (model.getRelationList());
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("批量删除角色可以访问的页面")
    @ApiImplicitParams({})
    @PostMapping("/batchDeleteRoleUrl")
    public ResponseModel batchDeleteRoleUrl(@ModelAttribute UserInfoForToken userInfo,@RequestBody UrlModel model){
        backRoleUrlService.batchDeleteUrlRelation (model.getRelationList());
        return ResponseModel.sucessWithEmptyData("");
    }*/

    @ApiOperation("批量增加角色可以访问的页面")
    @ApiImplicitParams({})
    @PostMapping("/batchUpdateRoleUrl")
    public ResponseModel batchUpdateRoleUrl(@ModelAttribute UserInfoForToken userInfo, @RequestBody UrlModel model){
        backRoleUrlService.batchUpdateUrlRelation (model);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation("获取角色可访问的页面")
    @ApiImplicitParams({})
    @GetMapping("/getStaticPageByRoleId")
    public ResponseModel getStaticPageByRoleId(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backRoleUrlService.getStaticpageByRoleId(userInfo));
    }

    @ApiOperation("根据角色id获取可访问的页面")
    @ApiImplicitParams({})
    @GetMapping("/getJurisdictionByRoleId")
    public ResponseModel getJurisdictionByRoleId(@ModelAttribute UserInfoForToken userInfo, @RequestParam String roleId){
        return ResponseModel.sucess("",backRoleUrlService.getStaticpageByRoleId2(roleId));
    }

    @ApiOperation("获取全部角色")
    @ApiImplicitParams({})
    @GetMapping("/getAllRole")
    public ResponseModel getAllRole(@ModelAttribute UserInfoForToken userInfo){
        return ResponseModel.sucess("",backRoleUrlService.getAllRole());
    }

    @ApiOperation("检查权限")
    @ApiImplicitParams({})
    @GetMapping("/checkJurisdiction")
    public ResponseModel checkJurisdiction(@ModelAttribute UserInfoForToken userInfo, @RequestParam String url){
        return ResponseModel.sucess("",backRoleUrlService.checkUrl(userInfo,url));
    }
}
