package org.study.design.patterns.statement;

import org.study.design.patterns.transaction.Transaction;

import java.util.List;

public class PersonalizedStatement implements IStatement {
    private List<Transaction> transactions;

    private List<String> columns;

    public PersonalizedStatement(List<Transaction> transactions, List<String> columns) {
        this.transactions = transactions;
        this.columns = columns;
    }


    @Override
    public void generate() {
        System.out.println("--- Personalized Statement ---");
        String header = String.join(" | ", columns);
        System.out.println(header);

        for (Transaction transaction : transactions) {
            String row = "";
            for (String column : columns) {
                switch (column) {
                    case "Date":
                        row += transaction.getDate();
                        break;
                    case "Description":
                        row += transaction.getDescription();
                        break;
                    case "Amount":
                        row += transaction.getAmount();
                        break;
                }
                row += " | ";
            }
            System.out.println(row);
        }
    }

}
