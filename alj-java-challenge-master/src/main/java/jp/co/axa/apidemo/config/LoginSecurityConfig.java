package jp.co.axa.apidemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Protect rest api endpoint access
 *
 * @author Laxmi
 */
@Configuration
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Roles created
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

    }

    /**
     * Secure the endpoins with HTTP Basic authentication
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/employees/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/api/v1/employees").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/api/v1/employees").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/employees/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/employees/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}