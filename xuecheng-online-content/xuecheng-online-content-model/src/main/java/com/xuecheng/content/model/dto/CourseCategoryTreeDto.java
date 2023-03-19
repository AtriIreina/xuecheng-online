package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 课程分类树型结点dto
 */
@Data
@ToString
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {

    //子节点
    List<CourseCategoryTreeDto> childrenTreeNodes;

}
