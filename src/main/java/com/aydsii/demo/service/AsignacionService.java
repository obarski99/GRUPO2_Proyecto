package com.aydsii.demo.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aydsii.demo.dto.AccionAsignacion;
import com.aydsii.demo.dto.AsignacionRequestDTO;
import com.aydsii.demo.dto.AsignacionResponseDTO;
import com.aydsii.demo.exception.AccesoDenegadoException;
import com.aydsii.demo.exception.AsignacionInvalidaException;
import com.aydsii.demo.exception.RecursoNoEncontradoException;
import com.aydsii.demo.model.PuntoRecoleccion;
import com.aydsii.demo.model.Usuario;
import com.aydsii.demo.model.Asignacion;
import com.aydsii.demo.repository.AsignacionRepository;
import com.aydsii.demo.repository.PuntoRecoleccionRepository;
import com.aydsii.demo.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PuntoRecoleccionRepository puntoRecoleccionRepository;

    @Transactional
    public AsignacionResponseDTO gestionarAsignacion(AsignacionRequestDTO dto) {

        // Solo un administrador activo puede gestionar asignaciones
        Usuario admin = usuarioRepository.findById(dto.getAdministradorId())
                .orElseThrow(() -> new AccesoDenegadoException("Usuario no autorizado"));
        if (!"ADMINISTRADOR".equals(admin.getRol()) || !admin.isActivo()) {
            throw new AccesoDenegadoException("Solo un administrador habilitado puede gestionar asignaciones");
        }

        // Paso 2: existencia y estado (FA-02, FA-03)
        Usuario recolector = usuarioRepository.findById(dto.getRecolectorId())
                .filter(u -> "RECOLECTOR".equals(u.getRol()) && u.isActivo())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El recolector no existe o no se encuentra habilitado"));

        PuntoRecoleccion punto = puntoRecoleccionRepository.findById(dto.getPuntoRecoleccionId())
                .filter(p -> p.getActivo() == true)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El punto de recolección no existe o no se encuentra habilitado"));

        // Paso 3: condiciones de la operación (FA-04)
        // Optional permite que la variable pueda contener un valor o estar vacía, y devolviendo un booleano a la función isPresent()
        Optional<Asignacion> existente = asignacionRepository
                .findByUsuarioIdUsuarioAndPuntoRecoleccionIdPuntoRecoleccion(recolector.getIdUsuario(), punto.getIdPuntoRecoleccion());

        // Paso 4: ejecutar
        if (dto.getAccion() == AccionAsignacion.ASIGNAR) {
            if (existente.isPresent()) {
                throw new AsignacionInvalidaException("El recolector ya tiene asignado ese punto de recolección");
            }
            Asignacion nueva = new Asignacion();
            nueva.setUsuario(recolector);
            nueva.setPuntoRecoleccion(punto);
            nueva.setFechaDesde(LocalDate.now());
            asignacionRepository.save(nueva);
        } else {
            Asignacion asignacion = existente.orElseThrow(() -> new AsignacionInvalidaException(
                    "El recolector no tiene asignado ese punto de recolección"));
            asignacionRepository.delete(asignacion);
        }

        return AsignacionResponseDTO.builder()
                .recolectorId(recolector.getIdUsuario())
                .puntoRecoleccionId(punto.getIdPuntoRecoleccion())
                .accion(dto.getAccion())
                .fecha(LocalDate.now())
                .build();
    }
}