package com.banksystem.cliente.service;

import com.banksystem.cliente.entity.Cliente;
import com.banksystem.cliente.exception.ClienteAlreadyExistsException;
import com.banksystem.cliente.exception.ClienteNotFoundException;
import com.banksystem.cliente.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Obtener todos los clientes
     */
    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    /**
     * Obtener todos los clientes activos
     */
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientesActivos() {
        return clienteRepository.findAllActivos();
    }

    /**
     * Obtener cliente por ID
     */
    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));
    }

    /**
     * Obtener cliente por identificación
     */
    @Transactional(readOnly = true)
    public Cliente obtenerPorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new ClienteNotFoundException(
                        "Cliente no encontrado con identificación: " + identificacion));
    }

    /**
     * Obtener cliente por número de cliente
     */
    @Transactional(readOnly = true)
    public Cliente obtenerPorNumeroCliente(String numeroCliente) {
        return clienteRepository.findByNumeroCliente(numeroCliente)
                .orElseThrow(() -> new ClienteNotFoundException(
                        "Cliente no encontrado con número: " + numeroCliente));
    }

    /**
     * Buscar clientes por nombre
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCaseAndEstadoTrue(nombre);
    }

    /**
     * Crear nuevo cliente
     */
    public Cliente crearCliente(Cliente cliente) {
        // Validar que no exista cliente con la misma identificación
        if (clienteRepository.existsByIdentificacion(cliente.getIdentificacion())) {
            throw new ClienteAlreadyExistsException(
                    "Ya existe un cliente con identificación: " + cliente.getIdentificacion());
        }

        // Generar número de cliente único
        cliente.setNumeroCliente(generarNumeroCliente());
        cliente.setEstado(true);

        return clienteRepository.save(cliente);
    }

    /**
     * Actualizar cliente existente
     */
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = obtenerPorId(id);

        // Validar que no exista otro cliente con la misma identificación
        if (!clienteExistente.getIdentificacion().equals(clienteActualizado.getIdentificacion()) &&
                clienteRepository.existsByIdentificacion(clienteActualizado.getIdentificacion())) {
            throw new ClienteAlreadyExistsException(
                    "Ya existe otro cliente con identificación: " + clienteActualizado.getIdentificacion());
        }

        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setGenero(clienteActualizado.getGenero());
        clienteExistente.setEdad(clienteActualizado.getEdad());
        clienteExistente.setIdentificacion(clienteActualizado.getIdentificacion());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());

        if (clienteActualizado.getContrasena() != null && !clienteActualizado.getContrasena().isEmpty()) {
            clienteExistente.setContrasena(clienteActualizado.getContrasena());
        }

        return clienteRepository.save(clienteExistente);
    }

    /**
     * Cambiar estado del cliente
     */
    public Cliente cambiarEstado(Long id, Boolean nuevoEstado) {
        Cliente cliente = obtenerPorId(id);
        cliente.setEstado(nuevoEstado);
        return clienteRepository.save(cliente);
    }

    /**
     * Eliminar cliente
     */
    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerPorId(id);
        clienteRepository.delete(cliente);
    }

    /**
     * Generar número de cliente único
     */
    private String generarNumeroCliente() {
        return "CLI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
