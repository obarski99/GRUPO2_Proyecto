package com.aydsii.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Operación a realizar sobre la asignación")
public enum AccionAsignacion {
    ASIGNAR,
    QUITAR
}
