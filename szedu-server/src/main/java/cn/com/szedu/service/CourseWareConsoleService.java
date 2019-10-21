package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.CourseWare;
import cn.com.szedu.entity.CourseWareShare;
import cn.com.szedu.exception.CourseWareException;
import cn.com.szedu.model.GradeModel;
import cn.com.szedu.model.ShareElements;
import cn.com.szedu.model.SubjectModel;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.ICourseWareRespository;
import com.github.tobato.fastdfs.domain.StorePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseWareConsoleService {
    @Resource
    private ICourseWareRespository courseWareRespository;
   /* @Resource
    private ISchoolService schoolService;
    @Resource
    private ISchoolInfoRepository schoolInfoRepository ;
    @Resource
    private IPhraseInfoRepository phraseInfoRepository;
    @Resource
    private IGradeInfoRepository gradeInfoRepository;*/
    @Resource
    private IFastDFSClientService fastDFSClientService;

    private Logger logger=LoggerFactory.getLogger(getClass());

    @Transactional(rollbackFor = Exception.class)
    public void saveCourseWare(CourseWare courseWare){
        courseWareRespository.save(courseWare);
    }

    //课件上传
    public StorePath upLoadFile(UserInfoForToken userInfoForToken, MultipartFile file) throws CourseWareException {
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".ppt")){
            logger.error("文件不是.ppt类型");
        }
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
        courseWareRespository.save(courseWare);
    }

    //下载附件
    public void downCourseWare(String id,HttpServletResponse response)throws CourseWareException{
        CourseWare courseWare=courseWareRespository.findByid(id);
        if (courseWare==null){
            throw  new CourseWareException(ResultCode.FILE_ISNOT_EXIST);
        }
        //拼接后缀名
        String suffix=courseWare.getFilepath().substring(courseWare.getFilepath().lastIndexOf("."));
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

    //删除课件
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
    //分页查询
    public Page<CourseWare> getCouserWareByTeacherIdAndPage(String teacherId,int pageNum,int pageSize){
        pageNum=pageNum-1;
        Pageable pageable=new PageRequest(pageNum,pageSize);
        Page<CourseWare> page=courseWareRespository.findAll((Root<CourseWare> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicateList = new ArrayList<>();
            if(teacherId!=null && teacherId!=""){
                predicateList.add(criteriaBuilder.equal(root.get("teacherid"),teacherId));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        },pageable);
        return page;
    }

    //获得分享参数
    /*public ShareElements getShareElements(UserInfoForToken userInfoForToken, String schoolId, String teacherId){
        ShareElements shareElements=new ShareElements();
        shareElements.setSubjects(schoolService.getSubjects(userInfoForToken));
        SchoolInfo info = schoolInfoRepository.findFirstById(schoolId);
        List<String> gradeIds = new ArrayList<String>();
        List<PhraseInfo> phraseInfos = phraseInfoRepository.findByDeleteFlagAndIdIn("0", info.getPhraseIds());
        for (PhraseInfo phraseInfo : phraseInfos) {
            gradeIds.addAll(phraseInfo.getGradeIds());
        }
        List<GradeInfo> gradeInfoList=gradeInfoRepository.findByDeleteFlagAndIdInOrderByCreateTime("0",gradeIds);
        List<GradeModel> gradeModels = new ArrayList<GradeModel>();
        gradeInfoList.forEach(gradeInfo -> {
            GradeModel model = MappingEntity2ModelConverter.CONVERTERFROMGRADEINFO(gradeInfo);
            gradeModels.add(model);
        });
        shareElements.setGradeModels(gradeModels);
        shareElements.setCourseWares(courseWareRespository.findByTeacherid(teacherId));
        return shareElements;
    }*/

    public ShareElements getShareElements(UserInfoForToken userInfoForToken){
        SubjectModel subjectModel=new SubjectModel();
        //科目
        subjectModel.setId("1");
        subjectModel.setSubjectName("数学");
        List<SubjectModel> subjectModelList=new ArrayList<>();
        subjectModelList.add(subjectModel);
        ShareElements shareElements=new ShareElements();
        shareElements.setSubjects(subjectModelList);
        List<CourseWare> courseWareList=new ArrayList<>();
        CourseWare courseWare=new CourseWare();
        //课程
        courseWareList.add(courseWare);
        shareElements.setCourseWares(courseWareList);
        GradeModel gradeModel=new GradeModel();
        //年级
        List<GradeModel> gradeModelList = new ArrayList<>();
        gradeModelList.add(gradeModel);
        shareElements.setGradeModels(gradeModelList);
        return shareElements;
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
}
