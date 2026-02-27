package com.mallorca.Stay.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponse {
    private Long id;
    private String nombre;
    private String apellidos;
    private String documento;
    private String tipoDocumento;
    private String nacionalidad;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private Boolean clienteFrecuente;
    private Integer totalEstancias;
    private LocalDateTime createdAt;
}