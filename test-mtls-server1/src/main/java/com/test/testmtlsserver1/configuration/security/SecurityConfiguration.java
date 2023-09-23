package com.test.testmtlsserver1.configuration.security;


import com.test.testmtlsserver1.configuration.security.auth.properties.OauthProvidersProperty;
import com.test.testmtlsserver1.configuration.security.auth.provider.builder.JwtProviderStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    private final OauthProvidersProperty providersProperty;
    private final JwtProviderStrategy jwtProviderStrategy;

    public SecurityConfiguration(JwtProviderStrategy jwtProviderStrategy, OauthProvidersProperty providersProperty) {
        this.providersProperty = providersProperty;
        this.jwtProviderStrategy = jwtProviderStrategy;
    }


    @Bean
    @Order(1)
    public SecurityFilterChain mtlsSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/testMtls/**") //PREVENT COOKIES AND SESSION IDS FROM INTERVEENING IN THIS!!!
            .authorizeRequests(authorizeRequests ->
                    authorizeRequests
                            .antMatchers(HttpMethod.GET,"/testMtls/protected").authenticated()
                            .antMatchers(HttpMethod.GET,"/testMtls/notProtected").permitAll()
            )
            .x509()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER);

    //.userDetailsService(userDetailsService());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if (username.equals("localhost")) {
                    return new User(username, "",
                            AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
                }
                throw new UsernameNotFoundException("User not found!");
            }
        };
    }

    @Bean
    public SecurityFilterChain oauthFilterChain(HttpSecurity http) throws Exception {
        http
            .antMatcher("/testOauth/**") //PREVENT COOKIES AND SESSION IDS FROM INTERVEENING IN THIS!!!
            .authorizeRequests(authorize ->{
                    authorize
                            .antMatchers(HttpMethod.GET,"/testOauth/protected").authenticated()
                            .mvcMatchers(HttpMethod.GET,"/testOauth/notProtected").permitAll()// Secure these endpoints with OAuth2
                            .anyRequest().authenticated();
            })
            .oauth2ResourceServer(
                    oauth2 -> oauth2.authenticationManagerResolver(getJwtIssuerAuthenticationManagerResolver()));

        http.headers()
                .xssProtection()
                .disable()
                .frameOptions()
                .deny()
                .contentSecurityPolicy("default-src 'self'");

        return http.build();
    }


    @Bean("CurrentUser")
    public Supplier<JwtAuthenticationToken> currentUser() {
        return () -> (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    private JwtIssuerAuthenticationManagerResolver getJwtIssuerAuthenticationManagerResolver() {
        Map<String, AuthenticationManager> managers = providersProperty.getProviders()
                .values()
                .stream()
                .collect(Collectors.toMap(p -> p.getIssuerUri(),
                        p -> jwtProviderStrategy.getProvider(p.getKey())::authenticate));

        return new JwtIssuerAuthenticationManagerResolver(managers::get);
    }

}

//                                .mvcMatchers(HttpMethod.GET, "/test**").permitAll()
