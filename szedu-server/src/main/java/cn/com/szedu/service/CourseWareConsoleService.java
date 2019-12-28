package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.dao.mapper.ICourseWareMapper;
import cn.com.szedu.entity.*;
import cn.com.szedu.entity.IntermediateTable.CoursewareUserRelation;
import cn.com.szedu.entity.IntermediateTable.PurchasedResources;
import cn.com.szedu.entity.IntermediateTable.ResourceClassRelation;
import cn.com.szedu.exception.CourseWareException;
import cn.com.szedu.model.*;
import cn.com.szedu.model.teacher.PushResourceModel;
import cn.com.szedu.repository.ICourseWareRespository;
import cn.com.szedu.repository.IResourceRuleRepository;
import cn.com.szedu.repository.IUploadLabelRepository;
import cn.com.szedu.repository.IUserInfoRepository;
import cn.com.szedu.repository.IntermediateTableRepository.ICoursewareUserRelationRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IPurchasedResourcesRepository;
import cn.com.szedu.repository.IntermediateTableRepository.IResourceClassRelationRepository;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CourseWareConsoleService {
    @Resource
    private ICourseWareRespository courseWareRespository;
    @Resource
    private IFastDFSClientService fastDFSClientService;
    @Resource
    private IUserInfoRepository userInfoRepository;
    @Resource
    private ICourseWareMapper courseWareMapper;
    @Resource
    private ICoursewareUserRelationRepository coursewareUserRelationRepository;
    @Resource
    private IUploadLabelRepository uploadLabelRepository;
    @Resource
    private IPurchasedResourcesRepository purchasedResourcesRepository;
    @Resource
    private IResourceClassRelationRepository resourceClassRelationRepository;
    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;

    private Logger logger=LoggerFactory.getLogger(getClass());

    @Resource
    private IResourceRuleRepository resourceRuleRepository;

    @Transactional(rollbackFor = Exception.class)
    public void saveCourseWare(CourseWare courseWare){
        courseWare.setIntegral(5);
        courseWare.setIsCheck("1");
        courseWareRespository.save(courseWare);
    }

    /**
     * fastdfs文件上传
     * @param userInfoForToken
     * @param file
     * @return
     * @throws CourseWareException
     */
    public StorePath upLoadFile(UserInfoForToken userInfoForToken, MultipartFile file) throws CourseWareException {
        String fileName=file.getOriginalFilename();
        /*if(!fileName.endsWith(".ppt")){
            logger.error("文件不是.ppt类型");
        }*/
        try {
            return fastDFSClientService.upLoadFile(file);
        }catch (Exception e){
            throw new CourseWareException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveCourseWare(UserInfoForToken userInfoForToken, MultipartFile file) throws CourseWareException{
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".ppt")){
            logger.error("文件不是.ppt类型");
        }
        StorePath path=null;
        try {
            path=fastDFSClientService.upLoadFile(file);
        }catch (Exception e){
            throw new CourseWareException(e.getMessage());
        }
        CourseWare courseWare=new CourseWare();
        courseWare.setFilegroup(path.getGroup());
        courseWare.setFilepath(path.getPath());
        courseWare.setFilename(file.getOriginalFilename());
        courseWare.setTeacherid(userInfoForToken.getUserId());
        courseWare.setIntegral(5);
        CourseWare courseWare1=courseWareRespository.save(courseWare);
        CoursewareUserRelation relation=new CoursewareUserRelation(courseWare1.getId(),courseWare1.getTeacherid());
        coursewareUserRelationRepository.save(relation);
    }

    /**
     * fast下载附件
     * @param id
     * @param response
     * @throws CourseWareException
     */
    public void downCourseWare(String id,HttpServletResponse response)throws CourseWareException{
        CourseWare courseWare=courseWareRespository.findByid(id);
        if (courseWare==null){
            throw  new CourseWareException(ResultCode.FILE_ISNOT_EXIST);
        }
        //拼接后缀名
        String suffix=courseWare.getFilepath().substring(courseWare.getFilepath().lastIndexOf(" "));
        courseWare.setFilename(courseWare.getFilename().concat(suffix));
        try {
            fastDFSClientService.downLoadFile(courseWare.getFilegroup(),courseWare.getFilepath(),courseWare.getFilename(),response);
        }catch (Exception e){
            throw new CourseWareException(e.getMessage());
        }

    }

    //删除课件
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourseWare(CourseWare courseWare){
        courseWareRespository.delete(courseWare);
    }

    /**
     * fast删除课件
     * @param id
     * @throws CourseWareException
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourseWare(String id) throws CourseWareException{
        CourseWare courseWare=courseWareRespository.findByid(id);
        if (courseWare==null){
            throw  new CourseWareException(ResultCode.FILE_ISNOT_EXIST);
        }
        fastDFSClientService.deleteFlie(courseWare.getFilegroup(),courseWare.getFilepath());
        courseWareRespository.deleteByid(id);
    }

    public List<CourseWare> getCouserWareByTeacherId(String teacherId){
        return courseWareRespository.findByTeacherid(teacherId);
    }

    /**
     * 根据老师查询资源
     * @param userInfoForToken
     * @param type
     * @param label
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<CourseWare> getCouserWareByTeacherIdAndPage(UserInfoForToken userInfoForToken,String type,String label,int pageNum,int pageSize){
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<CourseWare> page=courseWareRespository.findAll((Root<CourseWare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("teacherid"),userInfoForToken.getUserId()));
            if(type!=null && type!=""){
                predicateList.add(criteriaBuilder.equal(root.get("type"),type));
            }
            if(label!=null && label!=""){
                predicateList.add(criteriaBuilder.equal(root.get("label"),label));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return page;
    }

    //分页查询全部课件
    public Page<CourseWare> getCouserWareByFileNameAndPage(String fileName,int pageNum,int pageSize){
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<CourseWare> page=courseWareRespository.findAll((Root<CourseWare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if(fileName!=null && fileName!=""){
                predicateList.add(criteriaBuilder.equal(root.get("filename"),fileName));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return page;
    }

    //资源审核列表
    public PageInfo<CourseModel> getCouserWareByPage(int pageNum,int pageSize){
        List<CourseModel> courseModelList=courseWareMapper.getCheckResourceList();
        PageInfo<CourseModel> page=new PageInfo<>();
        page.setTotal(courseModelList.size());
        page.setList(courseModelList);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return page;
    }

    //统计
    public PageInfo<UploadResouseModel> teacherResourseStatistics(int pageNum,int pageSize){
        List<UserInfo> userInfos=userInfoRepository.findByRole("Teacher");
        List<UploadResouseModel> modelList=new ArrayList<>();
        for (UserInfo user:userInfos) {
            UploadResouseModel model=new UploadResouseModel();
            model.setTeacherId(user.getId());
            model.setCountUpload(courseWareRespository.countByTeacherid(user.getId()));
            model.setIntegral(user.getIntegral().toString());
            model.setSubject(user.getSubject());
            model.setUserName(user.getName());
            modelList.add(model);
        }
        PageInfo<UploadResouseModel> page=new PageInfo<>();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        page.setList(modelList);
        page.setTotal(modelList.size());
        return page;
    }

    /**
     * 上传资源审核
     * @param resourceId
     * @param status
     * @throws CourseWareException
     */
    public void updateIsCheck(String resourceId,String status)throws CourseWareException{
        if (StringUtils.isEmpty(resourceId) || StringUtils.isEmpty(status)){
            throw new CourseWareException(ResultCode.PARAM_MISS_MSG);
        }
        CourseWare courseWare=courseWareRespository.findByid(resourceId);
        if (courseWare==null){
            throw  new CourseWareException(ResultCode.FILE_ISNOT_EXIST);
        }
        /*if(status.equalsIgnoreCase("3")){//删除fast资源驳回
            if(!(StringUtils.isEmpty(courseWare.getFilegroup())|| StringUtils.isEmpty(courseWare.getFilepath()))){
                fastDFSClientService.deleteFlie(courseWare.getFilegroup(),courseWare.getFilepath());
            }
        }
        if(status.equalsIgnoreCase("2")){//删除fast资源
            userInfoRepository.updateIntegral(integral,courseWare.getTeacherid());
        }*/
        courseWareRespository.updateIsCheck(resourceId,status);
    }
    /**
     * 分享资源审核
     * @param resourceId
     * @param status
     * @throws CourseWareException
     */
    public void updateIsShareCheck(String resourceId,String status)throws CourseWareException{
        if (StringUtils.isEmpty(resourceId) || StringUtils.isEmpty(status)){
            throw new CourseWareException(ResultCode.PARAM_MISS_MSG);
        }
        CourseWare courseWare=courseWareRespository.findByid(resourceId);
        if (courseWare==null){
            throw  new CourseWareException(ResultCode.FILE_ISNOT_EXIST);
        }
        /*if(status.equalsIgnoreCase("3")){//删除fast资源驳回
            if(!(StringUtils.isEmpty(courseWare.getFilegroup())|| StringUtils.isEmpty(courseWare.getFilepath()))){
                fastDFSClientService.deleteFlie(courseWare.getFilegroup(),courseWare.getFilepath());
            }
        }
        if(status.equalsIgnoreCase("2")){//删除fast资源
            userInfoRepository.updateIntegral(integral,courseWare.getTeacherid());
        }*/
        courseWareRespository.updateIsShareCheck(resourceId,status);
    }

    //积分修改
    public void updateIntegral(String resourceId,Integer integral)throws CourseWareException{
        if (StringUtils.isEmpty(resourceId) || StringUtils.isEmpty(integral)){
            throw new CourseWareException(ResultCode.PARAM_MISS_MSG);
        }
        courseWareRespository.updateIntegral(resourceId,integral);
    }

    //资源列表
    public PageInfo<CourseModel> getCouserWareByPage2(String name,int pageNum,int pageSize){
        List<CourseModel> courseModelList=null;if(!(StringUtils.isEmpty(name))){
            courseModelList=courseWareMapper.getResourceListByName("%"+name+"%");
        }else {
            courseModelList=courseWareMapper.getResourceList();
        }
        PageInfo<CourseModel> page=new PageInfo<>();
        page.setTotal(courseModelList.size());
        page.setList(courseModelList);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return page;
    }

    //查找分享课件
    public Page<CourseWare> findCourseWare(List<CourseWareShare> courseWareShares, int pageNum, int pageSize){
        List<String> courseWareIds=new ArrayList<>();
        for (CourseWareShare courseWare:courseWareShares) {
            courseWareIds.forEach(courseWareShare ->
                    courseWareIds.add(courseWare.getCoursewareid())
            );
        }
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<CourseWare> page=courseWareRespository.findAll((Root<CourseWare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if(courseWareIds!=null && courseWareIds.size()!=0){
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("id"));
                for (String id : courseWareIds) {
                    in.value(id);
                }
                predicateList.add(in);
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return page;
    }

    public CourseWare findCourseWareById(String id){
        return courseWareRespository.findByid(id);
    }

    public List<CourseWare> findCourseWareByFileName(){
        return courseWareRespository.findByTeacherid("common");
    }

    /**
     * 上传资源标签
     * @param userInfo
     * @return
     */
    public List<UploadLabel> getUploadLabel(UserInfoForToken userInfo){
        return uploadLabelRepository.findAll();
    }

    /**
     * 查询资源规则
     * @param userInfo
     * @return
     */
    public List<ResourcesRule> findResourcesRule(UserInfoForToken userInfo){
        return resourceRuleRepository.findAll();
    }

    /**
     * 根据id返回规则
     * @param userInfo
     * @param ruleId
     * @return
     */
    public ResourcesRule getResourceRuleByid(UserInfoForToken userInfo,Integer ruleId){
        return resourceRuleRepository.findFirstByid(ruleId);
    }

    /**
     * 根据规则id修改规则
     * @param userInfo
     * @param rule
     * @return
     */
    public ResourcesRule updateResourcesRule(UserInfoForToken userInfo,ResourcesRule rule){
        ResourcesRule rule1=resourceRuleRepository.findFirstByid(rule.getId());
        rule1.setRule(rule.getRule());
        return resourceRuleRepository.save(rule);
    }

    /**
     * 本地上传
     * @param userInfoForToken
     * @param file
     * @return
     * @throws IOException
     */
    public String localUpload(UserInfoForToken userInfoForToken, MultipartFile file,Integer resourceType,String resourceId) throws IOException,CourseWareException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //查询资源类型
        ResourcesRule rule=resourceRuleRepository.findFirstByid(resourceType);
        String[] type=rule.getResourceType().split("/");
        if (!Arrays.asList(type).contains(suffixName.substring(1))){
            throw new CourseWareException(rule.getTypeName()+"中不包含该类型！");
        }
        double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>Double.valueOf(rule.getRule())){
            throw new CourseWareException("资源过大！");
        }
        CourseWare courseWare=new CourseWare();
        if(!StringUtils.isEmpty(resourceId)){
            courseWare=courseWareRespository.findByid(resourceId);
        }
        courseWare.setFilename(fileName);
        courseWare.setTeacherid(userInfoForToken.getUserId());
        courseWare.setTeacherName(userInfoForToken.getUserName());
        courseWare.setIsCheck("1");//未审核
        //重新生成文件名
        fileName =UUID.randomUUID()+suffixName;
        //String logoPathDir = "/video/";
        //String logoRealPathDir = request.getSession().getServletContext().getRealPath(logoPathDir);
        //String dirPath = System.getProperty("user.dir");
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        courseWare.setFilepath(uri+fileName);
        courseWare.setType(resourceType.toString());
        CourseWare courseWare1=courseWareRespository.save(courseWare);
        return courseWare1.getId();
    }

    /**
     * 本地上传资源封面
     * @param userInfoForToken
     * @param file
     * @return
     * @throws IOException
     */
    public String localUploadCover(UserInfoForToken userInfoForToken, MultipartFile file,String resourceId) throws IOException,CourseWareException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        double fileSize = (double) file.getSize()/1024/1024;//MB
        if(fileSize>2){
            throw new CourseWareException("图片过大！");
        }
        CourseWare courseWare=new CourseWare();
        if(!StringUtils.isEmpty(resourceId)){
            courseWare=courseWareRespository.findByid(resourceId);
        }
        courseWare.setTeacherid(userInfoForToken.getUserId());
        courseWare.setTeacherName(userInfoForToken.getUserName());
        courseWare.setIsCheck("1");//未审核
        //重新生成文件名
        fileName =UUID.randomUUID()+suffixName;
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        courseWare.setCoverPic(uri+fileName);
        CourseWare courseWare1=courseWareRespository.save(courseWare);
        return courseWare1.getId();
    }

    /**
     * 添加资源
     * @param userInfo
     * @param courseWare
     */
    public void addCourseWareByLoacal(UserInfoForToken userInfo,CourseWare courseWare){
        CourseWare courseWare1=courseWareRespository.findByid(courseWare.getId());
        courseWare1.setLabel(courseWare.getLabel());
        courseWare1.setDescribes(courseWare.getDescribes());
        courseWare1.setSubject(courseWare.getSubject());
        courseWareRespository.save(courseWare1);
    }

    /**
     * 本地下载
     * @param id
     * @param response
     * @return
     */
    public void localDownLoad(String id,HttpServletResponse response)throws CourseWareException,Exception {
        CourseWare courseWare=courseWareRespository.findByid(id);
        String path=courseWare.getFilepath().substring(1);
        File file=new File(uploadPath+path);
        if(file.exists()){
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(courseWare.getFilename(),"UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis=null;//文件输入流
            BufferedInputStream bis=null;
            OutputStream os=null;//输出流
            try{
                os =response.getOutputStream();
                fis=new FileInputStream(file);
                bis=new BufferedInputStream(fis);
                int i=bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            throw new CourseWareException(ResultCode.FILE_ISNOT_EXIST);
        }
    }

    /**
     * 根据资源id删除课件（本地）
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourseWareById(String id) throws CourseWareException{
        CourseWare courseWare=courseWareRespository.findByid(id);
        if (courseWare==null){
            throw new CourseWareException(ResultCode.RESOURCE_ISNOT_EXIST);
        }
        File file=new File(courseWare.getFilepath());
        //删除课件
        courseWareRespository.deleteByid(id);
        if (!StringUtils.isEmpty(courseWare.getCoverPic())){
            File file2=new File(courseWare.getCoverPic());
            if(file2.exists()){
                //删除文件
                file2.delete();
            }
        }
        if(file.exists()){
            //删除文件
            file.delete();
        }else {
            throw new CourseWareException(ResultCode.FILE_ISNOT_EXIST);
        }
    }

    /**
     * 复制文件(购买资源)
     * @param userInfo
     * @param resourceId
     */
    @Transactional(rollbackFor = Exception.class)
    public void copyResource(UserInfoForToken userInfo,String resourceId) throws IOException{
        CourseWare courseWare=courseWareRespository.findByid(resourceId);
        CourseWare courseWare1=new CourseWare();
        courseWare1.setIsCheck(courseWare.getIsCheck());
        courseWare1.setIntegral(courseWare.getIntegral());
        courseWare1.setLabel(courseWare.getLabel());
        courseWare1.setDescribes(courseWare.getDescribes());
        courseWare1.setFilename(courseWare.getFilename());
        courseWare1.setType(courseWare.getType());
        courseWare1.setSubject(courseWare.getSubject());
        String path=courseWare.getFilepath().substring(1);
        //获取资源文件后缀名
        String suffixName = courseWare.getFilename().substring(courseWare.getFilename().lastIndexOf("."));
        String newName=UUID.randomUUID()+suffixName;
        //封面
        String suffixName2 = courseWare.getFilename().substring(courseWare.getCoverPic().lastIndexOf("."));
        String newName2=UUID.randomUUID()+suffixName;
        String path2=courseWare.getCoverPic().substring(1);
        FileChannel input=null;
        FileChannel output=null;
        try{
            input=new FileInputStream(new File(uploadPath+path)).getChannel();
            output=new FileOutputStream(new File(uploadPath+imagePath+newName)).getChannel();
            output.transferFrom(input,0,input.size());
            input=new FileInputStream(new File(uploadPath+path2)).getChannel();
            output=new FileOutputStream(new File(uploadPath+imagePath+newName2)).getChannel();
            output.transferFrom(input,0,input.size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            input.close();
            output.close();
            courseWare1.setFilepath(uri+newName);
            courseWare1.setTeacherid(userInfo.getUserId());
            courseWare1.setTeacherName(userInfo.getUserName());
            courseWare1.setCoverPic(uri+newName2);
            courseWare1.setWaysToget("1");
            CourseWare courseWare2=courseWareRespository.save(courseWare1);
            purchasedResourcesRepository.save(new PurchasedResources(courseWare2.getId(),courseWare2.getFilename(),courseWare2.getIntegral(),userInfo.getUserId()));
        }
    }

    /**
     * 推送资源
     * @param userInfo
     * @param model
     */
    @Transactional(rollbackFor = Exception.class)
    public void pushResouceToClass(UserInfoForToken userInfo, PushResourceModel model){
        List<ResourceClassRelation> resourceClassRelations=new ArrayList<>();
        model.getClassId().forEach(c->{
            ResourceClassRelation relation=new ResourceClassRelation(model.getResourceId(),c,userInfo.getUserId());
            resourceClassRelations.add(relation);
        });
        resourceClassRelationRepository.saveAll(resourceClassRelations);
    }
}
