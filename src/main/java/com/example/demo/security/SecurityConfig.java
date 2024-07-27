package com.example.demo.security;

import com.example.demo.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException
;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/", "/login", "/index", "/cadusuario", "/css/**", "/js/**", "/img/**", "/uploads/**", "/fonts/**").permitAll();
                auth.requestMatchers("/admin/**").hasRole("ADMIN");
                auth.requestMatchers("/cadastro/**", "/salvar/**", "/editar/**", "/deletar/**").hasRole("ADMIN");
                auth.requestMatchers("/vercarrinho", "/adicionarcarrinho", "/finalizarcompra").hasRole("USER");
                auth.anyRequest().authenticated();
            })
            .formLogin(form -> form
                .loginPage("/login") 
                .loginProcessingUrl("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                        if (roles.contains("ROLE_ADMIN")) {
                            response.sendRedirect("/admin");
                        } else if (roles.contains("ROLE_USER")) {
                            response.sendRedirect("/index");
                        } else {
                            response.sendRedirect("/index");
                        }
                    }
                })
                .failureUrl("/login?error=true") 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

