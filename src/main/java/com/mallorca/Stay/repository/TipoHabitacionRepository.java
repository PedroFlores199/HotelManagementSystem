package com.mallorca.Stay.repository;

import com.mallorca.Stay.domain.entity.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {
    List<TipoHabitacion> findByActivoTrue();
}