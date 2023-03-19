package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.validation.ValidationGroups;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description 课程信息接口
 */
@Api(value = "课程信息接口", tags = "课程信息接口")
@Slf4j
@RestController
public class CourseBaseInfoController {

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    @ApiOperation(value = "测试接口")
    @GetMapping("/course/hello")
    public String hello() {
        return "hello";
    }

    @ApiOperation(value = "课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParams) {
        PageResult<CourseBase> pageResult = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParams);
        return pageResult;
    }

    /**
     *
     * @param addCourseDto
     * 使用JSR303检验, 需要
     * 1.导包 spring-boot-starter-validation 2.dto 配置校验规则 3.接口参数开启校验 @Validated
     * 如果使用的全局异常捕获 还需要在 @ControllerAdvice 捕获 MethodArgumentNotValidException
     * 使用JSR303分组校验, 需要在原来的基础上
     * 1. 配置分组接口 2. dto 注释原先规则, 在指定校验分组的基础上配置校验规则
     * 3. 接口参数 @Validated 设置校验规则的分组
     * @return
     */
    @ApiOperation("新增课程基础信息(包括营销信息)")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(
            @RequestBody @Validated({ValidationGroups.Insert.class}) AddCourseDto addCourseDto) {
        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;
        return courseBaseInfoService.createCourseBase(companyId, addCourseDto);
    }

}
