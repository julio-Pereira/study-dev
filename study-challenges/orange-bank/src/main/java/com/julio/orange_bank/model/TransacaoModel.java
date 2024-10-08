package com.julio.orange_bank.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.OffsetDateTime;

public class TransacaoModel extends DecimalFormat {

    private static final long serialVersionUID = 1L;
    private DecimalFormatSymbols symbols;
    private String stringValue;
    private double valor;
    private OffsetDateTime dataHora;

    public TransacaoModel(double valor) {
        super("#.##");
        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        setDecimalFormatSymbols(symbols);
        this.setStringValue(format(valor));
    }

    public TransacaoModel(double valor, OffsetDateTime dataHora) {
        super("#.##");
        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        setDecimalFormatSymbols(symbols);
        this.setStringValue(format(valor));
        this.setDataHora(dataHora);
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
