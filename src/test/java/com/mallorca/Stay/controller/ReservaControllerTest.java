package com.mallorca.Stay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallorca.Stay.dto.request.ReservaRequest;
import com.mallorca.Stay.dto.response.ReservaResponse;
import com.mallorca.Stay.domain.enums.EstadoReserva;
import com.mallorca.Stay.exception.ReservaSolapadaException;
import com.mallorca.Stay.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservaControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ReservaService reservaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReservaController controller = new ReservaController(reservaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.mallorca.Stay.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void postCrearReserva_returns201() throws Exception {
        ReservaResponse resp = new ReservaResponse();
        resp.setId(10L);
        resp.setEstado(EstadoReserva.PENDIENTE);
        resp.setPrecioTotal(new BigDecimal("100.00"));

        when(reservaService.crearReserva(any())).thenReturn(resp);

        ReservaRequest req = new ReservaRequest();
        req.setClienteId(1L);
        req.setHabitacionId(1L);
        req.setFechaEntrada(LocalDate.now().plusDays(1));
        req.setFechaSalida(LocalDate.now().plusDays(3));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/reservas/10"));
    }

    @Test
    void postCrearReserva_conflict_returns409() throws Exception {
        when(reservaService.crearReserva(any())).thenThrow(new ReservaSolapadaException("conflicto"));

        ReservaRequest req = new ReservaRequest();
        req.setClienteId(1L);
        req.setHabitacionId(1L);
        req.setFechaEntrada(LocalDate.now().plusDays(1));
        req.setFechaSalida(LocalDate.now().plusDays(3));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }
}
