package br.com.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.google.common.cache.CacheBuilder;

@Configuration
@ComponentScan(basePackages = {"br.com.test"})
@EnableAutoConfiguration
@EnableCaching
@EnableAsync
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements AsyncConfigurer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		LOGGER.info("configure");
		return application.sources(Application.class);
	}
	
	public static void main(String[] args) throws Exception {
		LOGGER.info("main");
		SpringApplication.run(Application.class, args);
	}

	@Override
    public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Thread-");
        executor.initialize();
//		SimpleThreadPoolTaskExecutor executor = new SimpleThreadPoolTaskExecutor();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
			
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				ex.printStackTrace();
			}
		};
    }

	@Bean
	public <T> CacheManager cacheManager() {
		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
		GuavaCache cache1 = new GuavaCache("DEFAULT", CacheBuilder.newBuilder().build());

		GuavaCache cache2 = new GuavaCache("EXPIRE-30S-AFTER-ACCESS_60S-AFTER-WRITER",
				CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(30, TimeUnit.SECONDS)
						.expireAfterWrite(60, TimeUnit.SECONDS).build());

		simpleCacheManager.setCaches(Arrays.asList(cache1, cache2));
		return simpleCacheManager;
	}
	
}
