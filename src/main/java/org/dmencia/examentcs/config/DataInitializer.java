package org.dmencia.examentcs.config;

import lombok.RequiredArgsConstructor;
import org.dmencia.examentcs.model.Rol;
import org.dmencia.examentcs.model.Usuario;
import org.dmencia.examentcs.repository.RolRepository;
import org.dmencia.examentcs.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    @Bean
    CommandLineRunner initData(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            Rol estudiante =
                    rolRepository
                            .findByNombre(
                                    "ROLE_ESTUDIANTE"
                            )
                            .orElseGet(() ->
                                    rolRepository.save(
                                            new Rol(
                                                    null,
                                                    "ROLE_ESTUDIANTE"
                                            )
                                    )
                            );

            Rol admin =
                    rolRepository
                            .findByNombre(
                                    "ROLE_ADMIN"
                            )
                            .orElseGet(() ->
                                    rolRepository.save(
                                            new Rol(
                                                    null,
                                                    "ROLE_ADMIN"
                                            )
                                    )
                            );

            if (
                    !usuarioRepository
                            .existsByEmail(
                                    "admin@educonnect.com"
                            )
            ) {

                Usuario adminUsuario =
                        new Usuario();

                adminUsuario.setNombre(
                        "Administrador"
                );

                adminUsuario.setEmail(
                        "admin@educonnect.com"
                );

                adminUsuario.setPassword(
                        passwordEncoder.encode(
                                "Admin1234"
                        )
                );

                adminUsuario.setRoles(
                        Set.of(admin)
                );

                usuarioRepository.save(
                        adminUsuario
                );
            }
        };
    }
}
