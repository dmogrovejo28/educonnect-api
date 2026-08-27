package org.dmencia.examentcs.repository;

import org.dmencia.examentcs.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByCodigo(String codigo);

}
