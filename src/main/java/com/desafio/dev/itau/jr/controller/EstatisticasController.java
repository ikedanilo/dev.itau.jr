package com.desafio.dev.itau.jr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.desafio.dev.itau.jr.dto.EstatiscasResponseDTO;
import com.desafio.dev.itau.jr.service.EstatiscasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/estatistica")
@RequiredArgsConstructor
public class EstatisticasController {

    private final EstatiscasService estatisticasService;

    @GetMapping
    @Operation(description = "Endpoint responsável por buscar estatísticas de transações")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca efetuada com sucesso", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstatiscasResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Erro na busca de estatística de transações", content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<EstatiscasResponseDTO> buscarEstatistica(
        @Parameter(description = "Intervalo de busca em segundos. Padrão: 60 segundos.")
        @RequestParam(value = "intervalorBusca", required = false, defaultValue = "60") Integer intervaloBusca) {

            return ResponseEntity.ok(estatisticasService.calcularEstatisticasTransacoes(intervaloBusca));
    }


}
