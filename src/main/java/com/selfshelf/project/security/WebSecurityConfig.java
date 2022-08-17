package com.selfshelf.project.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static com.selfshelf.project.model.UserRole.ADMIN;
import static com.selfshelf.project.model.UserRole.USER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","index","/css/*", "/js/*",
                        "signup_form", "/register", "/process_register",
                        "registration_successful","/h2/**").permitAll()
                .antMatchers("/books",
                        "/book-main",
                        "/book/new",
                        "/books",
                        "/books/edit/{id}",
                        "/books/{id}",
                        "/books/info/{id}",
                        "/books/search",
                        "/books/search",
                        "/books/faq",
                        "/reservation/**",
                        "/book-main", "books","/books","faq", "/books/faq").hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers("/users/update/{id}").hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers( HttpMethod.GET,"/books/{id}").hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers("/issued/user").hasRole(USER.name())
                .antMatchers("/users/**", "/issued/admin", "/issued/return/{id}", "/issued/return/{id}").hasRole(ADMIN.name())
                .antMatchers(HttpMethod.GET, "/issued/return/{id}").hasRole(ADMIN.name())
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/account")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/").permitAll();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

}
