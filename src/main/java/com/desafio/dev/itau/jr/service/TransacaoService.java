package com.desafio.dev.itau.jr.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.desafio.dev.itau.jr.dto.TransacaoDTO;
import com.desafio.dev.itau.jr.dto.TransacaoResponseDTO;
import com.desafio.dev.itau.jr.exceptions.RecursoNaoEncontradoException;
import com.desafio.dev.itau.jr.exceptions.UnprocessableEntity;
import com.desafio.dev.itau.jr.mapper.TransacaoMapper;
import com.desafio.dev.itau.jr.model.Transacao;
import com.desafio.dev.itau.jr.repository.TransacaoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransacaoService {

    // Injeção de dependência
    private final TransacaoRepository transacaoRepository;

    // Construtor
    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    // Métodos
    public Transacao salvarTransacao(TransacaoDTO dto) {
        log.info("Iniciado o processamento de gravar transações " + dto);

        if (dto.dataHora().isAfter(OffsetDateTime.now())) {
            log.error("Data e hora maiores que a data atual");
            throw new UnprocessableEntity("Data e hora maiores que a data e hora atuais");
        }

        if (dto.valor() < 0) {
            log.error("Valor não pode ser menor que zero");
            throw new UnprocessableEntity("Valor não pode ser menor que zero");
        }

        Transacao transacao = new Transacao();
        transacao.setValor(dto.valor());
        transacao.setDataHora(dto.dataHora());

        log.info("Transações adicionadas com sucesso");
        return transacaoRepository.save(transacao);
    }

    public List<TransacaoResponseDTO> listarTransacoes(Integer intervaloBusca) {

        OffsetDateTime dataHoraIntervalo = OffsetDateTime.now().minusSeconds(intervaloBusca);

        return transacaoRepository.findByDataHoraAfter(dataHoraIntervalo)
            .stream()
            .map(TransacaoMapper::toDTO)
            .toList();
    }

    public TransacaoResponseDTO listarTransacao(Long id) {
        Optional<Transacao> transacaoOpt = transacaoRepository.findById(id);

        return transacaoOpt
                .map(TransacaoMapper::toDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada com ID: " + id));
    }

    public List<TransacaoResponseDTO> listarTudo() {
        return transacaoRepository.findAll().stream().map(TransacaoMapper::toDTO).toList();
    }

    public void limparTransacoes() {
        log.info("Iniciado processamento para deletar transações");
        transacaoRepository.deleteAll();
        log.info("Transações deletadas com sucesso");
    }

    public Transacao atualizarTransacao(Long id, TransacaoDTO dto) {
        Transacao transacaoExistente = transacaoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada com ID: " + id));

        transacaoExistente.setValor(dto.valor());
        transacaoExistente.setDataHora(dto.dataHora());

        return transacaoRepository.save(transacaoExistente);
    }
}
