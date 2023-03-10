package com.pyo.pyostagram.config;


import com.pyo.pyostagram.config.oauth.OAuth2DetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration  // IoC
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final OAuth2DetailsService oAuth2DetailsService;

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**","/api/**").authenticated()// 인증필요를 걸어둔것
                .anyRequest().permitAll() //나머지 허용
                .and()
                .formLogin()
                .loginPage("/auth/signin")  // 이건 get요청
                .loginProcessingUrl("/auth/signin") //이건 post(로그인이니까) -> 스프링 시큐리티가 로그인 프로세스 진행
                .defaultSuccessUrl("/")

                //oauth2 - 페이스북 로그인으로 인한 추가
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2DetailsService);



        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }

}
