package com.mallorca.Stay.service.impl;

import com.mallorca.Stay.domain.entity.Habitacion;
import com.mallorca.Stay.domain.enums.EstadoHabitacion;
import com.mallorca.Stay.dto.request.HabitacionRequest;
import com.mallorca.Stay.dto.response.HabitacionResponse;
import com.mallorca.Stay.repository.HabitacionRepository;
import com.mallorca.Stay.repository.HotelRepository;
import com.mallorca.Stay.repository.TipoHabitacionRepository;
import com.mallorca.Stay.service.HabitacionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final HotelRepository hotelRepository;

    @Override
    public List<HabitacionResponse> findAll() {
        return habitacionRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public HabitacionResponse findById(Long id) {
        return habitacionRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Habitación no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public HabitacionResponse create(HabitacionRequest request) {
        var hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));
        var tipo = tipoHabitacionRepository.findById(request.getTipoHabitacionId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de habitación no encontrado"));

        var habitacion = Habitacion.builder()
                .numero(request.getNumero())
                .planta(request.getPlanta())
                .estado(request.getEstado())
                .descripcion(request.getDescripcion())
                .hotel(hotel)
                .tipoHabitacion(tipo)
                .activo(true)
                .build();

        return toResponse(habitacionRepository.save(habitacion));
    }

    @Override
    @Transactional
    public HabitacionResponse update(Long id, HabitacionRequest request) {
        var habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Habitación no encontrada con id: " + id));
        var tipo = tipoHabitacionRepository.findById(request.getTipoHabitacionId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de habitación no encontrado"));

        habitacion.setNumero(request.getNumero());
        habitacion.setPlanta(request.getPlanta());
        habitacion.setEstado(request.getEstado());
        habitacion.setDescripcion(request.getDescripcion());
        habitacion.setTipoHabitacion(tipo);

        return toResponse(habitacionRepository.save(habitacion));
    }

    @Override
    @Transactional
    public HabitacionResponse updateEstado(Long id, EstadoHabitacion estado) {
        var habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Habitación no encontrada con id: " + id));
        habitacion.setEstado(estado);
        return toResponse(habitacionRepository.save(habitacion));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Habitación no encontrada con id: " + id));
        habitacion.setActivo(false);
        habitacionRepository.save(habitacion);
    }

    @Override
    public List<HabitacionResponse> findByEstado(EstadoHabitacion estado) {
        return habitacionRepository.findByEstadoAndActivoTrue(estado)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<HabitacionResponse> findDisponibles(LocalDate fechaEntrada, LocalDate fechaSalida, Integer personas) {
        return habitacionRepository.findDisponibles(fechaEntrada, fechaSalida, personas)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private HabitacionResponse toResponse(Habitacion h) {
        return HabitacionResponse.builder()
                .id(h.getId())
                .numero(h.getNumero())
                .planta(h.getPlanta())
                .estado(h.getEstado())
                .descripcion(h.getDescripcion())
                .tipoHabitacion(h.getTipoHabitacion().getNombre())
                .capacidadMaxima(h.getTipoHabitacion().getCapacidadMaxima())
                .precioBase(h.getTipoHabitacion().getPrecioBase())
                .build();
    }
}