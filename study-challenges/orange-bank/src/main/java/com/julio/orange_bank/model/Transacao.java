package com.julio.orange_bank.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.OffsetDateTime;

public class Transacao implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal valor;
    private OffsetDateTime dataHora;

    public Transacao(BigDecimal valor, OffsetDateTime dataHora) {
       this.valor = valor;
       this.dataHora = dataHora;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
