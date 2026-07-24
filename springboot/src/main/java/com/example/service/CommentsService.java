package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.dao.CommentsMapper;
import com.example.entity.Comments;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论业务处理
 */
@Service
public class CommentsService {

    @Resource
    private CommentsMapper commentsMapper;

    /**
     * 新增
     */
    public void add(Comments comments) {
        if (comments.getFid() == null) {
            comments.setFid(0);
        }
        comments.setContent(com.example.common.HtmlSanitizer.cleanText(comments.getContent()));
        comments.setTime(DateUtil.now());
        commentsMapper.insert(comments);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        commentsMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Comments comments) {
        comments.setContent(com.example.common.HtmlSanitizer.cleanText(comments.getContent()));
        commentsMapper.updateById(comments);
    }

    /**
     * 根据ID查询
     */
    public Comments selectById(Integer id) {
        return commentsMapper.selectById(id);
    }

    /**
     * 查询所有（加载子评论）
     */
    public List<Comments> selectAll(Comments comments) {
        List<Comments> list = commentsMapper.selectAll(comments);
        for (Comments dbComment : list) {
            Comments cm = new Comments();
            cm.setFid(dbComment.getId());
            List<Comments> children = commentsMapper.selectAll(cm);
            dbComment.setChildren(children);
        }
        return list;
    }

    /**
     * 分页查询
     */
    public PageInfo<Comments> selectPage(Comments comments, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comments> list = commentsMapper.selectAll(comments);
        return PageInfo.of(list);
    }

}
