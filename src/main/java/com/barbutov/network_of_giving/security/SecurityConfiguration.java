package com.barbutov.network_of_giving.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/charity/create").authenticated()
                .antMatchers("/charity/*").authenticated()
                .antMatchers("/profile").authenticated()
                .antMatchers("/profile/*").authenticated()
                .antMatchers("/charity/donate/*").authenticated()
                .antMatchers("/charity/delete/*").authenticated()
                .antMatchers("/charity/volunteer/*").authenticated()
                .antMatchers("/charity/withhold/*").authenticated()
                .antMatchers("/isVolunteered/*").authenticated()
                .antMatchers("/charity/name/*").authenticated()
                .antMatchers("/charity/edit/*").authenticated()
                .antMatchers("/user").authenticated()
                .and().csrf().disable().formLogin().loginPage("/login").defaultSuccessUrl("/")
                .and().logout().logoutSuccessUrl("/").init(http);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
