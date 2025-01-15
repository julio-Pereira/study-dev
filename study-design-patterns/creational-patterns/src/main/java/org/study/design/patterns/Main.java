package org.study.design.patterns;

import org.study.design.patterns.statement.BankStatementFactory;
import org.study.design.patterns.statement.IStatement;
import org.study.design.patterns.transaction.Transaction;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Transaction> transactions = List.of(
                new Transaction("2025-01-01", "Salary", 1000),
                new Transaction("2025-01-02", "Rent", -500),
                new Transaction("2025-01-03", "Groceries", -100),
                new Transaction("2025-01-04", "Dinner", -50)
        );

        BankStatementFactory bankStatementFactory = new BankStatementFactory(transactions, 1000, 350);

        IStatement simpleStatement = bankStatementFactory.createStatement("simple");
        simpleStatement.generate();

        IStatement detailedStatement = bankStatementFactory.createStatement("detailed");
        detailedStatement.generate();

        IStatement personalizedStatement = bankStatementFactory.createStatement("personalized");
        personalizedStatement.generate();
    }
}