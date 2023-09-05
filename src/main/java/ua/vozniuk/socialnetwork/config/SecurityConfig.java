package ua.vozniuk.socialnetwork.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import ua.vozniuk.socialnetwork.services.PersonDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    PersonDetailsService personDetailsService;


    @Bean
    public DefaultSecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests.requestMatchers("/login",
                                        "/registration", "/css/**", "/static/**",
                                        "/user/**", "/addLike/**", "/friends/**", "/followers/**", "/followings/**", "/deleteFollowing/**",
                                        "followerAccept/**", "/user/friend-request/**", "/deletePost/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginPage("/login")
                                .defaultSuccessUrl("/home", true)
                                .failureUrl("/login?error")
                                .loginProcessingUrl("/process_login"))
                .logout((logout) ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login"))
                .csrf(csrf ->
                        csrf.disable())
        ;
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationManager) throws Exception {
        return authenticationManager.getAuthenticationManager();
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(personDetailsService);
//        provider.setPasswordEncoder(getPasswordEncoder());
//        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
