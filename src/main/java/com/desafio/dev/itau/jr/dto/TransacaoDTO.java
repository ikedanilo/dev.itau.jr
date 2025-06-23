package com.desafio.dev.itau.jr.dto;

import java.time.OffsetDateTime;
import jakarta.validation.constraints.NotNull;


public record TransacaoDTO (
    @NotNull(message = "O valor não pode ser nulo")
    Double valor,

    @NotNull(message = "Data hora não pode ser nulo")
    OffsetDateTime dataHora
    ) {}
