package org.dmencia.examentcs.dto.response;

import java.util.Set;

public record EstudianteResponseDTO(

        Long id,

        String nombre,

        String email,

        Set<String> roles
) {
}
