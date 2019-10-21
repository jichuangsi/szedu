package cn.com.szedu.controller;

import com.github.tobato.fastdfs.domain.MataData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.sun.deploy.net.URLEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
@Api(description = "fastdfs接口API")
public class FastdfsController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation("文件上传")
    @PostMapping("/uppload")
    public StorePath test(@RequestParam MultipartFile file) throws IOException {

        // 设置文件信息
        Set<MataData> mataData = new HashSet<>();
        mataData.add(new MataData("author", "zonghui"));
        mataData.add(new MataData("description", "xxx文件，嘿嘿嘿"));

        // 上传   （文件上传可不填文件信息，填入null即可）
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), mataData);

        return storePath;
    }

    /**
     * 文件删除
     *
     * @param path
     * @return
     */
    @ApiOperation("文件删除")
    @DeleteMapping("/delete")
    public String delete(@RequestParam String path) {

        // 第一种删除：参数：完整地址
        fastFileStorageClient.deleteFile(path);

        // 第二种删除：参数：组名加文件路径
        // fastFileStorageClient.deleteFile(group,path);

        return "恭喜恭喜，删除成功！";
    }


    /**
     * 文件下载
     *
     * @param path
     * @return
     */
    @ApiOperation("文件下载")
    @GetMapping("/download")
    public void downLoad(@RequestParam String group, @RequestParam String path, @RequestParam String fileName, HttpServletResponse response) throws IOException {

        // 获取文件
        byte[] bytes = fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());

        //设置相应类型application/octet-stream        （注：applicatoin/octet-stream 为通用，一些其它的类型苹果浏览器下载内容可能为空）
        response.reset();
        response.setContentType("applicatoin/octet-stream");
        //设置头信息                 Content-Disposition为属性名  附件形式打开下载文件   指定名称  为 设定的fileName
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        // 写入到流
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

}
