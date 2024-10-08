package com.julio.orange_bank.service;

import com.julio.orange_bank.model.TransacaoModel;
import com.julio.orange_bank.records.Transacao;
import com.julio.orange_bank.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {

    private TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }


    public List<Transacao> getTransactions() {
        return transacaoRepository.getTransacoes();
    }

}
