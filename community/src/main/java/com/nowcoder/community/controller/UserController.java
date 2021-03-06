package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Msq
 * @date 2020/12/2 - 12:05
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uplodPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){ return "/site/setting";}

    /*
    multipartFile 用于接收文件
    model 用于给出各种页面提示
     */
    @LoginRequired
    @RequestMapping(path = "/uploadHeader", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile multipartFile, Model model){
        if(multipartFile == null ){
            model.addAttribute("headerError", "您还没有选择图片");
            return "site/setting";
        }

        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));  // --> suffix = ".png"
        if(StringUtils.isBlank(suffix) || (!suffix.equals(".png")
            && !suffix.equals(".jpg") && !suffix.equals(".jpeg"))){
            model.addAttribute("headerError", "文件格式不正确");
            return "site/setting";
        }

        //生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //指定文件存放的路径
        File dest = new File(uplodPath + "/" + fileName);
        try {
            multipartFile.transferTo(dest); //存储文件
        } catch (IOException e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }

        /*
            访问者无法通过d盘来读取头像
            更新当前用户的头像路径（web访问路径)
            eg: http://localhost:8080/community/user/header/xxx.png
        */
        User user = hostHolder.getUser();
        String headerUrl =domain + contextPath + "/user/header/" +fileName;
//        user.setHeaderUrl();
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";

    }

    @RequestMapping(path = "/header/{filename}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        // 服务器存放路径
        filename = uplodPath + "/" + filename;
        // 文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream is = new FileInputStream(filename);
                OutputStream os = response.getOutputStream();
        ){
                byte[] buffer = new byte[1024];
                int b = 0;
                while( (b=is.read(buffer)) != -1){
                    os.write(buffer,0,b);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @LoginRequired
    @RequestMapping(path = "/uploadPassword", method = RequestMethod.POST)
    public String uploadPassword(String oldPasword, String newPassword1, String newPassword2, Model model){
        if(oldPasword == null || StringUtils.isBlank(oldPasword)){
            model.addAttribute("error", "您未输入密码！");
            return "site/setting";
        }
        if(oldPasword.length() < 8){
            model.addAttribute("oldError", "密码长度不能小于8位!");
            return "site/setting";
        }

        User user = hostHolder.getUser();
        String originPw = user.getPassword();
        oldPasword = CommunityUtil.md5(oldPasword + user.getSalt());
        if(newPassword1.length() < 8){
            model.addAttribute("error", "密码长度不能小于8位!");
            return "site/setting";
        }
        if(!oldPasword.equals(originPw)){
            model.addAttribute("oldError", "与原密码不一致！");
            return "site/setting";
        }

        if(!newPassword1.equals(newPassword2)){
            model.addAttribute("error", "两次输入的密码不一致！");
            return "site/setting";
        }

        newPassword1 = CommunityUtil.md5(newPassword1 + user.getSalt());
        userService.updatePassword(user.getId(),newPassword1);

        return "redirect:/index";

    }
}



