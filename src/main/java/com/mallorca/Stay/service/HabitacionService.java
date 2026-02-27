package com.mallorca.Stay.service;

import com.mallorca.Stay.dto.request.HabitacionRequest;
import com.mallorca.Stay.dto.response.HabitacionResponse;
import com.mallorca.Stay.domain.enums.EstadoHabitacion;
import java.time.LocalDate;
import java.util.List;

public interface HabitacionService {
    List<HabitacionResponse> findAll();
    HabitacionResponse findById(Long id);
    HabitacionResponse create(HabitacionRequest request);
    HabitacionResponse update(Long id, HabitacionRequest request);
    HabitacionResponse updateEstado(Long id, EstadoHabitacion estado);
    void delete(Long id);
    List<HabitacionResponse> findByEstado(EstadoHabitacion estado);
    List<HabitacionResponse> findDisponibles(LocalDate fechaEntrada, LocalDate fechaSalida, Integer personas);
}