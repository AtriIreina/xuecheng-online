package com.xuecheng.content.feignclient;

import com.xuecheng.content.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description 媒资管理服务远程接口
 * configuration 指定一个配置, 使openFeign支持上传MultipartFile
 * fallback 和 fallbackFactory 指定降级处理
 * fallback 无法拿到熔断异常
 * fallbackFactory 实现 FallbackFactory, 可以拿到熔断异常信息
 */

@FeignClient(
        value = "media-api",
        configuration = MultipartSupportConfig.class,
        //fallback =MediaServiceClientFallback.class,
        fallbackFactory = MediaServiceClientFallbackFactory.class
)
public interface MediaServiceClient {

    @RequestMapping(value = "/media/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(@RequestPart("filedata") MultipartFile upload,
                      @RequestParam(value = "objectName", required = false) String objectName);

}
