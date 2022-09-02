package com.ls.sistemavendas.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${keycloak.auth-server-url}")
    private String AUTH_SERVER;

    @Value("${keycloak.credentials.secret}")
    private String CLIENT_SECRET;

    @Value("${keycloak.resource}")
    private String CLIENT_ID;

    @Value("${keycloak.realm}")
    private String REALM;



    private static final String OAUTH_NAME = "spring_oauth";

    private static final String TOKEN_NAME = "oauthtoken";
    private static final String ALLOWED_PATHS = "/api.*";
    private static final String GROUP_NAME = "grupo1";

    @Bean
    public Docket taskApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME)
                .useDefaultResponseMessages(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ls.sistemavendas"))
                .paths(regex(ALLOWED_PATHS))
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()))
                .apiInfo(metaInfo());
    }


    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .realm(REALM)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .appName(GROUP_NAME)
                .scopeSeparator(" ")
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType =
                new AuthorizationCodeGrantBuilder()
                        .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/realms/" + REALM + "/protocol/openid-connect/token", TOKEN_NAME))
                        .tokenRequestEndpoint(
                                new TokenRequestEndpoint(AUTH_SERVER + "/realms/" + REALM + "/protocol/openid-connect/auth", CLIENT_ID, CLIENT_SECRET))
                        .build();

        SecurityScheme oauth =
                new OAuthBuilder()
                        .name(OAUTH_NAME)
                        .grantTypes(Arrays.asList(grantType))
                        .scopes(Arrays.asList(scopes()))
                        .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("user", "for CRUD operations"),
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations")
        };
        return scopes;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference(OAUTH_NAME, scopes())))
                .forPaths(PathSelectors.regex(ALLOWED_PATHS))
                .build();
    }



    private ApiInfo metaInfo(){
        ApiInfo apiInfo = new ApiInfo(
                "Events API REST",
                "API REST for sales",
                "0.1",
                "Terms of Service",
                new Contact("Luiz Yokoyama",
                        "https://github.com/LuizYokoyama",
                        "luizfelix@egresso.ufg.br"),
                "Apache Licence Version 2.0",
                "https://www.apache.org/licesen.html",
                new ArrayList<VendorExtension>()
        );
        return apiInfo;
    }

    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> copy = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(copy);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }






}
