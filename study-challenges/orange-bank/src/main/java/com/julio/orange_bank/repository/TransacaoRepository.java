package com.julio.orange_bank.repository;

import com.julio.orange_bank.model.Transacao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransacaoRepository {

    List<Transacao> transacoesList = new ArrayList<>();

    public Transacao addTransacao(Transacao transacao) {
        transacoesList.add(transacao);
        return transacao;
    }

    public List<Transacao> deleteTransacoes() {
        transacoesList.clear();
        return transacoesList;
    }


    public List<Transacao> getTransacoes() {
        return transacoesList;
    }
}
