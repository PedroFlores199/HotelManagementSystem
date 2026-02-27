package com.mallorca.Stay.service;

import com.mallorca.Stay.domain.entity.Cliente;
import com.mallorca.Stay.domain.entity.Habitacion;
import com.mallorca.Stay.domain.entity.TipoHabitacion;
import com.mallorca.Stay.domain.enums.EstadoReserva;
import com.mallorca.Stay.dto.request.ReservaRequest;
import com.mallorca.Stay.dto.response.ReservaResponse;
import com.mallorca.Stay.exception.ReservaSolapadaException;
import com.mallorca.Stay.repository.ClienteRepository;
import com.mallorca.Stay.repository.HabitacionRepository;
import com.mallorca.Stay.repository.ReservaRepository;
import com.mallorca.Stay.service.impl.ReservaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    void crearReserva_success() {
        Cliente c = Cliente.builder().id(1L).nombre("Juan").apellidos("Perez").documento("123").build();
        TipoHabitacion t = TipoHabitacion.builder().id(1L).nombre("Doble").capacidadMaxima(2).precioBase(new BigDecimal("50.00")).build();
        Habitacion h = Habitacion.builder().id(1L).numero("101").planta(1).tipoHabitacion(t).build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(c));
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(h));
        when(reservaRepository.existsConflictingReserva(any(), any(), any())).thenReturn(false);
        when(reservaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ReservaRequest req = new ReservaRequest();
        req.setClienteId(1L);
        req.setHabitacionId(1L);
        req.setFechaEntrada(LocalDate.now().plusDays(1));
        req.setFechaSalida(LocalDate.now().plusDays(3));
        req.setNumAdultos(2);

        ReservaResponse resp = reservaService.crearReserva(req);
        assertNotNull(resp);
        assertEquals(EstadoReserva.PENDIENTE, resp.getEstado());
        assertEquals(new BigDecimal("100.00"), resp.getPrecioTotal());
    }

    @Test
    void crearReserva_conflict_throws() {
        Cliente c = Cliente.builder().id(1L).nombre("Juan").apellidos("Perez").documento("123").build();
        TipoHabitacion t = TipoHabitacion.builder().id(1L).nombre("Doble").capacidadMaxima(2).precioBase(new BigDecimal("50.00")).build();
        Habitacion h = Habitacion.builder().id(1L).numero("101").planta(1).tipoHabitacion(t).build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(c));
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(h));
        when(reservaRepository.existsConflictingReserva(any(), any(), any())).thenReturn(true);

        ReservaRequest req = new ReservaRequest();
        req.setClienteId(1L);
        req.setHabitacionId(1L);
        req.setFechaEntrada(LocalDate.now().plusDays(1));
        req.setFechaSalida(LocalDate.now().plusDays(3));
        req.setNumAdultos(2);

        assertThrows(ReservaSolapadaException.class, () -> reservaService.crearReserva(req));
    }
}
