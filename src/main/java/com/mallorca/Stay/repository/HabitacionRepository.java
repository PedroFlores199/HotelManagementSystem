package com.mallorca.Stay.repository;

import com.mallorca.Stay.domain.entity.Habitacion;
import com.mallorca.Stay.domain.enums.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByActivoTrue();

    List<Habitacion> findByEstadoAndActivoTrue(EstadoHabitacion estado);

    List<Habitacion> findByPlantaAndActivoTrue(Integer planta);

    @Query("""
        SELECT h FROM Habitacion h
        WHERE h.activo = true
        AND h.tipoHabitacion.capacidadMaxima >= :personas
        AND h.id NOT IN (
            SELECT r.habitacion.id FROM Reserva r
            WHERE r.estado NOT IN ('CANCELADA', 'COMPLETADA')
            AND r.fechaEntrada < :fechaSalida
            AND r.fechaSalida > :fechaEntrada
        )
    """)
    List<Habitacion> findDisponibles(
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida,
            @Param("personas") Integer personas
    );
}