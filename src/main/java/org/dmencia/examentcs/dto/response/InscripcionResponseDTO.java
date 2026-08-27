package org.dmencia.examentcs.dto.response;

import org.dmencia.examentcs.model.util.EstadoInscripcion;

import java.time.LocalDateTime;

public record InscripcionResponseDTO(
        Long id,

        Long estudianteId,

        String estudianteNombre,

        Long cursoId,

        String cursoCodigo,

        String cursoTitulo,

        LocalDateTime fechaInscripcion,

        EstadoInscripcion estado
) {

}
