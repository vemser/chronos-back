package br.com.dbc.chronosapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        http.headers().frameOptions().disable().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((authz) ->
                        authz.antMatchers("/auth", "/auth/cadastro-usuario", "/auth/recuperacao-senha").permitAll()
//                                .antMatchers("/auth/alteracao-senha").hasRole("RECUPERACAO")
//                                .antMatchers(HttpMethod.GET, "/item", "/item/itens-paginados", "/item/filtro").hasAnyRole("CLIENTE", "ADMIN")
//                                .antMatchers(HttpMethod.GET, "/avaliacao/{idUsuario}/user", "/avaliacao/ids").hasRole("ADMIN")
//                                .antMatchers("/lancamentos/**").hasAnyRole("CLIENTE", "ADMIN")
//                                .antMatchers("/assistidos/**").hasAnyRole("CLIENTE", "ADMIN")
//                                .antMatchers("/avaliacao/**").hasAnyRole("CLIENTE", "ADMIN")
//                                .antMatchers("/indicacao/**").hasAnyRole("CLIENTE", "ADMIN")
//                                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
//                                .antMatchers("/**").hasRole("ADMIN")

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
