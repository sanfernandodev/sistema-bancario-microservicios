package com.banksystem.cuenta.repository;

import com.banksystem.cuenta.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    @Query("SELECT c FROM Cuenta c WHERE c.clienteId = :clienteId AND c.estado = true")
    List<Cuenta> findByClienteIdAndEstadoTrue(@Param("clienteId") Long clienteId);

    @Query("SELECT c FROM Cuenta c WHERE c.clienteId = :clienteId")
    List<Cuenta> findByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT c FROM Cuenta c WHERE c.estado = true")
    List<Cuenta> findAllActivas();

    boolean existsByNumeroCuenta(String numeroCuenta);

    long countByClienteId(Long clienteId);
}
