package org.dmencia.examentcs.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "cursos",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_curso_codigo",
                columnNames = "codigo"
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 30
    )
    private String codigo;

    @Column(
            nullable = false,
            length = 150
    )
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Curso curso)) {
            return false;
        }

        return codigo != null
                && codigo.equals(curso.codigo);
    }

    @Override
    public int hashCode() {

        return codigo != null
                ? codigo.hashCode()
                : 0;
    }
}
