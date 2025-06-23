package com.desafio.dev.itau.jr.repository;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desafio.dev.itau.jr.model.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByDataHoraAfter(OffsetDateTime dataHora);
}
