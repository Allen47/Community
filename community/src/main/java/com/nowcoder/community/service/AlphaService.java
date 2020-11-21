package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;  //注入的是AlphaDao的默认实现类，有两个时注入标注了@Primary的对象

    public AlphaService() {
//        System.out.println("实例化AlphaService");
    }

    @PostConstruct  //这个方法会在构造器结束之后让容器自动执行
    public void init() {
//        System.out.println("初始化AlphaService");
    }

    @PreDestroy   //在对象销毁之前自动执行销毁方法
    public void destroy() {
//        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }

}
