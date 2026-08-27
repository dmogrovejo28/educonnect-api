package org.dmencia.examentcs.config;

import lombok.RequiredArgsConstructor;
import org.dmencia.examentcs.interceptor.AppHeaderInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final AppHeaderInterceptor
            appHeaderInterceptor;

    @Override
    public void addInterceptors(
            InterceptorRegistry registry
    ) {

        registry
                .addInterceptor(
                        appHeaderInterceptor
                );
    }
}
