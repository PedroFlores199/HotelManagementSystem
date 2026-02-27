package com.mallorca.Stay.controller;

import com.mallorca.Stay.domain.enums.EstadoHabitacion;
import com.mallorca.Stay.dto.request.HabitacionRequest;
import com.mallorca.Stay.dto.response.HabitacionResponse;
import com.mallorca.Stay.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionResponse>> findAll() {
        return ResponseEntity.ok(habitacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.findById(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<HabitacionResponse>> findDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida,
            @RequestParam(defaultValue = "1") Integer personas) {
        return ResponseEntity.ok(habitacionService.findDisponibles(fechaEntrada, fechaSalida, personas));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<HabitacionResponse>> findByEstado(@PathVariable EstadoHabitacion estado) {
        return ResponseEntity.ok(habitacionService.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<HabitacionResponse> create(@Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitacionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.ok(habitacionService.update(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<HabitacionResponse> updateEstado(
            @PathVariable Long id,
            @RequestParam EstadoHabitacion estado) {
        return ResponseEntity.ok(habitacionService.updateEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        habitacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}