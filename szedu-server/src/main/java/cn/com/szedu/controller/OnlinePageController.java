package cn.com.szedu.controller;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.exception.TecherException;
import cn.com.szedu.model.ResponseModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.ICourseWareRespository;
import cn.com.szedu.service.CourseWareConsoleService;
import cn.com.szedu.service.TeacherLessonService;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@Api("在线编辑")
@RequestMapping("/page")
public class OnlinePageController {
    @Value("${posyspath}")
    private String poSysPath;
    @Value("${popassword}")
    private String poPassWord;
    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Resource
    private TeacherLessonService teacherLessonService;
    @Resource
    private ICourseWareRespository courseWareRespository;
    @Resource
    private CourseWareConsoleService courseWareConsoleService;

    private static Map<String,String> map=new HashMap<String,String>();

    public static Map<String, String> getMap() {
        return map;
    }

    public static void setMap(Map<String, String> map) {
        OnlinePageController.map = map;
    }

    @ApiOperation(value = "在线制作文档word", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/onlineProduction")
    public ResponseModel onlineProduction(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseWare model, @RequestParam String type, HttpServletRequest request, Map<String, Object> map) throws TecherException {
        if (StringUtils.isEmpty(userInfo) || StringUtils.isEmpty(model)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        CourseWare courseWare = courseWareRespository.save(model);
        return ResponseModel.sucess("", courseWare.getId());
    }

    @ApiOperation(value = "本地上传封面", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/localUploadCover")
    public ResponseModel localUploadCover(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo, @RequestParam String id) {
        try {
            return ResponseModel.sucess("", courseWareConsoleService.localUploadCover(userInfo, file, id));
        } catch (Exception e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }


    @ApiOperation(value = "根据条件查找相关文档", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getDoc")
    public ResponseModel<List<CourseWare>> getDoc(@ModelAttribute UserInfoForToken userInfo, @RequestParam String type) throws TecherException {
        if (StringUtils.isEmpty(userInfo)) {
            throw new TecherException(ResultCode.PARAM_MISS_MSG);
        }
        return ResponseModel.sucess("", teacherLessonService.getMyResoure(userInfo, type));

    }








    @RequestMapping("/hello")
    public String test() {
        System.out.println("hello run");
        return "Hello";
    }

    @RequestMapping(value="/index", method=RequestMethod.GET)
    public ModelAndView showIndex(String id, String path, String type){
        ModelAndView mv = new ModelAndView("Index");
       /* mv.addObject("name","张三");*/
        mv.addObject("path", path);
        mv.addObject("id", id);
        mv.addObject("type", type);
        map.put("path",path);
        map.put("teacherid",id);
        map.put("type",type);
        return mv;
    }

   /* @RequestMapping(value="/word", method=RequestMethod.GET)
    public ModelAndView showWord(HttpServletRequest request, Map<String,Object> map){
        //--- PageOffice的调用代码 开始 -----
        PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
        poCtrl.setServerPage("/poserver.zz");//设置授权程序servlet
        poCtrl.addCustomToolButton("保存","Save",1); //添加自定义按钮
        poCtrl.addCustomToolButton("关闭","Close",21);//关闭
        poCtrl.setSaveFilePage("/save");//设置保存的action
        poCtrl.webOpen("d:\\test\\test.doc",OpenModeType.docAdmin,"张三");
        map.put("pageoffice",poCtrl.getHtmlCode("PageOfficeCtrl1"));
        //--- PageOffice的调用代码 结束 -----
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }*/





    @RequestMapping(value = "/word", method = RequestMethod.GET)
    public ModelAndView showWord(HttpServletRequest request, Map<String, Object> m) {
        if (map.size()<=0){ }
        String teacherid=map.get("teacherid");
        String type=map.get("type");
        String path=map.get("path");

        ModelAndView mv = null;
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        String str2 = path.substring(1, path.length());
        System.out.println(uploadPath);
        System.out.println(str2);
        System.out.println(uploadPath+path);
       String uploadPath2= uploadPath.replaceAll("/","\\\\");//d:/===>d:\\
        String path2= str2.replaceAll("/","\\\\");///===>\\
        //菜单栏设置
        poCtrl.setTitlebar(false); //隐藏标题栏
        poCtrl.setMenubar(false); //隐藏菜单栏
        poCtrl.setOfficeToolbars(false);//隐藏Office工具条
        poCtrl.setCustomToolbar(false);//隐藏自定义工具栏


        //设置并发控制时间
        poCtrl.setTimeSlice(20);
        poCtrl.setServerPage("/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("保存", "Save", 1);//添加自定义保存按钮
        poCtrl.addCustomToolButton("设置缩放", "SetPenZoom()", 5);
        poCtrl.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
        poCtrl.addCustomToolButton("关闭", "Close", 21);
        poCtrl.setSaveFilePage("/page/save");//设置处理文件保存的请求方法uploadPath+path
        //打开word
        if (type.equalsIgnoreCase("word")) {
            poCtrl.webOpen(uploadPath2+path2, OpenModeType.docNormalEdit, teacherid);
            m.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
            mv = new ModelAndView("Word");

        } else if (type.equalsIgnoreCase("excel")) {
            poCtrl.webOpen(uploadPath2+path2, OpenModeType.xlsNormalEdit,teacherid);
            m.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
            mv = new ModelAndView("excel");

        }
        if (type.equalsIgnoreCase("ppt")) {
            poCtrl.webOpen(uploadPath2+path2, OpenModeType.pptNormalEdit,teacherid);
            m.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
            mv = new ModelAndView("ppt");
        }
        mv.addObject("teacherid", teacherid);
        mv.addObject("path", path);
        return mv;
    }


    @RequestMapping("/save")
    public void saveFile(HttpServletRequest request, HttpServletResponse response){
        FileSaver fs = new FileSaver(request, response);
        String str2 = map.get("path").substring(1,map.get("path").length());
        fs.saveToFile(uploadPath+str2);
        fs.close();
    }

    /**
     * 添加PageOffice的服务器端授权程序Servlet（必须）
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();
        //设置PageOffice注册成功后,license.lic文件存放的目录
        poserver.setSysPath("d:\\lic\\");
        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
        srb.addUrlMappings("/poserver.zz");
        srb.addUrlMappings("/posetup.exe");
        srb.addUrlMappings("/pageoffice.js");
        srb.addUrlMappings("/jquery.min.js");
        srb.addUrlMappings("/pobstyle.css");
        srb.addUrlMappings("/sealsetup.exe");
        return srb;//
    }

    /**
     * 添加印章管理程序Servlet（可选）
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean2() {
        com.zhuozhengsoft.pageoffice.poserver.AdminSeal adminSeal = new com.zhuozhengsoft.pageoffice.poserver.AdminSeal();
        adminSeal.setAdminPassword("111111");//设置印章管理员admin的登录密码
        adminSeal.setSysPath("d:\\lic\\");//印章数据库文件poseal.db的存放目录
        ServletRegistrationBean srb = new ServletRegistrationBean(adminSeal);
        srb.addUrlMappings("/adminseal.zz");
        srb.addUrlMappings("/sealimage.zz");
        srb.addUrlMappings("/loginseal.zz");
        return srb;
    }
}
