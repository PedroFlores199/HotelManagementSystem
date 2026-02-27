package com.mallorca.Stay.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class ReservaRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long habitacionId;

    @NotNull
    private LocalDate fechaEntrada;

    @NotNull
    private LocalDate fechaSalida;

    @Positive
    private Integer numAdultos = 1;

    private Integer numNinos = 0;

    private String observaciones;

    // getters y setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }
    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
    public Integer getNumAdultos() { return numAdultos; }
    public void setNumAdultos(Integer numAdultos) { this.numAdultos = numAdultos; }
    public Integer getNumNinos() { return numNinos; }
    public void setNumNinos(Integer numNinos) { this.numNinos = numNinos; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

