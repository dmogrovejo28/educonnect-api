package org.dmencia.examentcs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dmencia.examentcs.dto.request.EstudianteRequestDTO;
import org.dmencia.examentcs.dto.response.EstudianteResponseDTO;
import org.dmencia.examentcs.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> registrar(
            @Valid
            @RequestBody
            EstudianteRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        usuarioService
                                .registrarEstudiante(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> listar() {

        return ResponseEntity.ok(
                usuarioService.listarEstudiantes()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {

        usuarioService.eliminarEstudiante(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
