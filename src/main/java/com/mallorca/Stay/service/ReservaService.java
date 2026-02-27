package com.mallorca.Stay.service;

import com.mallorca.Stay.dto.request.ReservaRequest;
import com.mallorca.Stay.dto.response.ReservaResponse;
import com.mallorca.Stay.domain.enums.EstadoReserva;

import java.util.List;

public interface ReservaService {

    ReservaResponse crearReserva(ReservaRequest request);

    ReservaResponse obtenerReserva(Long id);

    List<ReservaResponse> listarReservas();

    ReservaResponse actualizarReserva(Long id, ReservaRequest request);

    ReservaResponse cambiarEstado(Long id, EstadoReserva nuevoEstado);
}

