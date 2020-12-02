package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Msq
 * @date 2020/12/2 - 12:05
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){ return "/site/setting";}
}
