package org.dmencia.examentcs.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dmencia.examentcs.dto.request.CursoRequestDTO;
import org.dmencia.examentcs.dto.response.CursoResponseDTO;
import org.dmencia.examentcs.exception.DuplicateResourceException;
import org.dmencia.examentcs.exception.ResourceNotFoundException;
import org.dmencia.examentcs.model.Curso;
import org.dmencia.examentcs.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoService {

    private final CursoRepository cursoRepository;

    @Transactional
    public CursoResponseDTO crearCurso(
            CursoRequestDTO request
    ) {

        String codigo =
                request.codigo()
                        .trim()
                        .toUpperCase();

        if (cursoRepository.existsByCodigo(codigo)) {

            throw new DuplicateResourceException(
                    "Ya existe un curso con el código: "
                            + codigo
            );
        }

        Curso curso = new Curso();

        curso.setCodigo(codigo);

        curso.setTitulo(
                request.titulo().trim()
        );

        curso.setDescripcion(
                request.descripcion()
        );

        Curso guardado =
                cursoRepository.save(curso);

        log.info(
                "Curso creado. id={}, codigo={}",
                guardado.getId(),
                guardado.getCodigo()
        );

        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDTO> listarCursos() {

        return cursoRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Curso buscarPorId(Long id) {

        return cursoRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Curso no encontrado con id: "
                                        + id
                        )
                );
    }

    private CursoResponseDTO toResponse(
            Curso curso
    ) {

        return new CursoResponseDTO(
                curso.getId(),
                curso.getCodigo(),
                curso.getTitulo(),
                curso.getDescripcion()
        );
    }

}
