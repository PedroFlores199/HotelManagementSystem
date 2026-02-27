package com.mallorca.Stay.dto.request;

import com.mallorca.Stay.domain.enums.EstadoHabitacion;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionRequest {

    @NotBlank(message = "El número de habitación es obligatorio")
    @Size(max = 10)
    private String numero;

    @NotNull(message = "La planta es obligatoria")
    @Min(value = 0)
    private Integer planta;

    private EstadoHabitacion estado = EstadoHabitacion.DISPONIBLE;

    private String descripcion;

    @NotNull(message = "El tipo de habitación es obligatorio")
    private Long tipoHabitacionId;

    @NotNull(message = "El hotel es obligatorio")
    private Long hotelId;
}