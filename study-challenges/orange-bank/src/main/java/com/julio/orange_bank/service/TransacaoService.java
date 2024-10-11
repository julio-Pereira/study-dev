package com.julio.orange_bank.service;

import com.julio.orange_bank.mapper.TransacaoMapper;
import com.julio.orange_bank.model.Transacao;
import com.julio.orange_bank.dto.TransacaoDto;
import com.julio.orange_bank.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final TransacaoMapper mapper;

    public TransacaoService(TransacaoRepository transacaoRepository, TransacaoMapper mapper) {
        this.transacaoRepository = transacaoRepository;
        this.mapper = mapper;
    }

    public Transacao createNewTransaction(TransacaoDto transacaoDto) {
        Transacao transacao = mapper.toEntity(transacaoDto);

        validateTransacao(transacao);

       return transacaoRepository.addTransacao(transacao);
    }

    public List<Transacao> deleteTransacoes() {
        return transacaoRepository.deleteTransacoes();
    }

    public List<Transacao> getTransactions() {
        List<Transacao> transacoesDto = new ArrayList<>();
        List<Transacao> transacoes = transacaoRepository.getTransacoes();
        for (Transacao transacao : transacoes) {
            mapper.toDto(transacao);
            transacoesDto.add(transacao);
        }
        return transacoesDto;
    }

    private void validateTransacao(Transacao transacao) {
        if (transacao.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor da transação não pode ser negativo");
        }

        if (transacao.getDataHora() == null) {
            throw new IllegalArgumentException("Data e hora da transação não pode ser nula");
        }

        if (transacao.getDataHora().isEqual(OffsetDateTime.now()) || transacao.getDataHora().isAfter(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Data e hora da transação não pode ser no futuro");
        }
    }

}
