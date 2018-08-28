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
import static javafx.scene.input.KeyCode.*;

public class Controller {
    private static final Pattern NON_DIGIT = Pattern.compile("^0\\.|\\.");

    public static final int MAX_DIGIT_NUMBER = 16;

    private final Model model = new Model(this);

    @FXML
    private Text output;
    @FXML
    private Text memory;
    @FXML
    private Text superscript;
    @FXML
    private Text longSuperscript;

    private boolean newNumber = true;
    private boolean wasError;

    public boolean isNewNumber() {
        return newNumber;
    }

    public void setMemoryFlag(final boolean on) {
        memory.setVisible(on);
    }

    public void processKeyEvent(final KeyEvent event) {
        final KeyCode code = event.getCode();
        final String keyText = event.getText();

        if ("%".equals(keyText) || (event.isShiftDown() && code == DIGIT5)) {
            processOperationEventByKey(Perc.getValue());
            return;
        }

        if (code == AT || "@".equals(keyText) || (event.isShiftDown() && code == DIGIT2)) {
            processOperationEventByKey(Sqrt.getValue());
            return;
        }

        if (code.isDigitKey() || code == DECIMAL) {
            processNumpad(new ActionEvent(new Button(keyText), output));
            return;
        }

        if (event.isShiftDown() && code == EQUALS) {
            processOperationEventByKey(Add.getValue());
            return;
        }

        switch (code) {
            case ADD:
            case DIVIDE:
            case SUBTRACT:
            case MULTIPLY:
            case EQUALS:
                processOperationEventByKey(keyText);
                break;
            case ENTER:
                processOperationEventByKey(Eq.getValue());
                break;
            case F9:
                processOperationEventByKey(Neg.getValue());
                break;
            case R:
                processOperationEventByKey(Inv.getValue());
                break;
            case BACK_SPACE:
                processOperationEventByKey(Bs.getValue());
                break;
            case DELETE:
                processOperationEventByKey(CE.getValue());
                break;
            case ESCAPE:
                processOperationEventByKey(CL.getValue());
                break;
            default:
        }
    }

    @FXML
    void processOperationEvent(final ActionEvent event) {
        final Operator operator = getOperator(event);
        final OperatorType operatorType = operator.getType();

        if (wasError && operatorType != Clear) {
            return;
        }

        updateSuperscript(operator);
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
        superscript.setText(ViewHelper.updateSuperscript());
    }

    private void processOperationEventByKey(final String buttonText) {
        processOperationEvent(new ActionEvent(new Button(buttonText), output));
    }

    private void processOperation(final Operator operator) {
        switch (operator.getType()) {
            case Equal:
                model.processEqual();
                ViewHelper.clearSuperscript();
                superscript.setText("");
                break;
            case Math:
                model.processMathOperator(operator);
                break;
            case Transform:
                model.processNumberTransform(operator);
                break;
            case Percent:
                model.processPercent();
                final BigDecimal percent = model.getCurrentNumber();
                superscript.setText(ViewHelper.updateSuperscript(percent));
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
                model.updateCurrentNumber(new BigDecimal(currentText));
            } else {
                reset(false);
            }
        }

        if (op == CE) {
            reset(false);
        }

        if (op == CL) {
            reset(true);
        }
    }

    private void updateSuperscript(final Operator op) {
        final String currentNumber = output.getText();
        final String result = ViewHelper.updateSuperscript(op, currentNumber, newNumber, longSuperscript);
        superscript.setText(result);
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
        if (full || wasError) {
            ViewHelper.clearSuperscript();
            superscript.setText("");
        }

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