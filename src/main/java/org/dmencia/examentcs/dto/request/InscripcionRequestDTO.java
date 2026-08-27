package org.dmencia.examentcs.dto.request;

import jakarta.validation.constraints.NotNull;

public record InscripcionRequestDTO(

        @NotNull(
                message = "El curso es obligatorio"
        )
        Long cursoId,

        Long estudianteId
) {
}
