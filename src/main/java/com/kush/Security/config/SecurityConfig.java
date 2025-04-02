package com.kush.Security.config;

import com.kush.Security.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;


    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private final UserDetailsService userDetailsService;



    @Bean
    public AuthenticationProvider authProvider() { // security guard who checks ur credentials
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();// this goes to the db to look for ur info
        provider.setUserDetailsService(userDetailsService);// with the help of userDetails Service like an assistant
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));// make password strong and checks it
        return provider;//returns the user
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/register/user", "/api/login").permitAll() // Public routes
                        .requestMatchers("/api/users").hasRole("ADMIN") // Only Admins can access all users
                        .requestMatchers("/api/users/{username}").authenticated() // Only authenticated users can access profiles
                        .requestMatchers("/api/users/uploadProfilePicture").authenticated()
                        .anyRequest().authenticated()) // All other requests need authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }





}
