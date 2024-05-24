package com.sid.gl.security;
import com.sid.gl.usercontext.helper.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailServiceImpl();
    }


    public static final String[] WIGHTLISTURL = new String[]{"/api/register",
            "/api/login",
            "/api/get-authenticated-user",
            "api/songs",
            "api/songs/search",
            "/api/v1/login-by-otp",
            "/api/v1/find-token/**",
            "/api/v1/country",
            "/api/v1/user/update-mfa/**",
            "/api/v1/user/formatjpeg/**",
            "/api/v1/user/formatpng/**",
            "/api/v1/user/formatpdf/**",
            "/api/v1/user/activate-by-otp",
            "/api/v1/user/send-for-password",
            "/api/v1/user/change-password",
            "api/v1/user/recovery-username",
            "/api/v1/refreshToken",
            "/api/v1/deconnexion",
            "/api/v1/user/register",
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/swagger-ui.css",
            "/v3/api-docs/**",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/favicon-32x32.png",
            "/swagger-ui/favicon-16x16.png"};


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize)->{
                    authorize.requestMatchers(WIGHTLISTURL).permitAll();
                    authorize.anyRequest().authenticated();
                }).authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
