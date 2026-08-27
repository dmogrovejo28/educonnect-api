package org.dmencia.examentcs.repository;

import org.dmencia.examentcs.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    @Query("""
            SELECT i
            FROM Inscripcion i
            JOIN FETCH i.estudiante
            JOIN FETCH i.curso
            WHERE i.curso.id = :cursoId
            """)
    List<Inscripcion> findByCursoIdWithDetails(
            @Param("cursoId") Long cursoId
    );

    boolean existsByEstudianteIdAndCursoId(
            Long estudianteId,
            Long cursoId
    );

    @Modifying
    @Query("""
            DELETE
            FROM Inscripcion i
            WHERE i.estudiante.id = :estudianteId
            """)
    void deleteByEstudianteId(
            @Param("estudianteId") Long estudianteId
    );
}
