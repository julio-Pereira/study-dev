package com.julio.orange_bank.controller;

import com.julio.orange_bank.records.Transacao;
import com.julio.orange_bank.service.TransacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private TransacaoService transacaoService;
    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public void createNewTransaction() {

    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Transacao> getTransactions() {
        return transacaoService.getTransactions();
    }

}
