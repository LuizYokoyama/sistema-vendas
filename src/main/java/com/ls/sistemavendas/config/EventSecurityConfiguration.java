package com.ls.sistemavendas.config;

import com.ls.sistemavendas.service.FormService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class EventSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public UserDetailsService formDetailsService() {
        return new FormService();
    }

    @Bean
    public DaoAuthenticationProvider eventAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(formDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    public SecurityFilterChain filterChainEvent(HttpSecurity http) throws Exception {
       /* http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        */
        //http.authenticationProvider(eventAuthenticationProvider());
        http
                .httpBasic()
                .and()
                .authenticationProvider(eventAuthenticationProvider())
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST,"/api/event" ).permitAll()
                .mvcMatchers(HttpMethod.GET,"/**" ).authenticated()
                .mvcMatchers(HttpMethod.POST,"/**" ).authenticated()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
               /* .csrf().requireCsrfProtectionMatcher( //Enable Swagger use, but enable csrf in the GETs too
                        new NegatedRequestMatcher(new OrRequestMatcher(
                                new RequestHeaderRequestMatcher("Referer", "http://localhost:8080/swagger-ui/index.html"),
                                new AntPathRequestMatcher("/login"), // GET only path
                                new AntPathRequestMatcher("/index.html")
                        ))
                );*/

        return http.build();
    }

    /*   //In-Memory Authentication
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("$2a$04$H3LwvZtcgmySVhxRDIWUB.4t6og4Zgah43DxB/Y4H5hLEqVjLP57m")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
*/


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**")
                .and()
                // ignore swagger
                .ignoring().mvcMatchers("/swagger-ui/**", "/configuration/**",
                        "/swagger-resources/**", "/v2/api-docs");
    }


}
