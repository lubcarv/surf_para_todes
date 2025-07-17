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
                                // Endpoints públicos
                                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/type/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/alunos/inativos").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/ativos").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/inativos").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/*/ativos").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/*/inativos").permitAll()

                                // 👇 ROTAS DO ClassroomController (GET públicos)
                                .requestMatchers(HttpMethod.GET, "/api/classrooms").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/classrooms/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/classrooms/*/students").permitAll()

                                // 👇 ROTAS DO ClassroomController (protegidas)
                                .requestMatchers(HttpMethod.POST, "/api/classrooms").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/classrooms/*/teachers/add").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/classrooms/*/teachers/remove").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/classrooms/*").authenticated()

                                // Proteção dos endpoints de usuário
                                .requestMatchers(HttpMethod.PUT, "/api/users/*").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/users/*/reactivate").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/users/*").authenticated()

                                // Swagger liberado
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                                // ROTAS ClassroomScheduleController

// GETs públicos
                                .requestMatchers(HttpMethod.GET, "/api/classroom-schedule").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/classroom-schedule/**").permitAll()

// Protegidas (POST, PUT, DELETE, PATCH)
                                .requestMatchers(HttpMethod.POST, "/api/classroom-schedule").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/classroom-schedule/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/classroom-schedule/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/classroom-schedule/**").authenticated()

                                // Endpoints públicos do ScheduleController
                                .requestMatchers(HttpMethod.GET, "/api/schedules").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/schedules/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/schedules/shifts").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/schedules/times").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/schedules/daysOfWeek").permitAll()

// Endpoints protegidos do ScheduleController
                                .requestMatchers(HttpMethod.POST, "/api/schedules").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/schedules/*").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/schedules/*").authenticated()


                                // UserScheduleController

// Rotas POST e DELETE protegidas (exigem autenticação)
                                .requestMatchers(HttpMethod.POST, "/api/user-schedule").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/user-schedule/batch").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/user-schedule/*/*").authenticated()

// Rotas GET públicas
                                .requestMatchers(HttpMethod.GET, "/api/user-schedule/schedule/*/students").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/user-schedule/student/*/schedules").permitAll()

                                // Qualquer outro requer autenticação
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
