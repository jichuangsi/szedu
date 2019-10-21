package cn.com.szedu.service.impl;

import cn.com.szedu.service.IFastDFSClientService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.sun.deploy.net.URLEncoder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Service
public class FastDFSClientServiceImpl implements IFastDFSClientService {
    @Resource
    private FastFileStorageClient fastFileStorageClient;
    @Override
    public StorePath upLoadFile(MultipartFile file) throws Exception {
        StorePath path=fastFileStorageClient.uploadFile(file.getInputStream(),file.getSize(),FilenameUtils.getExtension(file.getOriginalFilename()),null);
        return path;
    }

    @Override
    public void downLoadFile(String group,String path,String filName,HttpServletResponse response)throws Exception {
        byte[] bytes =fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());
        //设置相应类型application/octet-stream        （注：applicatoin/octet-stream 为通用，一些其它的类型苹果浏览器下载内容可能为空）
        response.reset();
        response.setContentType("applicatoin/octet-stream");
        //设置头信息                 Content-Disposition为属性名  附件形式打开下载文件   指定名称  为 设定的fileName
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filName, "UTF-8"));
        // 写入到流
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

    @Override
    public byte[] downLoadFile(String group, String path) throws Exception {
        return fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());
    }

    @Override
    public void deleteFlie(String group,String path) {
        fastFileStorageClient.deleteFile(group, path);
    }
}
