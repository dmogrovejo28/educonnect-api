package org.dmencia.examentcs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dmencia.examentcs.dto.request.CursoRequestDTO;
import org.dmencia.examentcs.dto.response.CursoResponseDTO;
import org.dmencia.examentcs.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    public ResponseEntity<CursoResponseDTO> crear(
            @Valid
            @RequestBody
            CursoRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        cursoService.crearCurso(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> listar() {

        return ResponseEntity.ok(
                cursoService.listarCursos()
        );
    }
}
