package com.xuecheng.media;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinioBigFileTest {

    // 创建 minio 客户端
    MinioClient minioClient = MinioClient.builder()
            .endpoint("http://192.168.101.65:9000")
            .credentials("minioadmin", "minioadmin")
            .build();

    //上传文件块到minio
    @Test
    public void uploadChunk() {
        //块文件目录
        File chunkFolder = new File("C:\\Users\\loliy\\Videos\\chunk\\");
        //分块文件
        File[] files = chunkFolder.listFiles();
        //将分块文件上传至minio
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            try {
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                        .bucket("testbucket")
                        .filename(files[i].getAbsolutePath())
                        .object("/chunk/01/" + i)
                        .build();
                minioClient.uploadObject(uploadObjectArgs);
                System.out.println("上传分块成功" + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //合并文件，要求分块文件最小5M
    @Test
    public void test_merge() throws Exception {
        //源文件分块文件
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)
                .limit(6)
                .map(i -> ComposeSource.builder()
                        .bucket("testbucket")
                        .object("/chunk/01/".concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());

        //合并文件 (默认的分块大小为5MB)
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket("testbucket")
                .sources(sources)
                .object("/merge/01.mp4")
                .build();
        minioClient.composeObject(composeObjectArgs);
    }

    //清除分块文件
    @Test
    public void test_removeObjects() {
        //合并分块完成将分块文件清除
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                .limit(6)
                .map(i -> new DeleteObject("chunk/01/".concat(Integer.toString(i))))
                .collect(Collectors.toList());

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket("testbucket")
                .objects(deleteObjects)
                .build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        results.forEach(r -> {
            DeleteError deleteError = null;
            try {
                deleteError = r.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}