package org.dmencia.examentcs.config;

import lombok.RequiredArgsConstructor;
import org.dmencia.examentcs.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UsuarioRepository usuarioRepository;

    @Bean
    public UserDetailsService userDetailsService() {

        return username ->

                usuarioRepository
                        .findByEmail(
                                username
                                        .trim()
                                        .toLowerCase()
                        )
                        .map(usuario -> {

                            var authorities =
                                    usuario
                                            .getRoles()
                                            .stream()
                                            .map(rol ->
                                                    new SimpleGrantedAuthority(
                                                            rol.getNombre()
                                                    )
                                            )
                                            .toList();

                            return User
                                    .withUsername(
                                            usuario.getEmail()
                                    )
                                    .password(
                                            usuario.getPassword()
                                    )
                                    .authorities(
                                            authorities
                                    )
                                    .build();
                        })
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuario no encontrado: "
                                                + username
                                )
                        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(
                        csrf ->
                                csrf.disable()
                )

                .authorizeHttpRequests(
                        auth -> auth

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/estudiantes"
                                )
                                .permitAll()

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/estudiantes"
                                )
                                .hasRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/estudiantes/**"
                                )
                                .hasRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/cursos"
                                )
                                .hasRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/cursos"
                                )
                                .hasAnyRole(
                                        "ESTUDIANTE",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/inscripciones"
                                )
                                .hasAnyRole(
                                        "ESTUDIANTE",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/inscripciones/curso/**"
                                )
                                .hasAnyRole(
                                        "ESTUDIANTE",
                                        "ADMIN"
                                )

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/inscripciones/**"
                                )
                                .hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                )

                .httpBasic(
                        Customizer.withDefaults()
                );

        return http.build();
    }
}
