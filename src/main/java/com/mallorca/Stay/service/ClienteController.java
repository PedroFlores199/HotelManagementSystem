package com.mallorca.Stay.controller;

import com.mallorca.Stay.dto.request.ClienteRequest;
import com.mallorca.Stay.dto.response.ClienteResponse;
import com.mallorca.Stay.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @GetMapping("/documento/{documento}")
    public ResponseEntity<ClienteResponse> findByDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(clienteService.findByDocumento(documento));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponse>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(clienteService.buscar(q));
    }

    @GetMapping("/frecuentes")
    public ResponseEntity<List<ClienteResponse>> findFrecuentes() {
        return ResponseEntity.ok(clienteService.findClientesFrecuentes());
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}