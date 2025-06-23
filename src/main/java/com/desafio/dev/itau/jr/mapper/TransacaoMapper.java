package com.desafio.dev.itau.jr.mapper;

import com.desafio.dev.itau.jr.dto.TransacaoResponseDTO;
import com.desafio.dev.itau.jr.model.Transacao;

public class TransacaoMapper {

    private TransacaoMapper() {}

    public static TransacaoResponseDTO toDTO(Transacao transacao) {
       
        return new TransacaoResponseDTO(
            transacao.getId(),
            transacao.getValor(),
            transacao.getDataHora()
        );
    }

}
