package cn.com.szedu.service;

import cn.com.szedu.constant.ResultCode;
import cn.com.szedu.entity.ShowPicture;
import cn.com.szedu.exception.BackUserException;
import cn.com.szedu.model.UserInfoForToken;
import cn.com.szedu.repository.IShowPictureRepository;
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
public class ShowPictureService {
    @Resource
    private IShowPictureRepository showPictureRepository;

    @Value("${file.uploadFolder}")
    private String uploadPath;
    @Value("${file.imagePath}")
    private String imagePath;
    @Value("${file.uri}")
    private String uri;


    /**
     * 本地上传多文件
     * @param userInfoForToken
     * @param files
     * @param way
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public void localUpLoadFiles(UserInfoForToken userInfoForToken, MultipartFile[] files,String way) throws IOException {
        List<ShowPicture> showPictures=new ArrayList<>();
        for (MultipartFile file:files) {
            //获取文件名
            String fileName = file.getOriginalFilename();
            ShowPicture showPicture=new ShowPicture();
            //获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            showPicture.setFilename(fileName);
            showPicture.setCreatorId(userInfoForToken.getUserId());
            showPicture.setCreatorName(userInfoForToken.getUserName());
            //重新生成文件名
            fileName =UUID.randomUUID()+suffixName;
            File file1=new File(uploadPath+imagePath+fileName);
            if (!file1.exists()){
                //创建文件夹
                file1.getParentFile().mkdir();
            }
            file.transferTo(file1);
            showPicture.setFilepath(uri+fileName);
            showPicture.setWay(way);
            showPicture.setStatus("2");
            showPictures.add(showPicture);
        }
        showPictureRepository.saveAll(showPictures);
    }

    /**
     * 查询全部轮播图
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ShowPicture> getAllShowPictureByWay(String way)throws BackUserException{
        if (showPictureRepository.countByStatusAndWay("1",way)>0 && showPictureRepository.countByStatusAndWay("2",way)>0){
            List<ShowPicture> showPictureList=showPictureRepository.findByWayAndStatus(way,"1");
            if (showPictureList!=null&& showPictureList.size()!=0){
                for (ShowPicture s:showPictureList) {
                    File file=new File(s.getFilepath());
                    if(file.exists()){
                        //删除文件
                        file.delete();
                    }else {
                        throw new BackUserException(ResultCode.FILE_ISNOT_EXIST);
                    }
                }
                showPictureRepository.deleteAll(showPictureList);
            }
        }
        List<ShowPicture> showPictures=showPictureRepository.findByWayAndStatus(way,"2");
        if(showPictures!=null&&showPictures.size()>0){
            showPictures.forEach(sp->{
                sp.setStatus("1");
            });
            showPictureRepository.saveAll(showPictures);
        }
        return showPictureRepository.findByWayAndStatus(way,"1");
    }
    /**
     * 删除轮播图
     * @param id
     * @throws BackUserException
     */
    public void deleteShowPicture(Integer id)throws BackUserException {
        ShowPicture showPicture=showPictureRepository.findByid(id);
        if (StringUtils.isEmpty(id)){
            throw  new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        if(showPicture==null){
            throw  new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        showPictureRepository.delete(showPicture);
    }
}
