package com.mallorca.Stay.controller;

import com.mallorca.Stay.dto.request.ReservaRequest;
import com.mallorca.Stay.dto.response.ReservaResponse;
import com.mallorca.Stay.domain.enums.EstadoReserva;
import com.mallorca.Stay.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse resp = reservaService.crearReserva(request);
        return ResponseEntity.created(URI.create("/api/reservas/" + resp.getId())).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerReserva(id));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.actualizarReserva(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReservaResponse> cambiarEstado(@PathVariable Long id, @RequestBody EstadoPayload payload) {
        ReservaResponse resp = reservaService.cambiarEstado(id, payload.getEstado());
        return ResponseEntity.ok(resp);
    }

    public static class EstadoPayload {
        private EstadoReserva estado;
        public EstadoReserva getEstado() { return estado; }
        public void setEstado(EstadoReserva estado) { this.estado = estado; }
    }
}
