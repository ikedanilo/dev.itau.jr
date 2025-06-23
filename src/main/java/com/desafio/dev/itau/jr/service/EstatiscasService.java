package com.desafio.dev.itau.jr.service;


import java.util.DoubleSummaryStatistics;
import java.util.List;
import org.springframework.stereotype.Service;
import com.desafio.dev.itau.jr.dto.EstatiscasResponseDTO;
import com.desafio.dev.itau.jr.dto.TransacaoResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstatiscasService {

    private final TransacaoService transacaoService;

    // Método
    public EstatiscasResponseDTO calcularEstatisticasTransacoes(Integer intervalorBusca) {

        log.info("Iniciada busca de estatísticas de transações");
        long inicio = System.currentTimeMillis();

        List<TransacaoResponseDTO> transacoes = transacaoService.listarTransacoes(intervalorBusca);

        DoubleSummaryStatistics estatisticasTransacoes = transacoes.stream()
            .mapToDouble(TransacaoResponseDTO::valor).summaryStatistics();

        if (transacoes.isEmpty()) {
            log.info("Nenhuma transação encontrada. Retornando estatísticas zeradas.");
            return new EstatiscasResponseDTO(0L, 0.0, 0.0, 0.0, 0.0);
        }
        
        log.info("Estatísticas retornadas com sucesso");
        long fim = System.currentTimeMillis();
        log.info("Tempo de execução: " + (fim - inicio) + "ms");

        return new EstatiscasResponseDTO(
            estatisticasTransacoes.getCount(),
            estatisticasTransacoes.getSum(),
            estatisticasTransacoes.getAverage(),
            estatisticasTransacoes.getMin(),
            estatisticasTransacoes.getMax()
        );

    }
}
