package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有user对象，代替session对象
 * @author Msq
 * @date 2020/11/30 - 22:54
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users =  new ThreadLocal<>();

    public void setUser(User user){ users.set(user);}

    public User getUser(){ return users.get();}

    public void clear(){ users.remove();}
}
