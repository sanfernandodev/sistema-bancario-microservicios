package com.banksystem.cuenta.controller;

import com.banksystem.cuenta.entity.Cuenta;
import com.banksystem.cuenta.entity.Movimiento;
import com.banksystem.cuenta.service.CuentaService;
import com.banksystem.cuenta.service.MovimientoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReporteController {

    private final CuentaService cuentaService;
    private final MovimientoService movimientoService;

    public ReporteController(CuentaService cuentaService, MovimientoService movimientoService) {
        this.cuentaService = cuentaService;
        this.movimientoService = movimientoService;
    }

    /**
     * GET /api/reportes/estado-cuenta - Reporte de estado de cuenta
     * F4: Reportes - Estado de cuenta por rango de fechas y cliente
     */
    @GetMapping("/estado-cuenta")
    public ResponseEntity<Map<String, Object>> obtenerEstadoCuenta(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        // Obtener cuentas del cliente
        List<Cuenta> cuentas = cuentaService.obtenerCuentasPorCliente(clienteId);

        // Construir reporte
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("clienteId", clienteId);
        reporte.put("fechaInicio", fechaInicio);
        reporte.put("fechaFin", fechaFin);
        reporte.put("fechaReporte", LocalDateTime.now());

        List<Map<String, Object>> detallesCuentas = new ArrayList<>();

        // Convertir fechas a LocalDateTime
        LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime finDateTime = fechaFin.atTime(LocalTime.MAX);

        for (Cuenta cuenta : cuentas) {
            Map<String, Object> detalleCuenta = new HashMap<>();
            detalleCuenta.put("numeroCuenta", cuenta.getNumeroCuenta());
            detalleCuenta.put("tipoCuenta", cuenta.getTipoCuenta());
            detalleCuenta.put("saldoInicial", cuenta.getSaldoInicial());
            detalleCuenta.put("saldoActual", cuenta.getSaldoDisponible());
            detalleCuenta.put("estado", cuenta.getEstado());

            // Obtener movimientos del período
            List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorFechas(
                    cuenta.getId(), inicioDateTime, finDateTime);

            List<Map<String, Object>> detallesMovimientos = new ArrayList<>();
            for (Movimiento mov : movimientos) {
                Map<String, Object> detalleMovimiento = new HashMap<>();
                detalleMovimiento.put("fecha", mov.getFecha());
                detalleMovimiento.put("tipo", mov.getTipoMovimiento());
                detalleMovimiento.put("valor", mov.getValor());
                detalleMovimiento.put("saldo", mov.getSaldo());
                detalleMovimiento.put("descripcion", mov.getDescripcion());
                detallesMovimientos.add(detalleMovimiento);
            }

            detalleCuenta.put("movimientos", detallesMovimientos);
            detallesCuentas.add(detalleCuenta);
        }

        reporte.put("cuentas", detallesCuentas);
        return ResponseEntity.ok(reporte);
    }
}
