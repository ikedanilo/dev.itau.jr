package com.desafio.dev.itau.jr.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.desafio.dev.itau.jr.dto.TransacaoDTO;
import com.desafio.dev.itau.jr.dto.TransacaoResponseDTO;
import com.desafio.dev.itau.jr.exceptions.UnprocessableEntity;
import com.desafio.dev.itau.jr.model.Transacao;
import com.desafio.dev.itau.jr.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@WebMvcTest(TransacaoController.class)
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransacaoService transacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void salvarTransacao_DeveRetornarCreated() throws Exception {
        // Arrange
        TransacaoDTO dto = new TransacaoDTO(10.0, OffsetDateTime.of(2025, 2, 11, 12, 0, 0, 0, ZoneOffset.UTC));
        Transacao transacaoSalva = new Transacao();
        transacaoSalva.setId(1L);
        transacaoSalva.setValor(10.0);
        transacaoSalva.setDataHora(dto.dataHora());

        when(transacaoService.salvarTransacao(Mockito.any(TransacaoDTO.class))).thenReturn(transacaoSalva);

        // Act & Assert
        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    void deveGerarExcecaoAoSalvarTransacao_DeveRetornarUnprocessableEntity() throws Exception {
        // Arrange
        TransacaoDTO dto = new TransacaoDTO(-10.0, OffsetDateTime.of(2025, 2, 11, 12, 0, 0, 0, ZoneOffset.UTC));

        // Forçando o service a lançar a exceção personalizada ao tentar salvar
        when(transacaoService.salvarTransacao(any(TransacaoDTO.class)))
            .thenThrow(new UnprocessableEntity("Valor inválido"));

        // Act & Assert
        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isUnprocessableEntity()) // Código HTTP 422
            .andExpect(jsonPath("$.error").value("Unprocessable Entity"))
            .andExpect(jsonPath("$.message").value("Valor inválido"));
    }


    @Test
    void listarTransacao_DeveRetornarTransacaoPorId() throws Exception {
        // Arrange
        TransacaoResponseDTO response = new TransacaoResponseDTO(1L, 10.0, OffsetDateTime.now());

        when(transacaoService.listarTransacao(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/transacao/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.valor").value(10.0));
    }

    @Test
    void listarTransacoes_DeveRetornarLista() throws Exception {
        // Arrange
        TransacaoResponseDTO t1 = new TransacaoResponseDTO(1L, 10.0, OffsetDateTime.now());
        TransacaoResponseDTO t2 = new TransacaoResponseDTO(2L, 20.0, OffsetDateTime.now());

        when(transacaoService.listarTudo()).thenReturn(List.of(t1, t2));

        // Act & Assert
        mockMvc.perform(get("/transacao"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deletarTransacoes_DeveRetornarNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/transacao"))
            .andExpect(status().isNoContent());

        verify(transacaoService).limparTransacoes();
    }

    @Test
    void alterarTransacao_DeveRetornarTransacaoAtualizada() throws Exception {
        // Arrange
        TransacaoDTO dto = new TransacaoDTO(50.0, OffsetDateTime.now());

        Transacao transacaoAtualizada = new Transacao();
        transacaoAtualizada.setId(1L);
        transacaoAtualizada.setValor(50.0);
        transacaoAtualizada.setDataHora(dto.dataHora());

        when(transacaoService.atualizarTransacao(eq(1L), any(TransacaoDTO.class))).thenReturn(transacaoAtualizada);

        // Act & Assert
        mockMvc.perform(put("/transacao/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.valor").value(50.0));
    }
}
