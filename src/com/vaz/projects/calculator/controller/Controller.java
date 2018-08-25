package com.vaz.projects.calculator.controller;

import com.vaz.projects.calculator.model.Model;
import com.vaz.projects.calculator.model.Operator;
import com.vaz.projects.calculator.model.OperatorType;
import com.vaz.projects.calculator.view.ViewHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static com.vaz.projects.calculator.model.Operator.*;
import static com.vaz.projects.calculator.model.OperatorType.Clear;
import static com.vaz.projects.calculator.model.OperatorType.Negate;
import static com.vaz.projects.calculator.view.ViewHelper.resetView;

public class Controller {
    private static final Pattern NON_DIGIT = Pattern.compile("^0\\.|\\.");
    public static final int MAX_DIGIT_NUMBER = 16;

    private final Model model = new Model(this);

    @FXML
    private Text output;

    @FXML
    private Text superscript;

    private boolean newNumber = true;
    private boolean wasError;

    public boolean isNewNumber() {
        return newNumber;
    }

    public void processKeyEvent(final KeyEvent event) {
        final KeyCode code = event.getCode();
        final String keyText = event.getText();

        if ("%".equals(keyText) || (event.isShiftDown() && code == KeyCode.DIGIT5)) {
            processOperationEvent(new ActionEvent(new Button(Perc.getValue()), output));
            return;
        }

        if (code == KeyCode.AT || "@".equals(keyText) || (event.isShiftDown() && code == KeyCode.DIGIT2)) {
            processOperationEvent(new ActionEvent(new Button(Sqrt.getValue()), output));
            return;
        }

        if (code.isDigitKey() || code == KeyCode.DECIMAL) {
            processNumpad(new ActionEvent(new Button(keyText), output));
            return;
        }

        if (event.isShiftDown() && code == KeyCode.EQUALS) {
            processOperationEvent(new ActionEvent(new Button(Add.getValue()), output));
            return;
        }

        if (code == KeyCode.ADD || code == KeyCode.DIVIDE || code == KeyCode.SUBTRACT
                || code == KeyCode.MULTIPLY || code == KeyCode.EQUALS) {
            processOperationEvent(new ActionEvent(new Button(keyText), output));
            return;
        }

        if (code == KeyCode.ENTER) {
            processOperationEvent(new ActionEvent(new Button(Eq.getValue()), output));
            return;
        }

        if (code == KeyCode.F9) {
            processOperationEvent(new ActionEvent(new Button(Neg.getValue()), output));
            return;
        }

        if (code == KeyCode.R) {
            processOperationEvent(new ActionEvent(new Button(Inv.getValue()), output));
            return;
        }

        if (code == KeyCode.BACK_SPACE) {
            processClear(Bs);
            return;
        }

        if (code == KeyCode.DELETE) {
            processClear(CE);
            return;
        }

        if (code == KeyCode.ESCAPE) {
            processClear(C);
        }
    }

    @FXML
    void processOperationEvent(final ActionEvent event) {
        final Operator operator = getOperator(event);
        final OperatorType operatorType = operator.getType();

        if (wasError && operatorType != Clear) {
            return;
        }

        updateCurrentNumber();

        try {
            processOperation(operator);
            setOutputText(model.getCurrentNumber());
        } catch (final ArithmeticException e) {
            processError(e);
        }
    }

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

    private void processOperation(final Operator operator) {
        switch (operator.getType()) {
            case Equal:
                model.processEqual();
                break;
            case Math:
                model.processMathOperator(operator);
                break;
            case Transform:
                model.processNumberTransform(operator);
                break;
            case Percent:
                model.processPercent();
                break;
            case Negate:
                model.processNegate();
                break;
            case Memory:
                model.processMemory(operator);
                break;
            case Clear:
                processClear(operator);
                return;
        }

        if (operator.getType() != Negate) {
            newNumber = true;
        }
    }

    private void processClear(final Operator op) {
        String currentText = output.getText();
        final int currentTextLength = currentText.length();

        if (op == Bs && !wasError) {
            if (!newNumber && currentTextLength > 1) {
                currentText = currentText.substring(0, currentTextLength - 1);
                setOutputText(currentText);
            } else {
                reset(false);
            }
        }

        if (op == CE) {
            reset(false);
        }

        if (op == C) {
            reset(true);
        }
    }

    private void updateCurrentNumber() {
        if (!newNumber && !wasError) {
            final String existingOutput = output.getText();
            model.updateCurrentNumber(new BigDecimal(existingOutput));
        }
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
        if (wasError) {
            return;
        }

        final String valueStr = ViewHelper.convertValueToStringRepresentation(value);
        setOutputText(valueStr);
    }

    private void reset(final boolean full) {
        model.reset(full);
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