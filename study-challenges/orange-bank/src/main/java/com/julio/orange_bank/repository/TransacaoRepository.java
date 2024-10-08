package com.julio.orange_bank.repository;

import com.julio.orange_bank.records.Transacao;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransacaoRepository {



    public List<Transacao> getTransacoes() {
        List<Transacao> transacoesList = new ArrayList<>();
        transacoesList.add(new Transacao("199,99", OffsetDateTime.now()));

        return transacoesList;
    }
}
