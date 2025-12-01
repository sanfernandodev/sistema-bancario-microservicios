package com.banksystem.cliente;

import com.banksystem.cliente.entity.Cliente;
import com.banksystem.cliente.exception.ClienteAlreadyExistsException;
import com.banksystem.cliente.exception.ClienteNotFoundException;
import com.banksystem.cliente.repository.ClienteRepository;
import com.banksystem.cliente.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("M");
        cliente.setEdad(35);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");
        cliente.setContrasena("1234");
        cliente.setEstado(true);
        cliente.setNumeroCliente("CLI-ABC12345");
    }

    @Test
    public void testCrearClienteExitosamente() {
        // Arrange
        when(clienteRepository.existsByIdentificacion(cliente.getIdentificacion())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.crearCliente(cliente);

        // Assert
        assertNotNull(resultado);
        assertEquals("Jose Lema", resultado.getNombre());
        assertEquals("1234567890", resultado.getIdentificacion());
        assertTrue(resultado.getEstado());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testCrearClienteConIdentificacionDuplicada() {
        // Arrange
        when(clienteRepository.existsByIdentificacion(cliente.getIdentificacion())).thenReturn(true);

        // Act & Assert
        assertThrows(ClienteAlreadyExistsException.class, () -> {
            clienteService.crearCliente(cliente);
        });
        verify(clienteRepository, times(0)).save(any(Cliente.class));
    }

    @Test
    public void testObtenerClientePorIdExitosamente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente resultado = clienteService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Jose Lema", resultado.getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    public void testObtenerClientePorIdNoEncontrado() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class, () -> {
            clienteService.obtenerPorId(99L);
        });
    }

    @Test
    public void testActualizarClienteExitosamente() {
        // Arrange
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setNombre("Jose Lema Actualizado");
        clienteActualizado.setIdentificacion("1234567890");
        clienteActualizado.setDireccion("Nueva direccion");
        clienteActualizado.setTelefono("987654321");
        clienteActualizado.setEdad(36);
        clienteActualizado.setGenero("M");
        clienteActualizado.setContrasena("4321");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.actualizarCliente(1L, clienteActualizado);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testEliminarClienteExitosamente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(any(Cliente.class));

        // Act
        clienteService.eliminarCliente(1L);

        // Assert
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    public void testCambiarEstadoCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.cambiarEstado(1L, false);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
}
