package se.edugrade.carrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


/*Erik Edman 24/04: Påbörjat konfigfilen för security. Nu kan man logga in på h2-console. Alla data hade laddats in korrekt till
* databasen.*/

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")  // allow POSTs to h2-console
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()) // allow frames (H2 console needs it)
                );

        return http.build();
    }
}
