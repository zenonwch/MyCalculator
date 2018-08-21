package com.vaz.projects.calculator.model;

import java.math.BigDecimal;
import java.math.MathContext;

import static com.vaz.projects.calculator.model.BigDecimalHelper.sqrt;
import static com.vaz.projects.calculator.model.Operator.Div;

public final class Model {
    public static final String ERR_UNDEFINED = "Result is undefined";
    public static final String ERR_DIV_BY_ZERO = "Cannot divide by zero";

    private Model() {
    }

    public static String calculate(final BigDecimal n1, final BigDecimal n2, final Operator operator) {

        final BigDecimal result;

        switch (operator) {
            case Add:
                result = n1.add(n2);
                break;
            case Subst:
                result = n1.subtract(n2);
                break;
            case Mult:
                result = n1.multiply(n2);
                break;
            case Div:
                checkDivisionByZero(n1, n2);
                result = n1.divide(n2, MathContext.DECIMAL128);
                break;
            case Perc:
                result = n1.multiply(n2).divide(new BigDecimal("100"), MathContext.DECIMAL128);
                break;
            default:
                throw new IllegalArgumentException("Calculate method. Incorrect operator: " + operator);
        }

        return result.stripTrailingZeros().toPlainString();
    }

    public static String transform(final BigDecimal value, final Operator operator) {

        switch (operator) {
            case Inv:
                return calculate(BigDecimal.ONE, value, Div);
            case Sqrt:
                if (BigDecimal.ZERO.compareTo(value) > 0) {
                    throw new ArithmeticException("Invalid input");
                }
                return sqrt(value, new MathContext(100)).stripTrailingZeros().toPlainString();
            default:
                throw new IllegalArgumentException("Transform method. Incorrect operator: " + operator);
        }
    }


    private static void checkDivisionByZero(final BigDecimal n1, final BigDecimal n2) {
        if (BigDecimal.ZERO.compareTo(n2) != 0) {
            return;
        }

        if (BigDecimal.ZERO.compareTo(n1) == 0) {
            throw new ArithmeticException(ERR_UNDEFINED);
        }

        throw new ArithmeticException(ERR_DIV_BY_ZERO);
    }
}
