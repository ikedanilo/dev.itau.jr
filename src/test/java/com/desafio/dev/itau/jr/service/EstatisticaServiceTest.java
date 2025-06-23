package com.desafio.dev.itau.jr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.desafio.dev.itau.jr.dto.EstatiscasResponseDTO;
import com.desafio.dev.itau.jr.dto.TransacaoResponseDTO;


class EstatisticaServiceTest {
    
    @Mock
    private TransacaoService transacaoService;

    @InjectMocks
    private EstatiscasService estatisticasService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calcularEstatisticasTransacoes_DeveRetornarEstatisticasCorretas_QuandoExistiremTransacoes() {
        // Arrange
        List<TransacaoResponseDTO> transacoes = List.of(
            new TransacaoResponseDTO(1L, 10.0, OffsetDateTime.now()),
            new TransacaoResponseDTO(2L, 20.0, OffsetDateTime.now()),
            new TransacaoResponseDTO(3L, 30.0, OffsetDateTime.now())
        );

        when(transacaoService.listarTransacoes(anyInt())).thenReturn(transacoes);

        // Act
        EstatiscasResponseDTO resultado = estatisticasService.calcularEstatisticasTransacoes(10);

        // Assert
        assertEquals(3L, resultado.count());
        assertEquals(60.0, resultado.sum());
        assertEquals(20.0, resultado.avg());
        assertEquals(10.0, resultado.min());
        assertEquals(30.0, resultado.max());

        verify(transacaoService, times(1)).listarTransacoes(10);
    }

    @Test
    void calcularEstatisticasTransacoes_DeveRetornarEstatisticasZeradas_QuandoNaoHouverTransacoes() {
        // Arrange
        when(transacaoService.listarTransacoes(anyInt())).thenReturn(List.of());

        // Act
        EstatiscasResponseDTO resultado = estatisticasService.calcularEstatisticasTransacoes(10);

        // Assert
        assertEquals(0L, resultado.count());
        assertEquals(0.0, resultado.sum());
        assertEquals(0.0, resultado.avg());
        assertEquals(0.0, resultado.min());
        assertEquals(0.0, resultado.max());

        verify(transacaoService, times(1)).listarTransacoes(10);
    }

}
