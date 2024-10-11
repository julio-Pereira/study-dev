package com.julio.orange_bank.controller;

import com.julio.orange_bank.model.Transacao;
import com.julio.orange_bank.dto.TransacaoDto;
import com.julio.orange_bank.service.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private TransacaoService transacaoService;


    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransacaoDto> createNewTransaction(@RequestBody TransacaoDto transacao) {
           try {
               transacaoService.createNewTransaction(transacao);
               return ResponseEntity.created(null).build();
           } catch (IllegalArgumentException exception) {
               return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
           } catch (Exception exception) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
           }

    }

    @DeleteMapping
    public ResponseEntity deleteTransacoes() {
        transacaoService.deleteTransacoes();
        return ResponseEntity.ok(null);
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Transacao> getTransactions() {
        return transacaoService.getTransactions();
    }

}
