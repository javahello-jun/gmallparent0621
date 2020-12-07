package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("admin/product/")
public class FileUploadController {
    //文件上传的时候：文件服务器有可能会更改Ip地址。不能将ip写死在代码中，应该放在配置文件中。。软编码
    @Value("${fileServer.url}")
    private String fileServerUrl;

    //springMVc文件上传对象multipartfile
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) throws IOException, MyException {
        /*
        * 1.读取rakcer.conf
        * 2.创建一个trakcerClient
        * 3.创建一个tradkerServer
        * 4.创建一个stroageclient
        * 5.上传
        * 6.将上传之后的文件放入result
        * */
        String configFile = this.getClass().getResource("/tracker.conf").getFile();
        String path = null;
        if(configFile!=null){
            //初始化
            ClientGlobal.init(configFile);
            //创建一个trakcerClient
            TrackerClient trackerClient = new TrackerClient();

            //创建一个tradkerServer
            TrackerServer tradkerServer = trackerClient.getConnection();

            //创建一个stroageclient
            StorageClient1 storageClient1 = new StorageClient1(tradkerServer,null);

            //上传 第一个参数表示上传文件的字节数组  第二个参数
            String originalFilename = file.getOriginalFilename();
            String extName = FilenameUtils.getExtension(originalFilename);
            //获取到上传后的url
             path = storageClient1.upload_appender_file1(file.getBytes(), extName, null);

        }
        return Result.ok(fileServerUrl+path);
    }

}
