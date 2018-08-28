package com.vaz.projects.calculator.view;

import com.vaz.projects.calculator.model.Operator;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vaz.projects.calculator.controller.Controller.MAX_DIGIT_NUMBER;
import static com.vaz.projects.calculator.model.Operator.*;
import static com.vaz.projects.calculator.model.OperatorType.*;
import static com.vaz.projects.calculator.model.OperatorType.Math;

public final class ViewHelper {
    private static final Pattern ENDS_WITH_MATH_SIGN = Pattern.compile("[+\\-/*]$");
    private static final Pattern POSITIVE_SCIENTIFIC_NOTATION = Pattern.compile("E", Pattern.LITERAL);

    private static final String DEFAULT_OUTPUT_TEXT = "0";
    private static final String DEFAULT_SUPERSCRIPT_TEXT = "";

    private static final int MAX_SUPERSCRIPT_LENGTH = 26;
    private static final int DIGIT_THRESHOLD = 12;
    private static final int STRING_THRESHOLD = 16;

    private static final Font ERROR_FONT = Font.font(16);
    private static final Font EXTRA_SMALL_FONT = Font.font(22);
    private static final Font SMALL_FONT = Font.font(28);
    private static final Font BIG_FONT = Font.font(36);

    private static final Deque<String> history = new LinkedList<>();

    private static String currentSuperscriptText = DEFAULT_SUPERSCRIPT_TEXT;

    private ViewHelper() {
    }

    public static void setOutputText(final Text output, final String value) {
        output.setFont(BIG_FONT);
        StackPane.setMargin(output, new Insets(0, 0, 0, 0));

        if (value.length() > DIGIT_THRESHOLD) {
            output.setFont(SMALL_FONT);
            StackPane.setMargin(output, new Insets(0, 0, 2, 0));
        }

        if (value.length() > STRING_THRESHOLD) {
            output.setFont(EXTRA_SMALL_FONT);
            StackPane.setMargin(output, new Insets(0, 0, 4, 0));
        }

        output.setText(value);
    }

    public static void setOutputError(final Text output, final String error) {
        output.setFont(ERROR_FONT);
        StackPane.setMargin(output, new Insets(0, 0, 6, 0));
        output.setText(error);
    }

    public static String convertValueToStringRepresentation(final BigDecimal value) {
        if (value.precision() <= MAX_DIGIT_NUMBER && value.scale() <= MAX_DIGIT_NUMBER) {
            return value.toPlainString();
        }

        final MathContext mc = new MathContext(MAX_DIGIT_NUMBER, RoundingMode.HALF_UP);
        String resultStr = new BigDecimal(String.valueOf(value), mc).stripTrailingZeros().toString();

        if (resultStr.startsWith(".")) {
            resultStr = '0' + resultStr;
        }

        if (resultStr.contains("E") && !resultStr.contains(".")) {
            return POSITIVE_SCIENTIFIC_NOTATION.matcher(resultStr).replaceAll(".e");
        }

        if (resultStr.contains("E")) {
            return POSITIVE_SCIENTIFIC_NOTATION.matcher(resultStr)
                    .replaceAll(Matcher.quoteReplacement("e"));
        }

        return resultStr;
    }

    public static String updateSuperscript(final Operator op, final String currentNumber,
                                           final boolean isNew, final Text longSuperscript) {
        String result = calculateSuperscript(op, currentNumber, isNew);
        final int resLength = currentSuperscriptText.length();

        if (resLength > MAX_SUPERSCRIPT_LENGTH) {
            longSuperscript.setVisible(true);
            result = result.substring(resLength - MAX_SUPERSCRIPT_LENGTH + 1);
        } else {
            longSuperscript.setVisible(false);
        }

        return result;
    }

    public static String updateSuperscript() {
        if (history.isEmpty()) {
            return currentSuperscriptText;
        }

        final String lastElement = history.getLast();
        final boolean isLastMathOperator = ENDS_WITH_MATH_SIGN.matcher(lastElement).matches();

        if (!isLastMathOperator) {
            history.removeLast();
        }

        return convertHistoryToString();
    }


    public static String updateSuperscript(final BigDecimal percent) {
        final String newValue = convertValueToStringRepresentation(percent);

        if (history.isEmpty()) {
            history.addLast(newValue);
        } else {
            replaceLast("%s", newValue);
        }

        return convertHistoryToString();
    }

    public static void clearSuperscript() {
        history.clear();
        currentSuperscriptText = DEFAULT_SUPERSCRIPT_TEXT;
    }

    private static String calculateSuperscript(final Operator op,
                                               final String currentNumber, final boolean isNew) {

        if (op == Eq || op == CL || (!isNew && op == Neg)
                || op.getType() == Memory || op.getType() == Clear) {
            return currentSuperscriptText;
        }

        if (!isNew || history.isEmpty()) {
            history.add(currentNumber);
        }

        String lastElement = history.getLast();
        final boolean isLastMathOperator = ENDS_WITH_MATH_SIGN.matcher(lastElement).matches();

        if (op.getType() == Math && isLastMathOperator && isNew) {
            history.removeLast();
        }

        if (op.getType() == Math) {
            history.addLast(op.getValue());
        }

        if (op.getType() != Math && isLastMathOperator && isNew) {
            history.addLast(currentNumber);
            lastElement = currentNumber;
        }

        if (op == Inv) {
            replaceLast("reciproc(%s)", lastElement);
        }

        if (op == Sqrt) {
            replaceLast("sqrt(%s)", lastElement);
        }

        if (op == Neg) {
            replaceLast("negate(%s)", lastElement);
        }

        return convertHistoryToString();
    }

    public static void resetView(final Text output) {
        setOutputText(output, DEFAULT_OUTPUT_TEXT);
    }

    private static String convertHistoryToString() {
        currentSuperscriptText = String.join(" ", history);
        return currentSuperscriptText;
    }

    private static void replaceLast(final String pattern, final String lastElement) {
        history.removeLast();
        history.addLast(String.format(pattern, lastElement));
    }
}
