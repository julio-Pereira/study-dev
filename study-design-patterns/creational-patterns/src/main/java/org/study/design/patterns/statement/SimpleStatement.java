package org.study.design.patterns.statement;

import org.study.design.patterns.transaction.Transaction;

import java.util.List;

public class SimpleStatement implements IStatement {

    private List<Transaction> transactions;

    public SimpleStatement(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void generate() {
        System.out.println("--- Simple Statement ---");
        System.out.println("Date | Description | Amount");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getDate() + " | " + transaction.getDescription() + " | " + transaction.getAmount());
        }
    }

}
