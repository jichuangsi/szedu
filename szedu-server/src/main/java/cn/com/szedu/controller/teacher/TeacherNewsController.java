package cn.com.szedu.controller.teacher;

import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.model.teacher.NewsModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/TeacherNews")
@Api("新闻相关的api")
@CrossOrigin
public class TeacherNewsController {

    @ApiOperation(value = "新闻", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getNewsByTeacher")
    public ResponseModel<List<NewsModel>> getNewsByTeacher(@ModelAttribute UserInfoForToken userInfo) {
        List<NewsModel> modelList=new ArrayList<>();
        return  ResponseModel.sucess("",modelList);
    }

    @ApiOperation(value = "新闻详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getNewsDetail")
    public ResponseModel<NewsModel> getNewsDetail(@ModelAttribute UserInfoForToken userInfo) {
        NewsModel model=new NewsModel();
        return  ResponseModel.sucess("",model);
    }
}
