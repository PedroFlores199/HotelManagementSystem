package com.mallorca.Stay.dto.response;

import com.mallorca.Stay.domain.enums.EstadoHabitacion;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionResponse {
    private Long id;
    private String numero;
    private Integer planta;
    private EstadoHabitacion estado;
    private String descripcion;
    private String tipoHabitacion;
    private Integer capacidadMaxima;
    private BigDecimal precioBase;
}