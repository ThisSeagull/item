package com.cq.start.controller;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@MapperScan("com.cq.start.service.*.mapper*")
public class MybatisPlusConfig {
    @Bean
    public PerformanceInterceptor  paginationInterceptor() {
        return new PerformanceInterceptor();
    }
    @Bean
    public PaginationInterceptor paginationInterceptor1() {
        return new PaginationInterceptor();
    }
}
