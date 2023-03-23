package com.xuecheng.media;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//@SpringBootTest
public class BigFileTest {

    //测试文件分块方法
    @Test
    public void testChunk() throws IOException {
        //源文件
        File sourceFile = new File("C:\\Users\\loliy\\Videos\\Konachan.mp4");
        //分块文件存储路径
        String chunkPath = "C:\\Users\\loliy\\Videos\\chunk\\";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }
        //分块大小 5MB = 1024 * 1024 * 5 bit 位
        long chunkSize = 1024 * 1024 * 5;
        //分块数量 浮点运算 上取整
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        System.out.println("分块总数：" + chunkNum);
        //缓冲区大小 5MB = 1024 * 5 byte 字节
        byte[] b = new byte[1024 * 5];
        // RandomAccessFile 可读可写
        // 读原文件
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        //分块
        for (int i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            if (file.exists()) {
                file.delete();
            }
//            boolean newFile = file.createNewFile();
//            if (newFile) {
            //向分块文件中写数据
            RandomAccessFile raf_write = new RandomAccessFile(file, "rw");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
                //限定分块文件的大小
                if (file.length() >= chunkSize) {
                    break;
                }
            }
            raf_write.close();
            System.out.println("完成分块" + i);
        }

//        }
//        raf_read.close();

    }


    //测试文件合并方法
    @Test
    public void testMerge() throws IOException {
        //块文件目录
        File chunkFolder = new File("C:\\Users\\loliy\\Videos\\chunk\\");
        //合并文件的存放目录
        File mergeFolder = new File("C:\\Users\\loliy\\Videos\\merge\\");
        if (!mergeFolder.exists()) {
            mergeFolder.mkdirs();
        }
        File mergeFile = new File("C:\\Users\\loliy\\Videos\\merge\\Konachan.mp4");
        //创建新的合并文件 (文件夹不会自动创建, 但把流写入文件时 会自动创建)
        // mergeFile.createNewFile();
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端 (默认是指向顶端的)
        //raf_write.seek(0);
        //缓冲区
        byte[] b = new byte[1024 * 5];
        //取出所有的分块文件
        File[] fileArray = chunkFolder.listFiles();
        // 转成集合，便于排序
        List<File> fileList = Arrays.asList(fileArray);
        // 对块文件进行 小到大排序
        Collections.sort(fileList, Comparator.comparingInt(o -> Integer.parseInt(o.getName())));
        //合并文件
        for (File chunkFile : fileList) {
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();

        //源文件
        File sourceFile = new File("C:\\Users\\loliy\\Videos\\Konachan.mp4");
        //校验文件
        validateFile(sourceFile, mergeFile);
    }

    //校验文件
    private void validateFile(File sourceFile, File mergeFile) {
        try (FileInputStream fileInputStream = new FileInputStream(sourceFile);
             FileInputStream mergeFileStream = new FileInputStream(mergeFile);) {
            //取出原始文件的md5
            String originalMd5 = DigestUtils.md5Hex(fileInputStream);
            //取出合并文件的md5进行比较
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileStream);
            if (originalMd5.equals(mergeFileMd5)) {
                System.out.println("合并文件成功");
            } else {
                System.out.println("合并文件失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}