package com.banksystem.cuenta.controller;

import com.banksystem.cuenta.entity.Movimiento;
import com.banksystem.cuenta.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    /**
     * GET /api/movimientos - Obtener todos los movimientos
     */
    @GetMapping
    public ResponseEntity<List<Movimiento>> obtenerTodos() {
        List<Movimiento> movimientos = movimientoService.obtenerTodos();
        return ResponseEntity.ok(movimientos);
    }

    /**
     * GET /api/movimientos/cuenta/{cuentaId} - Obtener movimientos por cuenta
     */
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorCuenta(@PathVariable Long cuentaId) {
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(cuentaId);
        return ResponseEntity.ok(movimientos);
    }

    /**
     * GET /api/movimientos/cuenta/{cuentaId}/fechas - Movimientos por rango de fechas
     */
    @GetMapping("/cuenta/{cuentaId}/fechas")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorFechas(
            @PathVariable Long cuentaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorFechas(cuentaId, fechaInicio, fechaFin);
        return ResponseEntity.ok(movimientos);
    }

    /**
     * GET /api/movimientos/{id} - Obtener movimiento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> obtenerPorId(@PathVariable Long id) {
        Movimiento movimiento = movimientoService.obtenerPorId(id);
        return ResponseEntity.ok(movimiento);
    }

    /**
     * POST /api/movimientos/registrar - Registrar nuevo movimiento (depósito o retiro)
     * F2: Registro de movimientos
     * F3: Validación de saldo
     */
    @PostMapping("/registrar")
    public ResponseEntity<Movimiento> registrarMovimiento(
            @RequestParam Long cuentaId,
            @RequestParam String tipoMovimiento,
            @RequestParam BigDecimal valor) {
        Movimiento movimiento = movimientoService.registrarMovimiento(cuentaId, tipoMovimiento, valor);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }

    /**
     * POST /api/movimientos - Crear movimiento (sin afectar saldo)
     */
    @PostMapping
    public ResponseEntity<Movimiento> crearMovimiento(@Valid @RequestBody Movimiento movimiento) {
        // Este endpoint es para uso interno, sin lógica de transacción
        Movimiento nuevoMovimiento = movimientoService.obtenerPorId(movimiento.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMovimiento);
    }

    /**
     * GET /api/movimientos/tipo/{cuentaId} - Obtener movimientos por tipo
     */
    @GetMapping("/tipo/{cuentaId}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorTipo(
            @PathVariable Long cuentaId,
            @RequestParam String tipoMovimiento) {
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorTipo(cuentaId, tipoMovimiento);
        return ResponseEntity.ok(movimientos);
    }
}
