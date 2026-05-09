package com.project.notebook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig — the traditional, resume-worthy Spring Security setup.
 *
 * Key classes used:
 *
 * 1. UserDetailsService       — our CustomUserDetailsService loads the user from DB
 * 2. DaoAuthenticationProvider — wires UserDetailsService + PasswordEncoder together.
 *                                Spring uses this to authenticate login attempts.
 * 3. AuthenticationManager    — the entry point Spring Security uses to trigger
 *                                authentication. We expose it as a Bean so our
 *                                UserController can call it manually for login.
 * 4. HttpSecurity             — fluent API to configure routes, session policy,
 *                                filters, CSRF etc.
 * 5. PasswordEncoder          — BCrypt bean used everywhere passwords are touched.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter           jwtAuthFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter      = jwtAuthFilter;
    }

    /**
     * SecurityFilterChain — defines the HTTP security rules.
     *
     * HttpSecurity lets us configure:
     *   - Which routes are public vs protected
     *   - Session policy (STATELESS = no server-side sessions, JWT handles it)
     *   - CSRF (disabled for REST APIs — CSRF attacks require cookies/sessions)
     *   - Which filter runs before which
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/users/register",
                    "/api/users/login"
                ).permitAll()
                .anyRequest().authenticated()
            )

            .authenticationProvider(authenticationProvider())

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * DaoAuthenticationProvider — the traditional Spring Security authenticator.
     *
     * Wires together:
     *   - UserDetailsService  → loads the user from DB by email
     *   - PasswordEncoder     → compares the raw password against the BCrypt hash
     *
     * Spring Security calls this when AuthenticationManager.authenticate() is invoked.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * AuthenticationManager — Spring Security's central authentication entry point.
     *
     * Exposed as a Bean so UserController can inject it and call:
     *   authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))
     * which triggers the full DaoAuthenticationProvider → UserDetailsService flow.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /** BCrypt — used on register (hash) and by DaoAuthenticationProvider on login (compare). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
