package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

public interface CourseCategoryService {

    //使用递归查询分类, 不需要根节点
    List<CourseCategoryTreeDto> selectTreeNodes(String id);

}
