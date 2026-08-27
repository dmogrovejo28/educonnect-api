package org.dmencia.examentcs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dmencia.examentcs.dto.request.EstudianteRequestDTO;
import org.dmencia.examentcs.dto.response.EstudianteResponseDTO;
import org.dmencia.examentcs.exception.DuplicateResourceException;
import org.dmencia.examentcs.exception.ResourceNotFoundException;
import org.dmencia.examentcs.model.Rol;
import org.dmencia.examentcs.model.Usuario;
import org.dmencia.examentcs.repository.InscripcionRepository;
import org.dmencia.examentcs.repository.RolRepository;
import org.dmencia.examentcs.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {
    private static final String ROLE_ESTUDIANTE =
            "ROLE_ESTUDIANTE";

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final InscripcionRepository inscripcionRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public EstudianteResponseDTO registrarEstudiante(
            EstudianteRequestDTO request
    ) {

        String email =
                request.email()
                        .trim()
                        .toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {

            throw new DuplicateResourceException(
                    "Ya existe un usuario con el email: "
                            + email
            );
        }

        Rol rolEstudiante =
                rolRepository
                        .findByNombre(ROLE_ESTUDIANTE)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "No existe el rol "
                                                + ROLE_ESTUDIANTE
                                )
                        );

        Usuario usuario = new Usuario();

        usuario.setNombre(
                request.nombre().trim()
        );

        usuario.setEmail(email);

        usuario.setPassword(
                passwordEncoder.encode(
                        request.password()
                )
        );

        usuario.getRoles()
                .add(rolEstudiante);

        Usuario guardado =
                usuarioRepository.save(usuario);

        log.info(
                "Estudiante registrado correctamente. id={}, email={}",
                guardado.getId(),
                guardado.getEmail()
        );

        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> listarEstudiantes() {

        return usuarioRepository
                .findAll()
                .stream()
                .filter(this::esEstudiante)
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void eliminarEstudiante(Long id) {

        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estudiante no encontrado con id: "
                                                + id
                                )
                        );

        if (!esEstudiante(usuario)) {

            throw new ResourceNotFoundException(
                    "Estudiante no encontrado con id: "
                            + id
            );
        }

        inscripcionRepository
                .deleteByEstudianteId(id);

        usuarioRepository.delete(usuario);

        log.info(
                "Estudiante eliminado. id={}",
                id
        );
    }

    private boolean esEstudiante(
            Usuario usuario
    ) {

        return usuario
                .getRoles()
                .stream()
                .anyMatch(rol ->
                        ROLE_ESTUDIANTE
                                .equals(rol.getNombre())
                );
    }

    private EstudianteResponseDTO toResponse(
            Usuario usuario
    ) {

        return new EstudianteResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRoles()
                        .stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toSet())
        );
    }
}
