package com.feiqu;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author chenweidong
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.feiqu.*.mapper")
@EnableScheduling
public class FeiQuApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FeiQuApplication.class);

        springApplication.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  飞趣社区启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}