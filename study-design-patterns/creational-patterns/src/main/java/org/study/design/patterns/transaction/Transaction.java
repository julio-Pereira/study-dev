package org.study.design.patterns.transaction;

public class Transaction implements ITransaction {
    private String date;
    private String description;
    private double amount;

    public Transaction(String date, String description, double amount) {
        this.date = date;
        this.description = description;
        this.amount = amount;
    }


    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getAmount() {
        return amount;
    }
}
