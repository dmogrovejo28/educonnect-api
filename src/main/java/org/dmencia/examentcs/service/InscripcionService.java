package org.dmencia.examentcs.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dmencia.examentcs.dto.request.InscripcionRequestDTO;
import org.dmencia.examentcs.dto.response.InscripcionResponseDTO;
import org.dmencia.examentcs.exception.DuplicateResourceException;
import org.dmencia.examentcs.exception.ResourceNotFoundException;
import org.dmencia.examentcs.model.Curso;
import org.dmencia.examentcs.model.Inscripcion;
import org.dmencia.examentcs.model.Usuario;
import org.dmencia.examentcs.model.util.EstadoInscripcion;
import org.dmencia.examentcs.repository.InscripcionRepository;
import org.dmencia.examentcs.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InscripcionService {

    private static final String ROLE_ADMIN =
            "ROLE_ADMIN";

    private static final String ROLE_ESTUDIANTE =
            "ROLE_ESTUDIANTE";

    private final InscripcionRepository inscripcionRepository;

    private final UsuarioRepository usuarioRepository;

    private final CursoService cursoService;

    @Transactional
    public InscripcionResponseDTO matricular(
            String emailAutenticado,
            InscripcionRequestDTO request
    ) {

        Usuario autenticado =
                usuarioRepository
                        .findByEmail(emailAutenticado)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuario autenticado no encontrado"
                                )
                        );

        Usuario estudiante;

        if (
                esAdmin(autenticado)
                        && request.estudianteId() != null
        ) {

            estudiante =
                    usuarioRepository
                            .findById(
                                    request.estudianteId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Estudiante no encontrado con id: "
                                                    + request.estudianteId()
                                    )
                            );

        } else {

            estudiante = autenticado;
        }

        if (!esEstudiante(estudiante)) {

            throw new IllegalArgumentException(
                    "El usuario seleccionado no tiene rol de estudiante"
            );
        }

        Curso curso =
                cursoService.buscarPorId(
                        request.cursoId()
                );

        if (
                inscripcionRepository
                        .existsByEstudianteIdAndCursoId(
                                estudiante.getId(),
                                curso.getId()
                        )
        ) {

            throw new DuplicateResourceException(
                    "El estudiante ya está matriculado en el curso"
            );
        }

        Inscripcion inscripcion =
                new Inscripcion();

        inscripcion.setEstudiante(
                estudiante
        );

        inscripcion.setCurso(curso);

        inscripcion.setFechaInscripcion(
                LocalDateTime.now()
        );

        inscripcion.setEstado(
                EstadoInscripcion.ACTIVO
        );

        Inscripcion guardada =
                inscripcionRepository
                        .save(inscripcion);

        log.info(
                "Matrícula creada. id={}, estudianteId={}, cursoId={}",
                guardada.getId(),
                estudiante.getId(),
                curso.getId()
        );

        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<InscripcionResponseDTO> listarPorCurso(
            Long cursoId
    ) {

        cursoService.buscarPorId(cursoId);

        return inscripcionRepository
                .findByCursoIdWithDetails(cursoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void cancelar(Long id) {

        Inscripcion inscripcion =
                inscripcionRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inscripción no encontrada con id: "
                                                + id
                                )
                        );

        inscripcion.setEstado(
                EstadoInscripcion.CANCELADO
        );

        log.info(
                "Inscripción cancelada. id={}",
                id
        );
    }

    private boolean esAdmin(
            Usuario usuario
    ) {

        return usuario
                .getRoles()
                .stream()
                .anyMatch(rol ->
                        ROLE_ADMIN.equals(
                                rol.getNombre()
                        )
                );
    }

    private boolean esEstudiante(
            Usuario usuario
    ) {

        return usuario
                .getRoles()
                .stream()
                .anyMatch(rol ->
                        ROLE_ESTUDIANTE.equals(
                                rol.getNombre()
                        )
                );
    }

    private InscripcionResponseDTO toResponse(
            Inscripcion inscripcion
    ) {

        return new InscripcionResponseDTO(
                inscripcion.getId(),

                inscripcion
                        .getEstudiante()
                        .getId(),

                inscripcion
                        .getEstudiante()
                        .getNombre(),

                inscripcion
                        .getCurso()
                        .getId(),

                inscripcion
                        .getCurso()
                        .getCodigo(),

                inscripcion
                        .getCurso()
                        .getTitulo(),

                inscripcion
                        .getFechaInscripcion(),

                inscripcion
                        .getEstado()
        );
    }
}
