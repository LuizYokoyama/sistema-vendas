package com.ls.sistemavendas.config;

import com.ls.sistemavendas.service.EventAgentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(2)
public class AgentSecurityConfiguration {

    @Bean
    public PasswordEncoder noPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService agentDetailsService() {
        return new EventAgentService();
    }

    @Bean
    public DaoAuthenticationProvider agentAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(agentDetailsService());
        authProvider.setPasswordEncoder(noPasswordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChainAgent(HttpSecurity http) throws Exception {
       /* http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        */
        http.authenticationProvider(agentAuthenticationProvider());
        http
                .httpBasic()
                .and()
                .antMatcher("/api/participant/**")
                .authorizeRequests().anyRequest().authenticated()

                .and()
                .csrf().disable();
                /*.csrf().requireCsrfProtectionMatcher( //Enable Swagger use, but enable csrf in the GETs too
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
    public WebSecurityCustomizer webSecurityCustomizer1() {
        return (web) -> web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**")
                .and()
                // ignore swagger
                .ignoring().mvcMatchers("/swagger-ui/**", "/configuration/**",
                        "/swagger-resources/**", "/v2/api-docs");
    }


}
