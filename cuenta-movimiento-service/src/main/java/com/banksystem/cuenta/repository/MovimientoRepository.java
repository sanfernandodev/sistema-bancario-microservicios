package com.banksystem.cuenta.repository;

import com.banksystem.cuenta.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId ORDER BY m.fecha DESC")
    List<Movimiento> findByCuentaId(@Param("cuentaId") Long cuentaId);

    @Query("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha DESC")
    List<Movimiento> findByCuentaIdAndFechaBetween(@Param("cuentaId") Long cuentaId,
                                                   @Param("fechaInicio") LocalDateTime fechaInicio,
                                                   @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM Movimiento m WHERE m.tipoMovimiento = :tipoMovimiento AND m.cuentaId = :cuentaId")
    List<Movimiento> findByTipoMovimientoAndCuentaId(@Param("tipoMovimiento") String tipoMovimiento,
                                                     @Param("cuentaId") Long cuentaId);

    long countByCuentaId(Long cuentaId);
}
