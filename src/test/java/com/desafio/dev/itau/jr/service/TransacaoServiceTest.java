package com.desafio.dev.itau.jr.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.Optional;

import com.desafio.dev.itau.jr.dto.TransacaoDTO;
import com.desafio.dev.itau.jr.dto.TransacaoResponseDTO;
import com.desafio.dev.itau.jr.exceptions.RecursoNaoEncontradoException;
import com.desafio.dev.itau.jr.exceptions.UnprocessableEntity;
import com.desafio.dev.itau.jr.model.Transacao;
import com.desafio.dev.itau.jr.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void salvarTransacao_DeveSalvarComSucesso_QuandoDadosValidos() {
        // Arrange
        TransacaoDTO dto = new TransacaoDTO(10.0, OffsetDateTime.now().minusMinutes(1));
        Transacao transacaoSalva = new Transacao();
        transacaoSalva.setId(1L);
        transacaoSalva.setValor(dto.valor());
        transacaoSalva.setDataHora(dto.dataHora());

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoSalva);

        // Act
        Transacao resultado = transacaoService.salvarTransacao(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(dto.valor(), resultado.getValor());
        assertEquals(dto.dataHora(), resultado.getDataHora());

        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void salvarTransacao_DeveLancarExcecao_QuandoDataFutura() {
        // Arrange
        TransacaoDTO dto = new TransacaoDTO(10.0, OffsetDateTime.now().plusMinutes(1));

        // Act & Assert
        assertThrows(UnprocessableEntity.class, () -> transacaoService.salvarTransacao(dto));

        verify(transacaoRepository, never()).save(any());
    }

    @Test
    void salvarTransacao_DeveLancarExcecao_QuandoValorNegativo() {
        // Arrange
        TransacaoDTO dto = new TransacaoDTO(-5.0, OffsetDateTime.now().minusMinutes(1));

        // Act & Assert
        assertThrows(UnprocessableEntity.class, () -> transacaoService.salvarTransacao(dto));

        verify(transacaoRepository, never()).save(any());
    }

    @Test
    void listarTransacao_DeveRetornarTransacao_QuandoExistir() {
        // Arrange
        Transacao transacao = new Transacao();
        transacao.setId(1L);
        transacao.setValor(10.0);
        transacao.setDataHora(OffsetDateTime.now());

        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));

        // Act
        TransacaoResponseDTO resultado = transacaoService.listarTransacao(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals(10.0, resultado.valor());

        verify(transacaoRepository, times(1)).findById(1L);
    }

    @Test
    void listarTransacao_DeveLancarExcecao_QuandoNaoExistir() {
        // Arrange
        when(transacaoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNaoEncontradoException.class, () -> transacaoService.listarTransacao(1L));

        verify(transacaoRepository, times(1)).findById(1L);
    }

    @Test
    void limparTransacoes_DeveChamarDeleteAll() {
        // Act
        transacaoService.limparTransacoes();

        // Assert
        verify(transacaoRepository, times(1)).deleteAll();
    }

    @Test
    void atualizarTransacao_DeveAtualizar_QuandoTransacaoExistir() {
        // Arrange
        Transacao transacaoExistente = new Transacao();
        transacaoExistente.setId(1L);
        transacaoExistente.setValor(5.0);
        transacaoExistente.setDataHora(OffsetDateTime.now().minusMinutes(10));

        TransacaoDTO dto = new TransacaoDTO(20.0, OffsetDateTime.now());

        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacaoExistente));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoExistente);

        // Act
        Transacao resultado = transacaoService.atualizarTransacao(1L, dto);

        // Assert
        assertEquals(20.0, resultado.getValor());
        assertEquals(dto.dataHora(), resultado.getDataHora());

        verify(transacaoRepository, times(1)).findById(1L);
        verify(transacaoRepository, times(1)).save(transacaoExistente);
    }

    @Test
    void atualizarTransacao_DeveLancarExcecao_QuandoTransacaoNaoExistir() {
        // Arrange
        TransacaoDTO dto = new TransacaoDTO(20.0, OffsetDateTime.now());

        when(transacaoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecursoNaoEncontradoException.class, () -> transacaoService.atualizarTransacao(1L, dto));

        verify(transacaoRepository, times(1)).findById(1L);
        verify(transacaoRepository, never()).save(any());
    }
}
