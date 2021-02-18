package com.ironhack.bankingsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().ignoringAntMatchers("/admin/*")
                .ignoringAntMatchers("/account/*")
                .ignoringAntMatchers("/transfer-money")
                .ignoringAntMatchers("/send-money/*")
                .ignoringAntMatchers("/receive-money/*");
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/admin/account-balance/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/admin/account-holder").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/admin/third-party").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/admin/checking").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/admin/saving").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/admin/credit-card").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/admin/account-balance/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/admin/unfreeze-account/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/account/*").hasRole("ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.PATCH, "/transfer-money").hasRole("ACCOUNT_HOLDER")
                .antMatchers(HttpMethod.PATCH, "/receive-money/*").hasRole("THIRD_PARTY")
                .antMatchers(HttpMethod.PATCH, "/send-money/*").hasRole("THIRD_PARTY")
                .anyRequest().permitAll();
    }
}
