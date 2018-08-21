package com.vaz.projects.calculator.controller;

import com.vaz.projects.calculator.model.Model;
import com.vaz.projects.calculator.model.Operator;
import com.vaz.projects.calculator.view.ViewHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static com.vaz.projects.calculator.model.Operator.*;
import static com.vaz.projects.calculator.view.ViewHelper.resetView;

public class Controller {
    private static final Pattern NON_DIGIT = Pattern.compile("^0\\.|\\.");
    public static final int MAX_DIGIT_NUMBER = 16;

    @FXML
    private Text output;

    @FXML
    private Text superscript;

    private BigDecimal leftOperand = BigDecimal.ZERO;
    private BigDecimal rightOperand = BigDecimal.ZERO;
    private Operator lastOperator = Eq;
    private Operator mathOperator;
    private boolean newNumber = true;
    private boolean wasError;

    @FXML
    void processNumpad(final ActionEvent event) {
        if (wasError) {
            return;
        }

        final String outputText = output.getText();
        final int digitLength = NON_DIGIT.matcher(outputText).replaceFirst("").length();

        if (!newNumber && digitLength == MAX_DIGIT_NUMBER) {
            return;
        }

        final String value = getButtonText(event);

        if (!newNumber && ".".equals(value) && outputText.contains(".")) {
            return;
        }

        if (!newNumber && !"0".equals(outputText)) {
            setOutputText(outputText + value);
            return;
        }

        final String text = ".".equals(value) ? "0." : value;
        setOutputText(text);

        newNumber = false;
    }

    @FXML
    void processOperator(final ActionEvent event) {
        if (wasError) {
            return;
        }

        final Operator op = getOperator(event);

        if (Eq == op) {
            processEquals();
        } else if (Perc == op) {
            processPercent();
        } else if (Neg == op) {
            processNegate();
            lastOperator = Neg;
            return;
        } else {
            processMathOperator(op);
        }

        lastOperator = op;
        newNumber = true;
    }

    @FXML
    void processNumberTransform(final ActionEvent event) {
        if (wasError) {
            return;
        }

        lastOperator = getOperator(event);

        if (!newNumber) {
            final String existingOutput = output.getText();
            rightOperand = new BigDecimal(existingOutput);
        }

        try {
            rightOperand = new BigDecimal(Model.transform(rightOperand, lastOperator));
        } catch (final ArithmeticException e) {
            processError(e);
            return;
        }

        setOutputText(rightOperand);

        if (Neg != lastOperator) {
            newNumber = true;
        }
    }

    @FXML
    void processMemory(final ActionEvent event) {
        if (wasError) {
            return;
        }

        System.out.println("Should work with memory");
    }

    @FXML
    void processClear(final ActionEvent event) {
        final Operator op = getOperator(event);
        String currentText = output.getText();
        final int currentTextLenght = currentText.length();

        if (op == Bs && !wasError) {
            if (!newNumber && currentTextLenght > 1) {
                currentText = currentText.substring(0, currentTextLenght - 1);
                setOutputText(currentText);
            } else {
                reset();
            }
        }

        if (op == CE) {
            rightOperand = BigDecimal.ZERO;
            reset();
        }

        if (op == C) {
            leftOperand = BigDecimal.ZERO;
            rightOperand = BigDecimal.ZERO;
            mathOperator = null;
            lastOperator = Eq;
            reset();
        }
    }

    private void processEquals() {
        final String existingOutput = output.getText();

        if (!newNumber) {
            rightOperand = new BigDecimal(existingOutput);
        }

        if (mathOperator == null) {
            leftOperand = new BigDecimal(existingOutput);
            setOutputText(leftOperand);
            newNumber = true;
            return;
        }

        try {
            leftOperand = new BigDecimal(Model.calculate(leftOperand, rightOperand, mathOperator));
            setOutputText(leftOperand);
        } catch (final ArithmeticException e) {
            processError(e);
        }
    }

    private void processPercent() {
        final String percOperand = output.getText();
        rightOperand = new BigDecimal(Model.calculate(leftOperand, new BigDecimal(percOperand), Perc));
        setOutputText(rightOperand);
    }

    private void processNegate() {
        final String existingOutput = output.getText();
        final BigDecimal negatedOutput = new BigDecimal(existingOutput).negate();
        setOutputText(negatedOutput);
    }

    private void processMathOperator(final Operator op) {
        if (lastOperator.isMathOperator() && newNumber) {
            mathOperator = op;
            return;
        }

        if (!newNumber) {
            final String existingOutput = output.getText();
            rightOperand = new BigDecimal(existingOutput);
        }

        if (lastOperator == Eq && newNumber) {
            rightOperand = leftOperand;
        }

        leftOperand = (mathOperator == null || lastOperator == Eq || lastOperator.isTransformOperator())
                ? rightOperand
                : new BigDecimal(Model.calculate(leftOperand, rightOperand, mathOperator));

        mathOperator = op;
        setOutputText(leftOperand);
    }

    private void processError(final Exception e) {
        wasError = true;
        final String error = e.getMessage();
        ViewHelper.setOutputError(output, error);
    }

    private void setOutputText(final String value) {
        ViewHelper.setOutputText(output, value);
    }

    private void setOutputText(final BigDecimal value) {
        final String valueStr = ViewHelper.convertValueToStringRepresentation(value);
        setOutputText(valueStr);
    }

    private void reset() {
        wasError = false;
        newNumber = true;
        resetView(output);
    }

    private Operator getOperator(final ActionEvent event) {
        final String value = getButtonText(event);
        return fromString(value);
    }

    private String getButtonText(final ActionEvent event) {
        final Button pressedButton = (Button) event.getSource();
        return pressedButton.getText();
    }
}