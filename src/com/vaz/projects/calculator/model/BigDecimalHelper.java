package com.vaz.projects.calculator.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.BigDecimal.*;

final class BigDecimalHelper {
    private static final BigDecimal TWO = valueOf(2);
    private static final int EXPECTED_INITIAL_PRECISION = 17;

    private BigDecimalHelper() {
    }

    /**
     * Calculates the square root of {@link BigDecimal} x.
     *
     * <p>See <a href="http://en.wikipedia.org/wiki/Square_root">Wikipedia: Square root</a></p>
     * <p>See <a href="https://github.com/eobermuhlner/big-math">GitHub: eobermuhlner/big-math</a></p>
     *
     * @param x           the {@link BigDecimal} value to calculate the square root
     * @param mathContext the {@link MathContext} used for the result
     * @return the calculated square root of x with the precision specified in the {@code mathContext}
     * @throws ArithmeticException if x &lt; 0
     *
     * @author <a href="https://github.com/eobermuhlner/big-math">Eric Obermühlner</a>
     */
    static BigDecimal sqrt(final BigDecimal x, final MathContext mathContext) {
        if (x.signum() == 0) {
            return ZERO;
        }

        if (x.signum() == -1) {
            throw new ArithmeticException("Illegal sqrt(x) for x < 0: x = " + x);
        }

        @SuppressWarnings("NestedMethodCall") BigDecimal result = x.compareTo(valueOf(Double.MAX_VALUE)) > 0
                ? x.divide(TWO, mathContext)
                : valueOf(Math.sqrt(x.doubleValue()));

        if (result.multiply(result, mathContext).compareTo(x) == 0) {
            return result.round(mathContext); // early exit if x is a square number
        }

        int adaptivePrecision = EXPECTED_INITIAL_PRECISION;
        BigDecimal last;

        final int precision = mathContext.getPrecision();
        final int maxPrecision = precision + 6;
        final BigDecimal acceptableError = ONE.movePointLeft(precision + 1);

        do {
            last = result;
            adaptivePrecision *= 2;
            if (adaptivePrecision > maxPrecision) {
                adaptivePrecision = maxPrecision;
            }
            final RoundingMode roundingMode = mathContext.getRoundingMode();
            final MathContext mc = new MathContext(adaptivePrecision, roundingMode);
            result = x.divide(result, mc).add(last, mc).divide(TWO, mc);
        } while (adaptivePrecision < maxPrecision || result.subtract(last).abs().compareTo(acceptableError) > 0);

        return result.round(mathContext);
    }
}
