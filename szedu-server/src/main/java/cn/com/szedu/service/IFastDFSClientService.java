package cn.com.szedu.service;

import com.github.tobato.fastdfs.domain.StorePath;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IFastDFSClientService {
    StorePath upLoadFile(MultipartFile file)throws Exception;
    void downLoadFile(String group, String path, String fileName, HttpServletResponse response)throws Exception;
    byte[] downLoadFile(String group, String path)throws Exception;
    void deleteFlie(String group, String path);
}
