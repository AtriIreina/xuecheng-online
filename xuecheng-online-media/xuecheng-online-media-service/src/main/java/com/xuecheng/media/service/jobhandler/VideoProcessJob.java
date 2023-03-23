package com.xuecheng.media.service.jobhandler;

import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import com.xuecheng.media.service.MediaFileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class VideoProcessJob {

    @Autowired
    MediaFileService mediaFileService;

    @Autowired
    MediaFileProcessService mediaFileProcessService;

    @Value("${videoprocess.ffmpegpath}")
    private String ffmpegpath;

    @XxlJob("videoProcessJobHandle")
    public void shardingJobHandler() throws Exception {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();  //执行器序号, 从0开始
        int shardTotal = XxlJobHelper.getShardTotal();  //执行器数量

        int cpuCoreCount = Runtime.getRuntime().availableProcessors();  //cpu核心数
        //乐观锁 获取待处理的任务, 一次处理视频数量不要超过cpu核心数
        List<MediaProcess> mediaProcessList = mediaFileProcessService.getMediaProcessList(
                shardIndex, shardTotal, cpuCoreCount);
        int size = mediaProcessList.size();
        if (size <= 0) {
            return;
        }
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        //使用计数器
        CountDownLatch countDownLatch = new CountDownLatch(size);
        mediaProcessList.forEach(mediaProcess -> {
            try {
                //开启任务 (修改任务处理状态)
                boolean b = mediaFileProcessService.startTask(mediaProcess.getId());
                if (!b) {
                    log.debug("任务抢占失败:{}", mediaProcess.getId());
                    return;
                }

                //视频转码
                //1.下载视频
                String bucket = mediaProcess.getBucket();
                String filePath = mediaProcess.getFilePath();
                String fileId = mediaProcess.getFileId();
                File originalFile = mediaFileService.downloadFileFromMinIO(bucket, filePath);
                if (originalFile == null) {
                    log.debug("下载待处理文件失败,originalFile:{}", mediaProcess.getBucket().concat(mediaProcess.getFilePath()));
                    mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileId, null, "下载待处理文件失败");
                    return;
                }

                //2.进行转码
                File mp4File = null;
                try {
                    mp4File = File.createTempFile("mp4", ".mp4");
                } catch (IOException e) {
                    log.error("处理视频文件-创建临时文件异常:{},出错:{}", mediaProcess.getFilePath(), e.getMessage());
                    mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileId, null, "创建临时文件异常");
                    return;
                }
                Mp4VideoUtil videoUtil = new Mp4VideoUtil(
                        ffmpegpath,
                        originalFile.getAbsolutePath(),
                        mp4File.getName(),
                        mp4File.getAbsolutePath());
                //开始视频转换，成功将返回success
                String result = videoUtil.generateMp4();
                if (!"success".equals(result)) {
                    log.error("处理视频失败,视频地址:{},错误信息:{}", bucket + filePath, result);
                    mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileId, null, "处理视频失败:" + result);
                    return;
                }

                //3.上传视频
                String objectName = getFilePath(fileId, ".mp4");
                boolean b1 = mediaFileService.addMediaFiles2MinIO(
                        mp4File.getAbsolutePath(),
                        "video/mp4",
                        bucket,
                        objectName);
                if (!b1) {
                    log.error("上传视频失败,视频地址:{}", bucket + objectName);
                    mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId(), "3", fileId, null, "处理后视频上传或入库失败");
                    return;
                }
                //保存任务处理结果 (将url存储至数据，并更新状态为成功，并将待处理视频记录删除存入历史)
                String url = "/" + bucket + "/" + objectName;
                mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId(), "2", fileId, url, null);
            } finally {
                countDownLatch.countDown();
            }
        });
        //计数器阻塞 有限等待
        countDownLatch.await(30, TimeUnit.MINUTES);
    }

    private String getFilePath(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" +
                fileMd5.substring(1, 2) + "/" +
                fileMd5 + "/" + fileMd5 + fileExt;
    }

}