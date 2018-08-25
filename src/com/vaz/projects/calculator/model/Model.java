package com.vaz.projects.calculator.model;

import com.vaz.projects.calculator.controller.Controller;

import java.math.BigDecimal;
import java.math.MathContext;

import static com.vaz.projects.calculator.model.BigDecimalHelper.sqrt;
import static com.vaz.projects.calculator.model.Operator.*;
import static com.vaz.projects.calculator.model.OperatorType.Math;

public class Model {
    public static final String ERR_UNDEFINED = "Result is undefined";
    public static final String ERR_DIV_BY_ZERO = "Cannot divide by zero";

    private final Controller controller;

    private BigDecimal leftOperand = BigDecimal.ZERO;
    private BigDecimal rightOperand = BigDecimal.ZERO;
    private BigDecimal currentNumber = BigDecimal.ZERO;
    private Operator lastOperator = Eq;
    private Operator prevMathOperator;

    public Model(final Controller controller) {
        this.controller = controller;
    }

    public void processEqual() {
        if (prevMathOperator == null) {
            leftOperand = currentNumber;
            return;
        }

        if (lastOperator == Eq) {
            leftOperand = currentNumber;
        } else {
            rightOperand = currentNumber;
        }

        final String result = calculate(leftOperand, rightOperand, prevMathOperator);
        leftOperand = new BigDecimal(result);
        currentNumber = leftOperand;

        lastOperator = Eq;
    }

    public void processMathOperator(final Operator op) {
        if (lastOperator.getType() == Math && controller.isNewNumber()) {
            prevMathOperator = op;
            return;
        }

        rightOperand = lastOperator == Eq && controller.isNewNumber()
                ? leftOperand
                : currentNumber;

        if (lastOperator == Eq || prevMathOperator == null) {
            leftOperand = currentNumber;
        } else {
            final String result = calculate(leftOperand, rightOperand, prevMathOperator);
            leftOperand = new BigDecimal(result);
        }

        currentNumber = leftOperand;
        prevMathOperator = op;
        lastOperator = prevMathOperator;
    }

    public void processPercent() {
        final String result = calculate(leftOperand, currentNumber, Perc);
        currentNumber = new BigDecimal(result);
        lastOperator = Perc;
    }

    public void processNegate() {
        currentNumber = currentNumber.negate();
    }

    public void processNumberTransform(final Operator op) {
        final String result = transform(currentNumber, op);
        currentNumber = new BigDecimal(result);
    }

    public void processMemory(final Operator op) {
        System.out.println("Should work with memory");
    }

    public void reset(final boolean full) {
        rightOperand = BigDecimal.ZERO;
        currentNumber = BigDecimal.ZERO;

        if (full) {
            leftOperand = BigDecimal.ZERO;
            prevMathOperator = null;
            lastOperator = Eq;
        }
    }

    public BigDecimal getCurrentNumber() {
        return currentNumber;
    }

    public void updateCurrentNumber(final BigDecimal currentNumber) {
        this.currentNumber = currentNumber;
    }


    private String calculate(final BigDecimal n1, final BigDecimal n2, final Operator operator) {

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

    private String transform(final BigDecimal value, final Operator operator) {
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

    private void checkDivisionByZero(final BigDecimal n1, final BigDecimal n2) {
        if (BigDecimal.ZERO.compareTo(n2) != 0) {
            return;
        }

        if (BigDecimal.ZERO.compareTo(n1) == 0) {
            throw new ArithmeticException(ERR_UNDEFINED);
        }

        throw new ArithmeticException(ERR_DIV_BY_ZERO);
    }
}
