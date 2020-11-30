package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Msq
 * @date 2020/11/30 - 20:00
 */
public class CookieUtil {

    public static String getValue(HttpServletRequest request, String name){
        if(request == null || name == null)
            throw new IllegalArgumentException("参数为空！");

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
