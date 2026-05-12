package com.securitylab.security;


import com.securitylab.filter.MyFilter;
import jakarta.servlet.http.HttpSessionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.DelegatingFilterProxy;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.core.userdetails.User.withUsername;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class DefaultSecurityConfig {

    private final MyFilter myfilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        return http.sessionManagement(
                        sm -> sm.sessionConcurrency(
                                sc -> sc.maximumSessions(1)
                                        .expiredUrl("/login?expired")
                        )
                ).

                authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/**").hasRole("USER")

                )
                .formLogin(withDefaults()).

                build();

    }

    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();
        UserDetails admin = User
                .withUsername("admin")
                .password(encoder.encode("password"))
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

}
