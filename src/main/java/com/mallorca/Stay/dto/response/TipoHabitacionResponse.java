package com.mallorca.Stay.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoHabitacionResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer capacidadMaxima;
    private BigDecimal precioBase;
    private String amenities;
}