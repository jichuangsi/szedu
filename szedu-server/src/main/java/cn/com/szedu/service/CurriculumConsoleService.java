package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.entity.Curriculum;
import cn.com.szedu.entity.CurriculumResource;
import cn.com.szedu.entity.IntermediateTable.CurriculumKnowledges;
import cn.com.szedu.exception.CourseWareException;
import cn.com.szedu.model.CurriculemResourceModel;
import cn.com.szedu.model.CurriculumModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.ICurriculumKnowledgesRepository;
import cn.com.szedu.repository.ICurriculumRepository;
import cn.com.szedu.repository.ICurriculumResourceRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.github.tobato.fastdfs.domain.StorePath;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CurriculumConsoleService {
    @Resource
    private ICurriculumRepository curriculumRepository;
    @Resource
    private ICurriculumResourceRepository curriculumResourceRepository;
    @Resource
    private IFastDFSClientService fastDFSClientService;

    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;

    /**
     * 添加课程
     * @param model
     */
    public void  saveCurriculumAndCurriculumResource(CurriculumModel model){
        Curriculum curriculum1=curriculumRepository.findByid(model.getId());
        if (curriculum1!=null){
            model.setCurriculumPic(curriculum1.getCurriculumPic());
        }
        Curriculum curriculum=curriculumRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMCURRICULUMMODELCURRICULUM(model));
        /*if (model.getCurriculumKnowledges()!=null && model.getCurriculumKnowledges().size()!=0){
            for (CurriculumKnowledges k:model.getCurriculumKnowledges()) {
                k.setCurriculumId(curriculum.getId());
            }
            curriculumKnowledgesRepository.saveAll(model.getCurriculumKnowledges());//保存知识点
        }*/
        //保存资源
       /* model.setId(curriculum.getId());
        curriculumResourceRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMCURRICULUMMODELCURRICULUMRESOUTCE(model));*/
    }

    /**
     * fastdfs资源上传
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


    /**
     * 添加资源信息
     * @param userInfoForToken
     * @param curriculumResource
     */
    @Transactional(rollbackFor = Exception.class)
    public void addCurriculumResourceInfo(UserInfoForToken userInfoForToken,CurriculumResource curriculumResource){
        CurriculumResource resource=curriculumResourceRepository.findByid(curriculumResource.getId());
        resource.setChapterId(curriculumResource.getChapterId());
        resource.setSubjectId(curriculumResource.getSubjectId());
        resource.setCurriculumId(curriculumResource.getCurriculumId());
        curriculumResourceRepository.save(resource);
    }

    /**
     * 上传资源
     * @param userInfoForToken
     * @param file
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addCurriculumResource(UserInfoForToken userInfoForToken, MultipartFile file) throws IOException{
        CurriculumResource resource=localUpLoadFile(userInfoForToken,file);
        return resource.getId();
    }

    /**
     * 本地上传文件
     * @param userInfoForToken
     * @param file
     * @throws CourseWareException
     */
    @Transactional(rollbackFor = Exception.class)
    public CurriculumResource localUpLoadFile(UserInfoForToken userInfoForToken, MultipartFile file) throws IOException {
        //获取文件名
        String fileName = file.getOriginalFilename();
        CurriculumResource resource=new CurriculumResource();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        resource.setFilename(fileName);
        resource.setTeacherid(userInfoForToken.getUserId());
        //重新生成文件名
        fileName =UUID.randomUUID()+suffixName;
        File file1=new File(uploadPath+imagePath+fileName);
        if (!file1.exists()){
            //创建文件夹
            file1.getParentFile().mkdir();
        }
        file.transferTo(file1);
        resource.setFilepath(uri+fileName);
        CurriculumResource curriculumResource=curriculumResourceRepository.save(resource);
        return  curriculumResource;
    }

    /**
     * 多个资源保存
     * @param userInfoForToken
     * @param model
     */
    @Transactional(rollbackFor = Exception.class)
    public void addCurriculumResourceInfos(UserInfoForToken userInfoForToken,CurriculemResourceModel model){
        List<CurriculumResource> resources=curriculumResourceRepository.findByidIn(model.getResourceIds());
        for (CurriculumResource r : resources) {
            r.setSubjectId(model.getSubjectId());
            r.setChapterId(model.getChapterId());
            r.setCurriculumId(model.getCurriculumId());
        }
        curriculumResourceRepository.saveAll(resources);
    }

    /**
     * 多上传资源
     * @param userInfoForToken
     * @param files
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> addCurriculumResources(UserInfoForToken userInfoForToken, MultipartFile[] files) throws IOException{
        List<CurriculumResource> resource=localUpLoadFiles(userInfoForToken,files);
        List<Integer> resourceIds=new ArrayList<>();
        resource.forEach(curriculumResource -> {
            resourceIds.add(curriculumResource.getId());
        });
        return resourceIds;
    }

    /**
     * 本地多文件上传
     * @param userInfoForToken
     * @param files
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public List<CurriculumResource> localUpLoadFiles(UserInfoForToken userInfoForToken, MultipartFile[] files) throws IOException {
        List<CurriculumResource> curriculumResources=new ArrayList<>();
        for (MultipartFile file:files) {
            //获取文件名
            String fileName = file.getOriginalFilename();
            CurriculumResource resource=new CurriculumResource();
            resource.setFilename(fileName);
            //获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            resource.setTeacherid(userInfoForToken.getUserId());
            //重新生成文件名
            fileName =UUID.randomUUID()+suffixName;
            File file1=new File(uploadPath+imagePath+fileName);
            if (!file1.exists()){
                //创建文件夹
                file1.getParentFile().mkdir();
            }
            file.transferTo(file1);

            resource.setFilepath(uri+fileName);
            CurriculumResource curriculumResource=curriculumResourceRepository.save(resource);
            curriculumResources.add(curriculumResource);
        }
        return  curriculumResources;
    }

    //删除课程
    @Transactional(rollbackFor = Exception.class)
    public void deleteCurriculum(Integer id) throws CourseWareException{
        if(StringUtils.isEmpty(id)){
            throw new CourseWareException(ResultCode.PARAM_MISS_MSG);
        }
        List<CurriculumResource> curriculumResources=curriculumResourceRepository.findByCurriculumId(id);
        //fast删除
        for (CurriculumResource r:curriculumResources) {
            fastDFSClientService.deleteFlie(r.getFilegroup(),r.getFilepath());
        }
        //删除资源表
        curriculumResourceRepository.deleteByCurriculumId(id);
        //知识点
        //curriculumKnowledgesRepository.deleteByCurriculumId(id);
        //课程
        curriculumRepository.deleteById(id);
    }

    //上传课程图片
    public Integer addCurriculumImg(MultipartFile file)throws IOException {
        Curriculum curriculum=new Curriculum();
        curriculum.setCurriculumPic(file.getBytes());
        Curriculum curriculum1=curriculumRepository.save(curriculum);
        return curriculum1.getId();
    }

    //获得资源
    public List<CurriculumResource> getCurriculumResource(){
        return curriculumResourceRepository.findAll();
    }

    /**
     * 根据章节获取资源
     * @param curriculumId
     * @param chapterId
     * @return
     */
    public List<CurriculumResource> getResourceByChapter(Integer curriculumId,Integer chapterId){
        return curriculumResourceRepository.findByChapterIdAndCurriculumId(chapterId,curriculumId);
    }

    public List<Curriculum> getCurriculum(){
        return curriculumRepository.findAll();
    }
}
