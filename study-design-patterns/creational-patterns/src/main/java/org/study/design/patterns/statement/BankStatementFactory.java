package org.study.design.patterns.statement;

import org.study.design.patterns.transaction.Transaction;

import java.util.List;

public class BankStatementFactory implements StatementFactory{

    private List<Transaction> transactions;
    private double previousBalance;
    private double newBalance;

    public BankStatementFactory(List<Transaction> transactions, double previousBalance, double newBalance) {
        this.transactions = transactions;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
    }

    @Override
    public IStatement createStatement(String type) {
        if (type.equalsIgnoreCase("simple")) {
            return new SimpleStatement(transactions);
    } else if (type.equalsIgnoreCase("detailed")) {
            return new DetailedStatement(transactions, previousBalance, newBalance);
        } else if (type.equalsIgnoreCase("personalized")) {
            return new PersonalizedStatement(transactions, List.of("Date", "Amount"));
        }
        throw new IllegalArgumentException("Invalid statement type: " + type);
    }
}
