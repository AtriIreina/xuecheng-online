package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioTest {

    @Test
    public void test_upload() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 创建 minio 客户端
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://192.168.101.65:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
        // 通过工具类获取mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".png");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; //通用mimeType 字节流
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        // 上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")
                .filename("C:\\Users\\loliy\\Pictures\\33.png") // 指定本地文件路径
                .object("/test/01/3.png") // 对象名 (可以包含目录)
                .contentType(mimeType) // 设置文件类型(minio会自动指定, 但最好自行设置)
                .build();
        // 上传文件
        minioClient.uploadObject(uploadObjectArgs);
    }

    @Test
    public void test_delete() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 创建 minio 客户端
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://192.168.101.65:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("video")
                .object("/9/b/") // 对象名
                .build();
        // 删除文件
        minioClient.removeObject(removeObjectArgs);
    }

    @Test
    public void test_getFile() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 创建 minio 客户端
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://192.168.101.65:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket("testbucket")
                .object("/test/01/3.png") // 对象名
                .build();
        // 获取文件
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
        // 指定输出流(下载到本地)
        FileOutputStream outputStream = new FileOutputStream(new File("C:\\Users\\loliy\\Pictures\\33_minio.png"));
        IOUtils.copy(inputStream, outputStream);
        // 校验文件完整性 (md5对比)
        // 不要获取远程流的 (网络请求会导致md5对比不通过)
        // String source_md5 = DigestUtils.md5Hex(inputStream);
//        String local_md5 = DigestUtils.md5Hex(new FileInputStream(new File("C:\\Users\\loliy\\Pictures\\33_minio.png")));
//        if (source_md5.equals(local_md5)) {
//            System.out.println("下载成功");
//        } else {
//            System.out.println("下载失败");
//        }
    }
}
