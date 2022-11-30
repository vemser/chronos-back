package br.com.dbc.chronosapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers()
                .frameOptions().disable()
                .and().cors()
                .and().csrf().disable()
//                .authorizeHttpRequests((auth) -> auth.antMatchers("/", "/login", "/login-forgot-password").permitAll()
                .authorizeHttpRequests((auth) -> auth.antMatchers("/**").permitAll()
                                // permissões
//                                .antMatchers(HttpMethod.GET,"/usuario/logged-user").hasAnyRole("INSTRUTOR", "GESTAO_DE_PESSOAS", "ADMIN")
//                                .antMatchers(HttpMethod.GET, "/usuario").hasAnyRole("ADMIN", "INSTRUTOR")
//                                .antMatchers(HttpMethod.POST, "/usuario").hasRole("ADMIN")
//                                .antMatchers(HttpMethod.DELETE, "/usuario/**").hasRole("ADMIN")
//                                .antMatchers("/usuario/enable-disable/**").hasRole("ADMIN")
//                                .antMatchers("/usuario/update-cadastro/**").hasRole("ADMIN")
//
//                                .antMatchers(HttpMethod.GET, "/edicao").hasAnyRole("GESTAO_DE_PESSOAS", "INSTRUTOR")
//                                .antMatchers(HttpMethod.POST, "/edicao").hasRole("GESTAO_DE_PESSOAS")
//                                .antMatchers(HttpMethod.DELETE, "/edicao/**").hasRole("GESTAO_DE_PESSOAS")
//                                .antMatchers("/edicao/**").hasRole("GESTAO_DE_PESSOAS")
//
//                                .antMatchers(HttpMethod.GET, "/etapa").hasAnyRole("GESTAO_DE_PESSOAS", "INSTRUTOR")
//                                .antMatchers(HttpMethod.POST, "/etapa").hasRole("GESTAO_DE_PESSOAS")
//                                .antMatchers(HttpMethod.DELETE, "/etapa/**").hasRole("GESTAO_DE_PESSOAS")
//                                .antMatchers("/etapa/**").hasRole("GESTAO_DE_PESSOAS")
//
//                                .antMatchers(HttpMethod.GET, "/processo").hasAnyRole("GESTAO_DE_PESSOAS", "INSTRUTOR")
//                                .antMatchers(HttpMethod.POST, "/processo").hasRole("GESTAO_DE_PESSOAS")
//                                .antMatchers(HttpMethod.DELETE, "/processo/**").hasRole("GESTAO_DE_PESSOAS")
//                                .antMatchers("/processo/**").hasRole("GESTAO_DE_PESSOAS")
//
//                                .antMatchers(HttpMethod.GET, "/dia-nao-util").hasAnyRole("GESTAO_DE_PESSOAS", "INSTRUTOR")
//                                .antMatchers(HttpMethod.POST, "/dia-nao-util").hasRole("GESTAO_DE_PESSOAS")
//                                .antMatchers(HttpMethod.DELETE, "/dia-nao-util").hasRole("GESTAO_DE_PESSOAS")
//                                .antMatchers("/dia-nao-util/**").hasRole("GESTAO_DE_PESSOAS")
//
//                                .antMatchers(HttpMethod.GET, "/**").hasRole("INSTRUTOR")

                                .anyRequest().authenticated()
                );
        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**"
        );
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
