package com.easymeeting;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * EasyMeeting 会议管理系统启动类
 * 
 * @author EasyMeeting
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.easymeeting"})
@MapperScan("com.easymeeting.mappers")
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
 
public class EasymeetingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasymeetingApplication.class, args);

    }
}
