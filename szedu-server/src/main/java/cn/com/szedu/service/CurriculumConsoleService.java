package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.Curriculum;
import cn.com.szedu.entity.CurriculumResource;
import cn.com.szedu.entity.IntermediateTable.CurriculumKnowledges;
import cn.com.szedu.exception.CourseWareException;
import cn.com.szedu.model.CurriculumModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.ICurriculumKnowledgesRepository;
import cn.com.szedu.repository.ICurriculumRepository;
import cn.com.szedu.repository.ICurriculumResourceRepository;
import cn.com.szedu.util.MappingEntity2ModelCoverter;
import com.github.tobato.fastdfs.domain.StorePath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class CurriculumConsoleService {
    @Resource
    private ICurriculumRepository curriculumRepository;
    @Resource
    private ICurriculumResourceRepository curriculumResourceRepository;
    @Resource
    private IFastDFSClientService fastDFSClientService;
    @Resource
    private ICurriculumKnowledgesRepository curriculumKnowledgesRepository;

    //添加
    public void  saveCurriculumAndCurriculumResource(CurriculumModel model){
        Curriculum curriculum1=curriculumRepository.findByid(model.getId());
        if (curriculum1!=null){
            model.setCurriculumPic(curriculum1.getCurriculumPic());
        }
        Curriculum curriculum=curriculumRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMCURRICULUMMODELCURRICULUM(model));
        if (model.getCurriculumKnowledges()!=null && model.getCurriculumKnowledges().size()!=0){
            for (CurriculumKnowledges k:model.getCurriculumKnowledges()) {
                k.setCurriculumId(curriculum.getId());
            }
            curriculumKnowledgesRepository.saveAll(model.getCurriculumKnowledges());//保存知识点
        }
        //保存资源
        model.setId(curriculum.getId());
        curriculumResourceRepository.save(MappingEntity2ModelCoverter.CONVERTERFROMCURRICULUMMODELCURRICULUMRESOUTCE(model));
    }

    //添加资源
    public void  addCurriculumResource(CurriculumResource curriculumResource){
        curriculumResourceRepository.save(curriculumResource);
    }

    //资源上传
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
        curriculumKnowledgesRepository.deleteByCurriculumId(id);
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

    public List<Curriculum> getCurriculum(){
        return curriculumRepository.findAll();
    }
}
