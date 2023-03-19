package com.xuecheng.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @description 分页查询通用参数
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页请求参数")
public class PageParams {

    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private Long pageNo = 1L;

    /**
     * 每页记录数默认值
     */
    @ApiModelProperty("分页大小")
    private Long pageSize = 10L;

}
