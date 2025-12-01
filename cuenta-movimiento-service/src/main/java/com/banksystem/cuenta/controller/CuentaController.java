package com.banksystem.cuenta.controller;

import com.banksystem.cuenta.entity.Cuenta;
import com.banksystem.cuenta.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    /**
     * GET /api/cuentas - Obtener todas las cuentas
     */
    @GetMapping
    public ResponseEntity<List<Cuenta>> obtenerTodas() {
        List<Cuenta> cuentas = cuentaService.obtenerTodas();
        return ResponseEntity.ok(cuentas);
    }

    /**
     * GET /api/cuentas/activas - Obtener cuentas activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<Cuenta>> obtenerCuentasActivas() {
        List<Cuenta> cuentas = cuentaService.obtenerCuentasActivas();
        return ResponseEntity.ok(cuentas);
    }

    /**
     * GET /api/cuentas/{id} - Obtener cuenta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtenerPorId(@PathVariable Long id) {
        Cuenta cuenta = cuentaService.obtenerPorId(id);
        return ResponseEntity.ok(cuenta);
    }

    /**
     * GET /api/cuentas/numero/{numeroCuenta} - Obtener cuenta por número
     */
    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<Cuenta> obtenerPorNumeroCuenta(@PathVariable String numeroCuenta) {
        Cuenta cuenta = cuentaService.obtenerPorNumeroCuenta(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    /**
     * GET /api/cuentas/cliente/{clienteId} - Obtener cuentas por cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Cuenta>> obtenerCuentasPorCliente(@PathVariable Long clienteId) {
        List<Cuenta> cuentas = cuentaService.obtenerCuentasPorCliente(clienteId);
        return ResponseEntity.ok(cuentas);
    }

    /**
     * POST /api/cuentas - Crear nueva cuenta
     */
    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@Valid @RequestBody Cuenta cuenta) {
        Cuenta nuevaCuenta = cuentaService.crearCuenta(cuenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
    }

    /**
     * PUT /api/cuentas/{id} - Actualizar cuenta
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> actualizarCuenta(@PathVariable Long id,
                                                   @Valid @RequestBody Cuenta cuenta) {
        Cuenta cuentaActualizada = cuentaService.actualizarCuenta(id, cuenta);
        return ResponseEntity.ok(cuentaActualizada);
    }

    /**
     * PATCH /api/cuentas/{id}/estado - Cambiar estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cuenta> cambiarEstado(@PathVariable Long id,
                                               @RequestParam Boolean estado) {
        Cuenta cuenta = cuentaService.cambiarEstado(id, estado);
        return ResponseEntity.ok(cuenta);
    }

    /**
     * DELETE /api/cuentas/{id} - Eliminar cuenta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long id) {
        cuentaService.eliminarCuenta(id);
        return ResponseEntity.noContent().build();
    }
}
