package com.aydsii.demo.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Datos de la operación realizada sobre la asignación")
public class AsignacionResponseDTO {

    @Schema(description = "ID del recolector", example = "5")
    private Integer recolectorId;

    @Schema(description = "ID del punto de recolección", example = "3")
    private Integer puntoRecoleccionId;

    @Schema(description = "Operación realizada", example = "ASIGNAR")
    private AccionAsignacion accion;

    @Schema(description = "Fecha y hora en que se realizó la operación", example = "2026-11-16")
    private LocalDate fecha;
}