package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanTreeDto;

import java.util.List;

public interface TeachplanService {

    //查询课程计划树型结构
    List<TeachplanTreeDto> findTeachplanTree(Long courseId);

    //课程计划创建或修改(章/节)
    void saveTeachplan(SaveTeachplanDto saveTeachplanDto);
}
