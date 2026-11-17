package com.aydsii.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aydsii.demo.dto.AccionAsignacion;
import com.aydsii.demo.dto.AsignacionRequestDTO;
import com.aydsii.demo.dto.AsignacionResponseDTO;
import com.aydsii.demo.service.AsignacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping ("/api/asignaciones")
@RequiredArgsConstructor
@Tag(name = "Asignaciones", description = "Gestión de asignaciones de puntos de recolección a recolectores")
public class AsignacionController {
    
    private final AsignacionService asignacionService;

    @PostMapping
    @Operation (
        summary = "Registrar o quitar una asignación",
        description = "Registra o quita la asignación de un punto de recolección a un recolector según la acción indicada."
    )
    @ApiResponses ({
        @ApiResponse (
            responseCode = "201",
            description = "Asignación registrada correctamente"
        ),
        @ApiResponse (
            responseCode = "200",
            description = "Asignación quitada correctamente"
        ),
        @ApiResponse (
            responseCode = "400",
            description = "Datos incompletos o inválidos (por ejemplo, acción no válida)"
        ),
        @ApiResponse (
            responseCode = "404",
            description = "No existe el recolector o el punto de recolección"
        ),
        @ApiResponse (
            responseCode = "403",
            description = "El recolector ya tiene asignado el punto de recolección (cuando se intenta registrar) "
                    + "o no tiene asignado el punto de recolección (cuando se intenta quitar)"
        ),
        @ApiResponse (
            responseCode = "500",
            description = "Error al registrar o quitar la asignación"
        )
    })
    public ResponseEntity<com.aydsii.demo.dto.ApiResponse<AsignacionResponseDTO>> gestionarAsignacion(@Valid @RequestBody AsignacionRequestDTO asignacionRequestDTO){

        AsignacionResponseDTO resultado = asignacionService.gestionarAsignacion(asignacionRequestDTO);

        if (resultado.getAccion() == AccionAsignacion.ASIGNAR) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    com.aydsii.demo.dto.ApiResponse.success(201, "Asignación registrada correctamente", resultado));
        }
        return ResponseEntity.ok(
                com.aydsii.demo.dto.ApiResponse.success(200, "Asignación quitada correctamente", resultado));
    }
    
}
