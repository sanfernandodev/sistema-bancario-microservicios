package com.banksystem.cliente.controller;

import com.banksystem.cliente.entity.Cliente;
import com.banksystem.cliente.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * GET /api/clientes - Obtener todos los clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodos() {
        List<Cliente> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/clientes/activos - Obtener todos los clientes activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> obtenerActivos() {
        List<Cliente> clientes = clienteService.obtenerClientesActivos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/clientes/{id} - Obtener cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * GET /api/clientes/identificacion/{identificacion} - Obtener cliente por identificación
     */
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<Cliente> obtenerPorIdentificacion(@PathVariable String identificacion) {
        Cliente cliente = clienteService.obtenerPorIdentificacion(identificacion);
        return ResponseEntity.ok(cliente);
    }

    /**
     * GET /api/clientes/numero/{numeroCliente} - Obtener cliente por número de cliente
     */
    @GetMapping("/numero/{numeroCliente}")
    public ResponseEntity<Cliente> obtenerPorNumeroCliente(@PathVariable String numeroCliente) {
        Cliente cliente = clienteService.obtenerPorNumeroCliente(numeroCliente);
        return ResponseEntity.ok(cliente);
    }

    /**
     * GET /api/clientes/buscar?nombre=X - Buscar clientes por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombre) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }

    /**
     * POST /api/clientes - Crear nuevo cliente
     */
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    /**
     * PUT /api/clientes/{id} - Actualizar cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id,
                                                     @Valid @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * PATCH /api/clientes/{id}/estado - Cambiar estado del cliente
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cliente> cambiarEstado(@PathVariable Long id,
                                                 @RequestParam Boolean estado) {
        Cliente cliente = clienteService.cambiarEstado(id, estado);
        return ResponseEntity.ok(cliente);
    }

    /**
     * DELETE /api/clientes/{id} - Eliminar cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
