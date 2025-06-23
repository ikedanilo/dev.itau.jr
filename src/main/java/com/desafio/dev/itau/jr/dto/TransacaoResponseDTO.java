package com.desafio.dev.itau.jr.dto;

import java.time.OffsetDateTime;

public record TransacaoResponseDTO(
    Long id,
    Double valor,
    OffsetDateTime dataHora
) {}
