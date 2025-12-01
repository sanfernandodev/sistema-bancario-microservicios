package com.banksystem.cuenta.service;

import com.banksystem.cuenta.entity.Cuenta;
import com.banksystem.cuenta.entity.Movimiento;
import com.banksystem.cuenta.exception.CuentaNotFoundException;
import com.banksystem.cuenta.exception.SaldoNoDisponibleException;
import com.banksystem.cuenta.repository.CuentaRepository;
import com.banksystem.cuenta.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public MovimientoService(MovimientoRepository movimientoRepository,
                            CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Obtener todos los movimientos
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerTodos() {
        return movimientoRepository.findAll();
    }

    /**
     * Obtener movimientos por ID de cuenta
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    /**
     * Obtener movimientos por cuenta y rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerMovimientosPorFechas(Long cuentaId,
                                                        LocalDateTime fechaInicio,
                                                        LocalDateTime fechaFin) {
        return movimientoRepository.findByCuentaIdAndFechaBetween(cuentaId, fechaInicio, fechaFin);
    }

    /**
     * Registrar un movimiento (depósito o retiro)
     * F2: Registro de movimientos - Actualizar saldo disponible
     */
    public Movimiento registrarMovimiento(Long cuentaId, String tipoMovimiento, BigDecimal valor) {
        // Obtener cuenta
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + cuentaId));

        // Validar saldo (F3)
        if ("Retiro".equalsIgnoreCase(tipoMovimiento)) {
            if (cuenta.getSaldoDisponible().compareTo(valor) < 0) {
                throw new SaldoNoDisponibleException("Saldo no disponible");
            }
        }

        // Calcular nuevo saldo
        BigDecimal nuevoSaldo;
        if ("Deposito".equalsIgnoreCase(tipoMovimiento)) {
            nuevoSaldo = cuenta.getSaldoDisponible().add(valor);
        } else if ("Retiro".equalsIgnoreCase(tipoMovimiento)) {
            nuevoSaldo = cuenta.getSaldoDisponible().subtract(valor);
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no válido: " + tipoMovimiento);
        }

        // Actualizar saldo de la cuenta
        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepository.save(cuenta);

        // Registrar movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setValor(valor);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuentaId(cuentaId);
        movimiento.setDescripcion(tipoMovimiento + " de " + valor);

        return movimientoRepository.save(movimiento);
    }

    /**
     * Obtener movimientos por tipo
     */
    @Transactional(readOnly = true)
    public List<Movimiento> obtenerMovimientosPorTipo(Long cuentaId, String tipoMovimiento) {
        return movimientoRepository.findByTipoMovimientoAndCuentaId(tipoMovimiento, cuentaId);
    }

    /**
     * Obtener un movimiento por ID
     */
    @Transactional(readOnly = true)
    public Movimiento obtenerPorId(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("Movimiento no encontrado con ID: " + id));
    }
}
