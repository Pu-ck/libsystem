package com.system.libsystem.config;

import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.session.SessionFilter;
import com.system.libsystem.util.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class Config {

    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    @Lazy
    private SessionFilter sessionFilter;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http = http.exceptionHandling().authenticationEntryPoint(
                (request, response, exception) -> response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        exception.getMessage())
        ).and();

        http.authorizeRequests()
                .antMatchers("/api/login", "/api/password-reminder", "/api/registration/**", "/api/logout")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/administration/books/extend-book")
                .hasRole(UserType.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/api/administration/books/confirm-order")
                .hasRole(UserType.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/api/administration/books/return")
                .hasRole(UserType.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        http.addFilterBefore(
                sessionFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        http.logout().clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/home/logout"))
                .logoutSuccessUrl("/home/login").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
