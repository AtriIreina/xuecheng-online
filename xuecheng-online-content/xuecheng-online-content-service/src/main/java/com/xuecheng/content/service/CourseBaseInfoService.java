package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;

/**
 * @description 课程基本信息管理业务接口
 */
public interface CourseBaseInfoService {

    /**
     * @param pageParams           分页参数
     * @param queryCourseParamsDto 条件条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
     * @description 课程分页查询接口
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * @param companyId    教学机构id
     * @param addCourseDto 课程基本信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     * @description 添加课程基本信息
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    //保存课程营销信息, 存在则更新, 不存在则添加
    int saveCourseMarket(CourseMarket courseMarketNew);

    //根据课程id查询课程基本信息，包括基本信息和营销信息
    CourseBaseInfoDto getCourseBaseInfo(Long courseId);

    //修改课程基础信息
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto);
}
