package com.xuecheng.media;

import com.xuecheng.base.utils.Mp4VideoUtil;
import org.junit.jupiter.api.Test;

public class VideoProcessTest {

    @Test
    public void testProcess(){
        //ffmpeg的路径
        String ffmpeg_path = "D:\\_SoftWare\\FFmpeg\\bin\\ffmpeg.exe";//ffmpeg的安装位置
        //源avi视频的路径
        String video_path = "C:\\Users\\loliy\\Videos\\desktop.wmv";
        //转换后mp4文件的名称
        String mp4_name = "desktop.mp4";
        //转换后mp4文件的路径
        String mp4_path = "C:\\Users\\loliy\\Videos\\desktop.mp4";
        //创建工具类对象
        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4_path);
        //开始视频转换，成功将返回success
        String s = videoUtil.generateMp4();
        System.out.println(s);
    }
}
