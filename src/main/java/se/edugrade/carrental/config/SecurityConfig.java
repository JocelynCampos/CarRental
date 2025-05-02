package se.edugrade.carrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


/*Erik Edman 24/04: Påbörjat konfigfilen för security. Nu kan man logga in på h2-console. Alla data hade laddats in korrekt till
* databasen.*/

/*05/01: SecurityConfig är färdig. TODO: ladda in Customers via databas istället för hårdkoda användare*/

@Configuration
public class SecurityConfig
{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()) //h2 behöver detta
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager()
    {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();

        UserDetails user1 = User.builder()
                .username("19850101-1234")
                .password("{noop}1234")
                .roles("USER")
                .build();

        UserDetails user2 = User.builder()
                .username("19900215-5678")
                .password("{noop}5678")
                .roles("USER")
                .build();

        UserDetails user3 = User.builder()
                .username("19751230-9101")
                .password("{noop}9101")
                .roles("USER")
                .build();

        UserDetails user4 = User.builder()
                .username("19881122-3456")
                .password("{noop}3456")
                .roles("USER")
                .build();

        UserDetails user5 = User.builder()
                .username("19950505-7890")
                .password("{noop}7890")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user1, user2, user3, user4, user5);
    }
}
