package org.dmencia.examentcs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dmencia.examentcs.model.util.EstadoInscripcion;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "inscripciones",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_inscripcion_estudiante_curso",
                columnNames = {
                        "estudiante_id",
                        "curso_id"
                }
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "estudiante_id",
            nullable = false
    )
    private Usuario estudiante;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "curso_id",
            nullable = false
    )
    private Curso curso;

    @Column(nullable = false)
    private LocalDateTime fechaInscripcion;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private EstadoInscripcion estado = EstadoInscripcion.ACTIVO;

}
