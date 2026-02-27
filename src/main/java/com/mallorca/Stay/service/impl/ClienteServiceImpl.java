package com.mallorca.Stay.service.impl;

import com.mallorca.Stay.domain.entity.Cliente;
import com.mallorca.Stay.dto.request.ClienteRequest;
import com.mallorca.Stay.dto.response.ClienteResponse;
import com.mallorca.Stay.repository.ClienteRepository;
import com.mallorca.Stay.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<ClienteResponse> findAll() {
        return clienteRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ClienteResponse findById(Long id) {
        return clienteRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
    }

    @Override
    public ClienteResponse findByDocumento(String documento) {
        return clienteRepository.findByDocumento(documento)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con documento: " + documento));
    }

    @Override
    public List<ClienteResponse> buscar(String texto) {
        return clienteRepository.buscar(texto)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ClienteResponse> findClientesFrecuentes() {
        return clienteRepository.findByClienteFrecuenteTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ClienteResponse create(ClienteRequest request) {
        if (clienteRepository.findByDocumento(request.getDocumento()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con el documento: " + request.getDocumento());
        }

        var cliente = Cliente.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .documento(request.getDocumento())
                .tipoDocumento(request.getTipoDocumento())
                .nacionalidad(request.getNacionalidad())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .fechaNacimiento(request.getFechaNacimiento())
                .clienteFrecuente(false)
                .totalEstancias(0)
                .activo(true)
                .build();

        return toResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public ClienteResponse update(Long id, ClienteRequest request) {
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));

        cliente.setNombre(request.getNombre());
        cliente.setApellidos(request.getApellidos());
        cliente.setDocumento(request.getDocumento());
        cliente.setTipoDocumento(request.getTipoDocumento());
        cliente.setNacionalidad(request.getNacionalidad());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setFechaNacimiento(request.getFechaNacimiento());

        return toResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    private ClienteResponse toResponse(Cliente c) {
        return ClienteResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .apellidos(c.getApellidos())
                .documento(c.getDocumento())
                .tipoDocumento(c.getTipoDocumento())
                .nacionalidad(c.getNacionalidad())
                .email(c.getEmail())
                .telefono(c.getTelefono())
                .direccion(c.getDireccion())
                .fechaNacimiento(c.getFechaNacimiento())
                .clienteFrecuente(c.getClienteFrecuente())
                .totalEstancias(c.getTotalEstancias())
                .createdAt(c.getCreatedAt())
                .build();
    }
}