package com.yumyumcoach.global.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yumyumcoach.domain")
public class MyBatisConfig {
}
