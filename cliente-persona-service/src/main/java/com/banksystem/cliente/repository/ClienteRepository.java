package com.banksystem.cliente.repository;

import com.banksystem.cliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByIdentificacion(String identificacion);

    Optional<Cliente> findByNumeroCliente(String numeroCliente);

    @Query("SELECT c FROM Cliente c WHERE c.estado = true")
    List<Cliente> findAllActivos();

    @Query("SELECT c FROM Cliente c WHERE c.nombre LIKE %:nombre% AND c.estado = true")
    List<Cliente> findByNombreContainingIgnoreCaseAndEstadoTrue(@Param("nombre") String nombre);

    boolean existsByNumeroCliente(String numeroCliente);

    boolean existsByIdentificacion(String identificacion);
}
