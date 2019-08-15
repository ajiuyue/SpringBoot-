package cn.jiuyue.bootstraptable.service.impl;

import cn.jiuyue.bootstraptable.mapper.CommentMapper;
import cn.jiuyue.bootstraptable.model.Comment;
import cn.jiuyue.bootstraptable.model.CommentExample;
import cn.jiuyue.bootstraptable.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create bySeptember
 * 2019/8/15
 * 10:16
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageInfo<Comment> list(Integer page, Integer size) {
        //设置分页信息
        PageHelper.startPage(page,size);
        //在mysql中分页 limit,在下面查询sql中拼接
        CommentExample example = new CommentExample();
        example.isDistinct();
        List<Comment> list = commentMapper.selectByExampleWithRowbounds(example, new RowBounds(page, size));
        //获取分页信息
        PageInfo<Comment> pageInfoCommentList = new PageInfo<>(list);
        return pageInfoCommentList;
    }
}
