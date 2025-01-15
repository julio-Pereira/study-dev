package org.study.design.patterns.statement;

import org.study.design.patterns.transaction.Transaction;

import java.util.List;

public class DetailedStatement implements IStatement {

    private List<Transaction> transactions;
    private double previousBalance;
    private double newBalance;

    public DetailedStatement(List<Transaction> transactions, double previousBalance, double newBalance) {
        this.transactions = transactions;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
    }

    @Override
    public void generate() {
        System.out.println("--- Detailed Statement ---");
        System.out.println("Previous Balance: " + previousBalance);
        System.out.println("Date | Description | Amount");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getDate() + " | " + transaction.getDescription() + " | " + transaction.getAmount());
        }
        System.out.println("New Balance: " + newBalance);
    }
}
