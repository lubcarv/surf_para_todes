package dev.surfparatodes.surfparatodes.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ROTAS DE AUTENTICAÇÃO
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // ROTAS DE USUÁRIOS
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/type/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/alunos/inativos").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/").hasRole("ADMIN")

                        // ROTAS DE HORÁRIOS
                        .requestMatchers("/api/schedules/").permitAll()

                        // ROTAS DE TURMAS
                        .requestMatchers("/api/classrooms/").permitAll()

                        // ROTAS DE AULAS (CLASSROOM-SCHEDULE)
                        .requestMatchers("/api/classroom-schedule/").permitAll()

                        // ROTAS DE INSCRIÇÕES EM HORÁRIOS
                        .requestMatchers("/api/user-schedule/").permitAll()

                        // SWAGGER / DOCS
                        .requestMatchers("/swagger-ui/", "/v3/api-docs/").permitAll()

                        // QUALQUER OUTRO
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ⚠️ SHA256 NÃO É O PADRÃO PARA SENHAS (pense em migrar para BCrypt futuramente)
        return new SHA256PasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Aceita qualquer porta de localhost / 127.0.0.1
        config.setAllowedOriginPatterns(
                List.of("http://localhost:*", "http://127.0.0.1:*"));

        // Métodos e cabeçalhos que o front realmente usa
        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "Accept"));
        // Se usa cookies/JWT via header Authorization, mantenha:
        config.setAllowCredentials(true);

        // Se o front precisar ler algum header de resposta, exponha aqui
        // config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);   // <- cobre TODAS as rotas
        return source;
    }
}
