package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author Msq
 * @date 2020/12/17 - 11:35
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJSONString(403, "您还没有登录噢!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况, 将来统一处理
        return CommunityUtil.getJSONString(0, "发送成功!");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDisgussPost(@PathVariable("discussPostId") int discussPostId, Model model){
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);

        // post 显示的是作者id, but我们希望显示头像
        // 使用userservice 来查询作者头像进行显示
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 查看"回复"待补充
        return "/site/discuss-detail";
    }
}
