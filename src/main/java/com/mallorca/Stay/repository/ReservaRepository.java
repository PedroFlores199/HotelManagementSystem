// Creación de ReservaRepository con consultas para solapamiento
package com.mallorca.Stay.repository;

import com.mallorca.Stay.domain.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reserva r " +
            "WHERE r.habitacion.id = :habitacionId " +
            "AND r.estado NOT IN ('CANCELADA', 'COMPLETADA') " +
            "AND r.fechaEntrada < :fechaFin " +
            "AND r.fechaSalida > :fechaInicio")
    boolean existsConflictingReserva(@Param("habitacionId") Long habitacionId,
                                     @Param("fechaInicio") LocalDate fechaInicio,
                                     @Param("fechaFin") LocalDate fechaFin);
}

