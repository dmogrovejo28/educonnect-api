package org.dmencia.examentcs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CursoRequestDTO(
        @NotBlank(
                message = "El código es obligatorio"
        )
        @Size(
                max = 30,
                message = "El código no puede superar los 30 caracteres"
        )
        String codigo,

        @NotBlank(
                message = "El título es obligatorio"
        )
        @Size(
                max = 150,
                message = "El título no puede superar los 150 caracteres"
        )
        String titulo,

        @Size(
                max = 500,
                message = "La descripción no puede superar los 500 caracteres"
        )
        String descripcion
) {
}
