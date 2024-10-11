package com.julio.orange_bank.mapper;

import com.julio.orange_bank.dto.TransacaoDto;
import com.julio.orange_bank.model.Transacao;
import org.springframework.stereotype.Component;

@Component
public class TransacaoMapper {

    public TransacaoDto toDto(Transacao transacao) {
        if (transacao == null) {
            return null;
        }

        return new TransacaoDto(transacao.getValor(), transacao.getDataHora());
    }

    public Transacao toEntity(TransacaoDto transacaoDto) {

        if (transacaoDto == null) {
            return null;
        }

        return new Transacao(transacaoDto.valor(), transacaoDto.dataHora());
    }
}
