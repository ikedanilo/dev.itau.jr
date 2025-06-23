package com.desafio.dev.itau.jr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.desafio.dev.itau.jr.dto.TransacaoDTO;
import com.desafio.dev.itau.jr.dto.TransacaoResponseDTO;
import com.desafio.dev.itau.jr.mapper.TransacaoMapper;
import com.desafio.dev.itau.jr.model.Transacao;
import com.desafio.dev.itau.jr.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/transacao")
public class TransacaoController {

    // Injeção de dependência
    private final TransacaoService transacaoService;

    // Chamadas API
    @PostMapping
    @Operation(description = "Endpoint responsável por adicionar transações")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transação gravada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transacao.class))),
        @ApiResponse(responseCode = "422", description = "Campos inválidos", content = @Content),
        @ApiResponse(responseCode = "400", description = "Erro de requisição", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<TransacaoResponseDTO> salvarTransacao(@Valid @RequestBody TransacaoDTO dto) {
        Transacao transacaoSalva = transacaoService.salvarTransacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransacaoMapper.toDTO(transacaoSalva));
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint responsável por mostrar transação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transação mostrada com sucesso", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transacao.class))),
        @ApiResponse(responseCode = "400", description = "Erro de requisição", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<TransacaoResponseDTO> listarTransacao(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.listarTransacao(id));
    }

    @GetMapping
    @Operation(description = "Endpoint responsável por listar todas transações")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todas transações listadas com sucesso", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transacao.class))),
        @ApiResponse(responseCode = "400", description = "Erro de requisição", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<List<TransacaoResponseDTO>> listarTransacoes() {
        List<TransacaoResponseDTO> transacoes = transacaoService.listarTudo();
        return ResponseEntity.ok(transacoes);
    }

    @DeleteMapping
    @Operation(description = "Endpoint responsável por deletar todas transações")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todas transações deletadas com sucesso", content = @Content),
        @ApiResponse(responseCode = "400", description = "Erro de requisição", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<Void> deletarTransacoes() {
        transacaoService.limparTransacoes();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint responsável por alterar transação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transação alterada com sucesso", content = @Content),
        @ApiResponse(responseCode = "400", description = "Erro de requisição", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<TransacaoResponseDTO> alterarTransacao(@PathVariable Long id, @Valid @RequestBody TransacaoDTO dto) {
        Transacao transacaoAtualizada = transacaoService.atualizarTransacao(id, dto);
        return ResponseEntity.ok(TransacaoMapper.toDTO(transacaoAtualizada));
    }

}
