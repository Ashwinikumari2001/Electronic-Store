package com.lcwd.electronic.store.ElectronicStore.config;

import com.lcwd.electronic.store.ElectronicStore.services.Impl.CustomeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
//    @Autowired
//    private CustomeUserService customUserService;
    //Securitu Filter Chain beans
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //configurations

        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        //urls
        //konse url public honge ya protected honge
        //konse url admin access karenge or konse users
        httpSecurity.authorizeHttpRequests(request->
                request.requestMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole("NORMAL","ADMIN")
                        .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                        .requestMatchers("/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                        .requestMatchers("/categories/**").hasRole("ADMIN")
                        .anyRequest().permitAll()



                );
        httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();



    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(customUserService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
