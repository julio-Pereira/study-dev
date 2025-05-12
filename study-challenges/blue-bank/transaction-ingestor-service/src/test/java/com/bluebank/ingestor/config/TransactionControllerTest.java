package com.bluebank.ingestor.config;

import com.bluebank.common.dto.TransactionRequest;
import com.bluebank.common.model.Currency;
import com.bluebank.common.model.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testReceiveValidTransaction() throws Exception {
        // Arrange
        TransactionRequest request = TransactionRequest.builder()
                .accountId("test-account")
                .amount(new BigDecimal("100.50"))
                .currency(Currency.BRL)
                .type(TransactionType.CREDIT_CARD)
                .merchantName("Test Merchant")
                .merchantCategory("Retail")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)));
    }
}