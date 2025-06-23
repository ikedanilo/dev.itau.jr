package com.desafio.dev.itau.jr.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.desafio.dev.itau.jr.dto.EstatiscasResponseDTO;
import com.desafio.dev.itau.jr.service.EstatiscasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EstatisticasController.class)
class EstatisticasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstatiscasService estatisticasService;

    @Test
    void buscarEstatistica_DeveRetornarEstatisticasComStatus200() throws Exception {
        // Arrange
        EstatiscasResponseDTO estatisticasMock = new EstatiscasResponseDTO(3L, 60.0, 20.0, 10.0, 30.0);

        when(estatisticasService.calcularEstatisticasTransacoes(60)).thenReturn(estatisticasMock);

        // Act & Assert
        mockMvc.perform(get("/estatistica")
                .param("intervalorBusca", "60")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(3))
            .andExpect(jsonPath("$.sum").value(60.0))
            .andExpect(jsonPath("$.avg").value(20.0))
            .andExpect(jsonPath("$.min").value(10.0))
            .andExpect(jsonPath("$.max").value(30.0));
    }
}
