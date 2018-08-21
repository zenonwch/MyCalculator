package com.vaz.projects.calculator.model;

import java.util.Arrays;
import java.util.function.Predicate;

@SuppressWarnings("MethodDoesntCallSuperMethod")
public enum Operator {
    Add("+") {
        @Override
        public boolean isMathOperator() {
            return true;
        }
    },
    Subst("-") {
        @Override
        public boolean isMathOperator() {
            return true;
        }
    },
    Div("/") {
        @Override
        public boolean isMathOperator() {
            return true;
        }
    },
    Mult("*") {
        @Override
        public boolean isMathOperator() {
            return true;
        }
    },

    Eq("="),

    Bs("⬅"),
    C("C"),
    CE("CE"),

    Perc("%"),

    Inv("1/x") {
        @Override
        public boolean isTransformOperator() {
            return true;
        }
    },
    Neg("±") {
        @Override
        public boolean isTransformOperator() {
            return true;
        }
    },
    Sqrt("√") {
        @Override
        public boolean isTransformOperator() {
            return true;
        }
    },
    ;

    private final String value;

    Operator(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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

    public boolean isMathOperator() {
        return false;
    }

    public boolean isTransformOperator() {
        return false;
    }
}
