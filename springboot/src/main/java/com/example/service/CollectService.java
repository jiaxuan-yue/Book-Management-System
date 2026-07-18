package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.entity.Collect;
import com.example.dao.CollectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏业务处理
 **/
@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;

    /**
     * 新增/取消收藏（切换逻辑）
     * 如果已收藏则删除，如果未收藏则新增
     */
    public void add(Collect collect) {
        List<Collect> collects = collectMapper.selectAll(collect);
        if (CollectionUtil.isNotEmpty(collects)) {
            // 之前收藏过，取消收藏
            collectMapper.deleteById(collects.get(0).getId());
        } else {
            // 之前没收藏过，新增收藏
            collect.setTime(DateUtil.now());
            collectMapper.insert(collect);
        }
    }

    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    public List<Collect> selectAll(Collect collect) {
        return collectMapper.selectAll(collect);
    }

    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }

}
