package com.xuecheng.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


/**
 * @desription 分页查询结果模型类
 * 会在不同服务中传输, 因此需要实现 Serializable 接口
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    //数据列表
    private List<T> items;

    //总记录数
    private long counts;

    //当前页码
    private long pageNo;

    //每页记录数
    private long pageSize;

}
