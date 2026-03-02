package com.example.backend;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficios")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Beneficiários", description = "Operações de CRUD e transferência de beneficiários")
public class BeneficioController {

    private final com.example.backend.service.BeneficioService service;

    public BeneficioController(com.example.backend.service.BeneficioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os beneficiários")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<com.example.ejb.Beneficio> list() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar beneficiário por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Beneficiário encontrado"),
        @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    })
    public com.example.ejb.Beneficio get(
            @Parameter(description = "ID do beneficiário") @PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Benefício não encontrado"));
    }

    @PostMapping
    @Operation(summary = "Criar novo beneficiário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Beneficiário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public com.example.ejb.Beneficio create(@RequestBody com.example.ejb.Beneficio b) {
        return service.create(b);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar beneficiário existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Beneficiário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    })
    public com.example.ejb.Beneficio update(
            @Parameter(description = "ID do beneficiário") @PathVariable Long id,
            @RequestBody com.example.ejb.Beneficio b) {
        return service.update(id, b);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir beneficiário")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Beneficiário excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    })
    public void delete(
            @Parameter(description = "ID do beneficiário") @PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transferir valor entre beneficiários",
               description = "Transfere um valor do beneficiário de origem para o de destino. Retorna erro 400 se saldo insuficiente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    })
    public void transfer(
            @Parameter(description = "ID do beneficiário de origem") @RequestParam Long fromId,
            @Parameter(description = "ID do beneficiário de destino") @RequestParam Long toId,
            @Parameter(description = "Valor a transferir") @RequestParam java.math.BigDecimal amount) {
        service.transfer(fromId, toId, amount);
    }
}
