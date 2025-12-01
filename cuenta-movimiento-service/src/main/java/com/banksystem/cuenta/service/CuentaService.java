package com.banksystem.cuenta.service;

import com.banksystem.cuenta.entity.Cuenta;
import com.banksystem.cuenta.exception.CuentaAlreadyExistsException;
import com.banksystem.cuenta.exception.CuentaNotFoundException;
import com.banksystem.cuenta.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Obtener todas las cuentas
     */
    @Transactional(readOnly = true)
    public List<Cuenta> obtenerTodas() {
        return cuentaRepository.findAll();
    }

    /**
     * Obtener todas las cuentas activas
     */
    @Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentasActivas() {
        return cuentaRepository.findAllActivas();
    }

    /**
     * Obtener cuenta por ID
     */
    @Transactional(readOnly = true)
    public Cuenta obtenerPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + id));
    }

    /**
     * Obtener cuenta por número de cuenta
     */
    @Transactional(readOnly = true)
    public Cuenta obtenerPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException(
                        "Cuenta no encontrada con número: " + numeroCuenta));
    }

    /**
     * Obtener cuentas por cliente ID
     */
    @Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentasPorCliente(Long clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }

    /**
     * Obtener cuentas activas por cliente ID
     */
    @Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentasActivasPorCliente(Long clienteId) {
        return cuentaRepository.findByClienteIdAndEstadoTrue(clienteId);
    }

    /**
     * Crear nueva cuenta
     */
    public Cuenta crearCuenta(Cuenta cuenta) {
        // Validar que no exista cuenta con el mismo número
        if (cuentaRepository.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
            throw new CuentaAlreadyExistsException(
                    "Ya existe una cuenta con número: " + cuenta.getNumeroCuenta());
        }

        cuenta.setEstado(true);
        return cuentaRepository.save(cuenta);
    }

    /**
     * Actualizar cuenta (sin cambiar saldo)
     */
    public Cuenta actualizarCuenta(Long id, Cuenta cuentaActualizada) {
        Cuenta cuentaExistente = obtenerPorId(id);

        // Validar número de cuenta único
        if (!cuentaExistente.getNumeroCuenta().equals(cuentaActualizada.getNumeroCuenta()) &&
                cuentaRepository.existsByNumeroCuenta(cuentaActualizada.getNumeroCuenta())) {
            throw new CuentaAlreadyExistsException(
                    "Ya existe otra cuenta con número: " + cuentaActualizada.getNumeroCuenta());
        }

        cuentaExistente.setTipoCuenta(cuentaActualizada.getTipoCuenta());

        return cuentaRepository.save(cuentaExistente);
    }

    /**
     * Cambiar estado de la cuenta
     */
    public Cuenta cambiarEstado(Long id, Boolean nuevoEstado) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.setEstado(nuevoEstado);
        return cuentaRepository.save(cuenta);
    }

    /**
     * Eliminar cuenta
     */
    public void eliminarCuenta(Long id) {
        Cuenta cuenta = obtenerPorId(id);
        cuentaRepository.delete(cuenta);
    }
}
