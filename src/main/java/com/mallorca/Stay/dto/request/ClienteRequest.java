package com.mallorca.Stay.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20)
    private String documento;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    private String nacionalidad;

    @Email(message = "El email no tiene formato válido")
    private String email;

    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
}