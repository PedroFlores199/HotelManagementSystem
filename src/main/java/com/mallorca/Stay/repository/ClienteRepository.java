package com.mallorca.Stay.repository;

import com.mallorca.Stay.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByActivoTrue();

    Optional<Cliente> findByDocumento(String documento);

    Optional<Cliente> findByEmail(String email);

    @Query("""
        SELECT c FROM Cliente c
        WHERE c.activo = true
        AND (
            LOWER(c.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
            OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :busqueda, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :busqueda, '%'))
            OR LOWER(c.documento) LIKE LOWER(CONCAT('%', :busqueda, '%'))
        )
    """)
    List<Cliente> buscar(@Param("busqueda") String busqueda);

    List<Cliente> findByClienteFrecuenteTrue();
}