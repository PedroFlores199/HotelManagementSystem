package com.mallorca.Stay.service;

import com.mallorca.Stay.dto.request.ClienteRequest;
import com.mallorca.Stay.dto.response.ClienteResponse;
import java.util.List;

public interface ClienteService {
    List<ClienteResponse> findAll();
    ClienteResponse findById(Long id);
    ClienteResponse findByDocumento(String documento);
    List<ClienteResponse> buscar(String texto);
    List<ClienteResponse> findClientesFrecuentes();
    ClienteResponse create(ClienteRequest request);
    ClienteResponse update(Long id, ClienteRequest request);
    void delete(Long id);
}