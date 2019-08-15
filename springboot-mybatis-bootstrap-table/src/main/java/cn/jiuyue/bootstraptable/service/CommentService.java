package cn.jiuyue.bootstraptable.service;

import cn.jiuyue.bootstraptable.model.Comment;
import com.github.pagehelper.PageInfo;

/**
 * Create bySeptember
 * 2019/8/15
 * 10:15
 */
public interface CommentService {
    PageInfo<Comment> list(Integer page, Integer size);
}
