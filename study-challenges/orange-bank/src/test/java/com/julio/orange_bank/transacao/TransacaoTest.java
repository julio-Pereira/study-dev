package com.julio.orange_bank.transacao;

import org.junit.jupiter.api.Test;

public class TransacaoTest {

    @Test
    public void givenNewTransaction_whenDateIsInTheFuture_thenTransactionIsNotCreated() {
        // given
        // when
        // then
    }

    @Test
    public void givenNewTransaction_whenDateIsInThePast_thenTransactionIsCreated() {
        // given
        // when
        // then
    }

    @Test
    public void givenNewTransaction_whenValueIsNegative_thenTransactionIsNotCreated() {
        // given
        // when
        // then
    }

    @Test
    public void givenNewTransaction_whenValueIsPositive_thenTransactionIsCreated() {
        // given
        // when
        // then
    }

    @Test
    public void givenNewTransaction_whenValueIsZero_thenTransactionIsNotCreated() {
        // given
        // when
        // then
    }

    @Test
    public void givenNewTransaction_whenTransactionIsCreated_thenShouldReturnHttpStatusCode201() {
        // given
        // when
        // then
    }

    @Test
    public void givenNewTransactionRequest_whenTransactionIsNotAccepted_thenShouldReturnHttpStatusCode422() {
        // given
        // when
        // then
    }

    @Test
    public void givenAMalFormedRequest_whenJsonFileIsNotValid_thenShouldReturnHttpStatusCode400() {
        // given
        // when
        // then
    }
}
