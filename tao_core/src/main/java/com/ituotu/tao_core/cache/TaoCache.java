package com.ituotu.tao_core.cache;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaoCache {
    public static ApplicationContext appContext;
    public static ExecutorService mainTaskThreadPool = Executors.newCachedThreadPool();
    public static ExecutorService caijiTaskThreadPool = Executors.newCachedThreadPool();
    public static DruidDataSource dataSource;
}
