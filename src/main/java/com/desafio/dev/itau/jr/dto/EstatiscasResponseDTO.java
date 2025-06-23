package com.desafio.dev.itau.jr.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record EstatiscasResponseDTO(
    Long count,

    @Schema(example = "0.0")
    Double sum,

    @Schema(example = "0.0")
    Double avg,

    @Schema(example = "0.0")
    Double min,

    @Schema(example = "0.0")
    Double max
) {}
