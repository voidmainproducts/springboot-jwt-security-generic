package com.myapp.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.myapp.security.user.Permission.ADMIN_CREATE;
import static com.myapp.security.user.Permission.ADMIN_DELETE;
import static com.myapp.security.user.Permission.ADMIN_READ;
import static com.myapp.security.user.Permission.ADMIN_UPDATE;
import static com.myapp.security.user.Permission.USER_CREATE;
import static com.myapp.security.user.Permission.USER_DELETE;
import static com.myapp.security.user.Permission.USER_READ;
import static com.myapp.security.user.Permission.USER_UPDATE;
import static com.myapp.security.user.Role.ADMIN;
import static com.myapp.security.user.Role.USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationEntryPoint unAuthorisedHandler;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .cors().disable()
            .authorizeHttpRequests()
            .requestMatchers(
                    "/api/v1/auth/**",
                    "/v2/api-docs",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-resources",
                    "/swagger-resources/**",
                    "/configuration/ui",
                    "/configuration/security",
                    "/swagger-ui/**",
                    "/webjars/**",
                    "/swagger-ui.html"
            )
            .permitAll()


            .requestMatchers("/api/v1/user/**").hasAnyRole(ADMIN.name(), USER.name())
            .requestMatchers(GET, "/api/v1/user/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
            .requestMatchers(POST, "/api/v1/user/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
            .requestMatchers(PUT, "/api/v1/user/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
            .requestMatchers(DELETE, "/api/v1/user/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())

            .requestMatchers(GET, "/api/v1/feedback/all").hasAnyRole(ADMIN.name())
            .requestMatchers(POST,"/api/v1/feedback/create").hasAnyRole(ADMIN.name(), USER.name())
            .requestMatchers(DELETE, "/api/v1/feedback/delete").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
            .requestMatchers(GET, "/api/v1/feedback").hasAnyRole(ADMIN.name(), USER.name())


            .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
//
//            .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
//            .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
//            .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
//            .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())


            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(unAuthorisedHandler)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout()
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    ;

    return http.build();
  }
}
