package cn.jiuyue.bootstraptable.controller;

import cn.jiuyue.bootstraptable.model.Comment;
import cn.jiuyue.bootstraptable.service.CommentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Create bySeptember
 * 2019/8/15
 * 10:00
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<Comment> list(Integer page, Integer size){
        page = 0;
        size = 10;
        PageInfo<Comment> list = commentService.list(page, size);
        return list;
    }
}
