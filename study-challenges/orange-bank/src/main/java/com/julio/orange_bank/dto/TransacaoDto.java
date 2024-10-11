package com.julio.orange_bank.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransacaoDto(BigDecimal valor, OffsetDateTime dataHora) {}
