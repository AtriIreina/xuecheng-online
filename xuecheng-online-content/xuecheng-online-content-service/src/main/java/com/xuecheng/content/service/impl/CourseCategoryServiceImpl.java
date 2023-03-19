package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> selectTreeNodes(String id) {
        List<CourseCategoryTreeDto> categoryList = courseCategoryMapper.selectTreeNodes(id);
        //将list转map(方便根据id找到节点)
        // List转Map: stream().collect(Collectors.toMap())
        // stream().filter() 当结果true则保留
        // Collectors.toMap() 第一个选定map的key, 第二个选定map的value, 第三个指定key重复时的选哪个
        Map<String, CourseCategoryTreeDto> mapTemp = categoryList.stream()
                // 排除当前tree的根节点 (list 不需要根节点)
                .filter(item -> !id.equals(item.getId()))
                // list转map
                .collect(Collectors.toMap(item -> item.getId(), value -> value, (key1, key2) -> key2));

        //最终返回的list
        List<CourseCategoryTreeDto> categoryTreeDtoList = new ArrayList<>();
        //排除根节点, 再遍历
        categoryList.stream()
                .filter(item -> !id.equals(item.getId()))
                .forEach(item -> {
                    //父节点放入 categoryTreeDtoList
                    if (item.getParentid().equals(id)) {
                        categoryTreeDtoList.add(item);
                    }
                    //子节点放入父节点的ChildrenTreeNodes
                    //找到当前节点的父节点
                    CourseCategoryTreeDto courseCategoryParent = mapTemp.get(item.getParentid());
                    if (courseCategoryParent != null) {
                        //如果父节点的childrenTreeNodes为空, 需要new一个集合, 再放入子节点
                        if (courseCategoryParent.getChildrenTreeNodes() == null) {
                            courseCategoryParent.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                        }
                        //向ChildrenTreeNodes属性中放子节点
                        /*
                         * 1-1 节点 通过 categoryTreeDtoList.add(item) 放入 categoryTreeDtoList
                         * 1-1-1 节点 通过 mapTemp.get() 获取到父节点 courseCategoryParent
                         * 1-1-1 节点 通过 courseCategoryParent.getChildrenTreeNodes().add() 放入父节点 1-1
                         * 父节点已放入list, 但是子节点只是放入了临时的父节点 courseCategoryParent, 而父节点没有放入 list ?
                         * 造成理解困难的原因: java对象引用机制 !
                         * 解释:
                         *  categoryList 和 mapTemp 只是list对象和map对象在内存的引用不同
                         *  但是对于这两个对象, 在内存里的 CourseCategoryTreeDto 指向同一个引用
                         *  也就是 mapTemp.get() 获取到的父节点, 在之前的遍历中已通过 categoryTreeDtoList.add() 添加到 list
                         */
                        courseCategoryParent.getChildrenTreeNodes().add(item);
                    }
                });
        return categoryTreeDtoList;
    }

}