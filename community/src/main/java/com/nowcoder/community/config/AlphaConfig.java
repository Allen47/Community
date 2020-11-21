package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class AlphaConfig {


    /*
        用来装配第三方类到Spring容器中，该方法返回的对象将被装配到容器中，name是“simpleDateFormat”
        主动获取其他的类实例
       （比较笨拙，有更灵活的依赖注入 @Autowired标注在属性之前，也可加到构造器、set方法之前）
    */
    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}
