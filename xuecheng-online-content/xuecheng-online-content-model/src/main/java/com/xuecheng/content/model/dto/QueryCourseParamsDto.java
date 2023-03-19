package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description 课程查询参数Dto
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课程请求参数")
public class QueryCourseParamsDto {
    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String auditStatus;
    /**
     * 课程名称
     */
    @ApiModelProperty("课程名称")
    private String courseName;
    /**
     * 发布状态
     */
    @ApiModelProperty("发布状态")
    private String publishStatus;

}
