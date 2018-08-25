package com.vaz.projects.calculator.view;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vaz.projects.calculator.controller.Controller.MAX_DIGIT_NUMBER;

public final class ViewHelper {
    private static final int DIGIT_THRESHOLD = 12;
    private static final int STRING_THRESHOLD = 16;
    private static final Font ERROR_FONT = new Font(16);
    private static final Font EXTRA_SMALL_FONT = new Font(22);
    private static final Font SMALL_FONT = new Font(28);
    private static final Font BIG_FONT = new Font(36);

    private static final Pattern POSITIVE_SCIENTIFIC_NOTATION = Pattern.compile("E", Pattern.LITERAL);

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

    public static void resetView(final Text output) {
        setOutputText(output, "0");
    }
}
