package org.dmencia.examentcs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dmencia.examentcs.dto.request.InscripcionRequestDTO;
import org.dmencia.examentcs.dto.response.InscripcionResponseDTO;
import org.dmencia.examentcs.service.InscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<InscripcionResponseDTO> matricular(
            Authentication authentication,

            @Valid
            @RequestBody
            InscripcionRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        inscripcionService.matricular(
                                authentication.getName(),
                                request
                        )
                );
    }

    @GetMapping("/curso/{id}")
    public ResponseEntity<List<InscripcionResponseDTO>>
    listarPorCurso(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                inscripcionService.listarPorCurso(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(
            @PathVariable Long id
    ) {

        inscripcionService.cancelar(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
