package com.aydsii.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Datos para registrar o quitar la asignación de un punto de recolección a un recolector")
public class AsignacionRequestDTO {
    @NotNull(message = "El administrador es obligatorio")
    @Schema(description = "ID del administrador que realiza la gestión", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer administradorId;

    @NotNull(message = "El recolector es obligatorio")
    @Schema(description = "ID del recolector", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer recolectorId;

    @NotNull(message = "El punto de recolección es obligatorio")
    @Schema(description = "ID del punto de recolección", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer puntoRecoleccionId;

    @NotNull(message = "La acción es obligatoria (ASIGNAR o QUITAR)")
    @Schema(description = "Acción a realizar sobre la asignación", example = "ASIGNAR", requiredMode = Schema.RequiredMode.REQUIRED)
    private AccionAsignacion accion;
}
