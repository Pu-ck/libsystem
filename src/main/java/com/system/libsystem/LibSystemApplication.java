package com.system.libsystem;

import com.system.libsystem.config.UserEnabledInterceptor;
import com.system.libsystem.entities.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class LibSystemApplication implements WebMvcConfigurer {

    private static final String[] EXCLUDED_INTERCEPTOR_PATHS = {"/api/login",
            "/api/password-reminder/**", "/api/registration/**", "/api/logout", "/api/registered/**"};
    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(LibSystemApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserEnabledInterceptor(userService)).excludePathPatterns(EXCLUDED_INTERCEPTOR_PATHS);
    }

}
