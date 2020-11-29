package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author Msq
 * @date 2020/11/21 - 13:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextmail(){
        System.out.println("start,to 1371908769");
        mailClient.sendMail("1371908769@qq.com","test by msq","hello,I am a test mail");
        System.out.println("end");
    }

    @Test
    public void testHtmlMail(){
        System.out.println("start");
        Context context = new Context();
        context.setVariable("username","heliu");
        String content = templateEngine.process("/mail/mailDemo.html", context);
        System.out.println(content);

        mailClient.sendMail("1084725650@qq.com", "你好哟",content);
        System.out.println("end");
    }

}
