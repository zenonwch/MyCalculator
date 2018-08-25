package com.vaz.projects.calculator.model;

import java.util.Arrays;
import java.util.function.Predicate;

import static com.vaz.projects.calculator.model.OperatorType.*;
import static com.vaz.projects.calculator.model.OperatorType.Math;

public enum Operator {
    Add("+", Math),
    Subtr("-", Math),
    Div("/", Math),
    Mult("*", Math),

    Eq("=", Equal),

    Bs("⬅", Clear),
    C("C", Clear),
    CE("CE", Clear),

    Perc("%", Percent),
    Neg("±", Negate),

    Inv("1/x", Transform),
    Sqrt("√", Transform),

    M_Clear("MC", Memory),
    M_Retrieve("MR", Memory),
    M_Store("MS", Memory),
    M_Add("M+", Memory),
    M_Subtr("M-", Memory),
    ;

    private final String value;
    private final OperatorType type;

    Operator(final String value, final OperatorType type) {
        this.value = value;
        this.type = type;
    }

    public static Operator fromString(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("Empty operator value");
        }

        final Predicate<Operator> byStringValue
                = v -> v.getValue().equalsIgnoreCase(value);

        return Arrays.stream(values())
                .filter(byStringValue)
                .findFirst()
                .orElse(Eq);
    }

    public String getValue() {
        return value;
    }

    public OperatorType getType() {
        return type;
    }
}
