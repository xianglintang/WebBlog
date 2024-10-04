package com.tangxianglin.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class WebBlogApplicationTests {

    @Test
    void contextLoads() {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String time = "2019-09-19";
        try {
            Date date = ft.parse(time);
            System.out.println(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
