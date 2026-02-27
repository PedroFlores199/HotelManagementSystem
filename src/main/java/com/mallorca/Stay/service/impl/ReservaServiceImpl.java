package com.mallorca.Stay.service.impl;

import com.mallorca.Stay.domain.entity.Cliente;
import com.mallorca.Stay.domain.entity.Habitacion;
import com.mallorca.Stay.domain.entity.Reserva;
import com.mallorca.Stay.domain.entity.TipoHabitacion;
import com.mallorca.Stay.domain.enums.EstadoReserva;
import com.mallorca.Stay.dto.request.ReservaRequest;
import com.mallorca.Stay.dto.response.ReservaResponse;
import com.mallorca.Stay.exception.EntidadNoEncontradaException;
import com.mallorca.Stay.exception.ReservaSolapadaException;
import com.mallorca.Stay.exception.TransicionEstadoInvalidaException;
import com.mallorca.Stay.repository.ClienteRepository;
import com.mallorca.Stay.repository.HabitacionRepository;
import com.mallorca.Stay.repository.ReservaRepository;
import com.mallorca.Stay.service.ReservaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final HabitacionRepository habitacionRepository;

    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              ClienteRepository clienteRepository,
                              HabitacionRepository habitacionRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Override
    public ReservaResponse crearReserva(ReservaRequest request) {
        // validar fechas
        if (request.getFechaEntrada() == null || request.getFechaSalida() == null ||
                !request.getFechaEntrada().isBefore(request.getFechaSalida())) {
            throw new IllegalArgumentException("Fechas inválidas: fechaEntrada debe ser anterior a fechaSalida");
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Cliente no encontrado"));

        Habitacion habitacion = habitacionRepository.findById(request.getHabitacionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Habitación no encontrada"));

        // comprobar solapamiento
        boolean conflicto = reservaRepository.existsConflictingReserva(habitacion.getId(), request.getFechaEntrada(), request.getFechaSalida());
        if (conflicto) {
            throw new ReservaSolapadaException("La habitación ya está reservada en el rango de fechas solicitado");
        }

        // comprobar capacidad
        TipoHabitacion tipo = habitacion.getTipoHabitacion();
        int capacidad = tipo.getCapacidadMaxima() != null ? tipo.getCapacidadMaxima() : Integer.MAX_VALUE;
        int personas = (request.getNumAdultos() == null ? 1 : request.getNumAdultos()) + (request.getNumNinos() == null ? 0 : request.getNumNinos());
        if (personas > capacidad) {
            throw new IllegalArgumentException("Número de personas excede la capacidad de la habitación");
        }

        Reserva reserva = Reserva.builder()
                .cliente(cliente)
                .habitacion(habitacion)
                .fechaEntrada(request.getFechaEntrada())
                .fechaSalida(request.getFechaSalida())
                .numAdultos(request.getNumAdultos())
                .numNinos(request.getNumNinos())
                .observaciones(request.getObservaciones())
                .estado(EstadoReserva.PENDIENTE)
                .build();

        // calcular precioTotal básico: precioBase * noches
        long noches = Duration.between(request.getFechaEntrada().atStartOfDay(), request.getFechaSalida().atStartOfDay()).toDays();
        BigDecimal precioBase = tipo.getPrecioBase() != null ? tipo.getPrecioBase() : BigDecimal.ZERO;
        BigDecimal subtotal = precioBase.multiply(new BigDecimal(noches));
        subtotal = subtotal.setScale(2, RoundingMode.HALF_EVEN);
        reserva.setPrecioTotal(subtotal);

        Reserva saved = reservaRepository.save(reserva);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerReserva(Long id) {
        Reserva r = reservaRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));
        return toResponse(r);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservas() {
        return reservaRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ReservaResponse actualizarReserva(Long id, ReservaRequest request) {
        Reserva r = reservaRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));
        if (r.getEstado() == EstadoReserva.CANCELADA || r.getEstado() == EstadoReserva.COMPLETADA) {
            throw new TransicionEstadoInvalidaException("No se puede modificar una reserva cancelada o completada");
        }

        // validar fechas si cambian
        if (request.getFechaEntrada() != null && request.getFechaSalida() != null) {
            if (!request.getFechaEntrada().isBefore(request.getFechaSalida())) {
                throw new IllegalArgumentException("Fechas inválidas");
            }
            boolean conflicto = reservaRepository.existsConflictingReserva(r.getHabitacion().getId(), request.getFechaEntrada(), request.getFechaSalida());
            if (conflicto) throw new ReservaSolapadaException("La habitación ya está reservada en el rango de fechas solicitado");
            r.setFechaEntrada(request.getFechaEntrada());
            r.setFechaSalida(request.getFechaSalida());

            // recalcular precio
            long noches = Duration.between(request.getFechaEntrada().atStartOfDay(), request.getFechaSalida().atStartOfDay()).toDays();
            BigDecimal precioBase = r.getHabitacion().getTipoHabitacion().getPrecioBase();
            BigDecimal subtotal = precioBase.multiply(new BigDecimal(noches)).setScale(2, RoundingMode.HALF_EVEN);
            r.setPrecioTotal(subtotal);
        }

        if (request.getNumAdultos() != null) r.setNumAdultos(request.getNumAdultos());
        if (request.getNumNinos() != null) r.setNumNinos(request.getNumNinos());
        if (request.getObservaciones() != null) r.setObservaciones(request.getObservaciones());

        Reserva saved = reservaRepository.save(r);
        return toResponse(saved);
    }

    @Override
    public ReservaResponse cambiarEstado(Long id, EstadoReserva nuevoEstado) {
        Reserva r = reservaRepository.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("Reserva no encontrada"));

        EstadoReserva actual = r.getEstado();
        // reglas simples de transicion
        if (actual == EstadoReserva.CANCELADA || actual == EstadoReserva.COMPLETADA) {
            throw new TransicionEstadoInvalidaException("No se permiten cambios desde el estado finalizado o cancelado");
        }
        if (actual == EstadoReserva.PENDIENTE && nuevoEstado == EstadoReserva.CONFIRMADA) {
            r.setEstado(nuevoEstado);
        } else if ((actual == EstadoReserva.PENDIENTE || actual == EstadoReserva.CONFIRMADA) && nuevoEstado == EstadoReserva.CANCELADA) {
            r.setEstado(EstadoReserva.CANCELADA);
        } else if (actual == EstadoReserva.CONFIRMADA && nuevoEstado == EstadoReserva.COMPLETADA) {
            r.setEstado(EstadoReserva.COMPLETADA);
        } else {
            throw new TransicionEstadoInvalidaException("Transición de estado inválida");
        }

        Reserva saved = reservaRepository.save(r);
        return toResponse(saved);
    }

    private ReservaResponse toResponse(Reserva r) {
        ReservaResponse resp = new ReservaResponse();
        resp.setId(r.getId());
        resp.setClienteId(r.getCliente() != null ? r.getCliente().getId() : null);
        resp.setHabitacionId(r.getHabitacion() != null ? r.getHabitacion().getId() : null);
        resp.setFechaEntrada(r.getFechaEntrada());
        resp.setFechaSalida(r.getFechaSalida());
        resp.setNumAdultos(r.getNumAdultos());
        resp.setNumNinos(r.getNumNinos());
        resp.setEstado(r.getEstado());
        resp.setPrecioTotal(r.getPrecioTotal());
        resp.setCreatedAt(r.getCreatedAt());
        return resp;
    }
}

