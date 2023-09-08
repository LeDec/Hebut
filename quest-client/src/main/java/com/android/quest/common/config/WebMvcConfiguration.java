package com.android.quest.common.config;


import com.android.quest.common.interceptor.AccessTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义注册配置
 *
 * @author 作者
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 注入拦截器
     *
     * @return AccessTokenInterceptor
     */
    @Bean
    public AccessTokenInterceptor accessTokenInterceptor() {
        return new AccessTokenInterceptor();
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加自定义的token拦截器，然后配置拦截器的规则
        registry.addInterceptor(accessTokenInterceptor())
                // 添加此拦截器匹配的 所有请求路径
                .addPathPatterns("/**")
                // 此拦截器需要排除的请求路径
                .excludePathPatterns("/file/**");
    }
}
