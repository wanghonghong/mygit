package com.jm.repository.client;

import com.jm.repository.client.dto.CosResourceDto;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import com.qcloud.PicCloud;
import com.qcloud.UploadResult;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>万象有图</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/06
 */
@Slf4j
@Repository
public class ImageClient extends BaseClient {

    //万象优图
    public static final int APP_ID_V2 = 10046040;
    public static final String SECRET_ID_V2 = "AKIDOVfkAYOk6EcAHLr7iRg6KEh2mZ9motH7";
    public static final String SECRET_KEY_V2 = "pq1FCjhJGed3RTwUm4kSB1LRpyaOc0LK";
    public static final String BUCKET = "product";

    //COS
    public static final long APP_ID = 1253295418;
    public static final String SECRET_ID = "AKIDHQSK0pF3PdfWAgjDF5oPo7HFF93onnaH";
    public static final String SECRET_KEY = "gDYhfTQYWKzSjohOsAHCuQksoY8YmoGc";
    public static final String BUCKET_NAME = "product";

    private static PicCloud pc; //万象优图

    private static COSClient cosClient; //cos

    public static PicCloud getPc(){
        if (pc==null){
            pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET);
        }
        return pc;
    }

    public static COSClient getCosClient(){
        if (cosClient==null){
            Credentials cred = new Credentials(APP_ID, SECRET_ID, SECRET_KEY);
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.setRegion("sh");
            cosClient = new COSClient(clientConfig, cred);
        }
        return cosClient;
    }

    public static CosResourceDto uploadCosRes(String prefix,String suffix,byte[] bytes) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fileId = "/"+prefix+"/"+ sdf.format(new Date())+"/"+ Toolkit.getUUID()+"."+suffix;
        UploadFileRequest uploadFileRequest = new UploadFileRequest(BUCKET_NAME, fileId, bytes);
        String resJson  = getCosClient().uploadFile(uploadFileRequest);
        CosResourceDto cosResourceDto = JsonMapper.parse(resJson,CosResourceDto.class);
        cosResourceDto.setFileId(fileId);
        return cosResourceDto;
    }

    /**
     * 删除图片
     * @param fileId
     * @return
     */
    public static int delPic(String fileId){
        return getPc().delete(fileId);
    }

    /**
     * 上传图片
     * @param file
     * @return
     * @throws IOException
     */
    public static UploadResult uploadPic(MultipartFile file,String suffix) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fileId = sdf.format(new Date())+"/"+Toolkit.getUUID()+"."+suffix;
        return getPc().upload(file.getInputStream(),fileId);
    }

    /**
     * 上传图片流
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static UploadResult uploadInputStream(InputStream inputStream,String suffix) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fileId = sdf.format(new Date())+"/"+Toolkit.getUUID()+"."+suffix;
        return getPc().upload(inputStream,fileId);
    }

}
