package com.example.service;

import com.example.dao.ChapterMapper;
import com.example.entity.Chapter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 章节业务处理
 */
@Service
public class ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    public void add(Chapter chapter) {
        chapterMapper.insert(chapter);
    }

    public void deleteById(Integer id) {
        chapterMapper.deleteById(id);
    }

    public void updateById(Chapter chapter) {
        chapterMapper.updateById(chapter);
    }

    public Chapter selectById(Integer id) {
        return chapterMapper.selectById(id);
    }

    public List<Chapter> selectAll(Chapter chapter) {
        return chapterMapper.selectAll(chapter);
    }

    public PageInfo<Chapter> selectPage(Chapter chapter, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Chapter> list = chapterMapper.selectAll(chapter);
        return PageInfo.of(list);
    }

}
