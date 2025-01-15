package org.study.design.patterns.statement;

public interface StatementFactory {

    IStatement createStatement(String type);
}
