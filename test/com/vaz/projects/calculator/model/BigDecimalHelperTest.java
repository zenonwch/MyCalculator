package com.vaz.projects.calculator.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

import static com.vaz.projects.calculator.model.BigDecimalHelper.sqrt;
import static org.junit.jupiter.api.Assertions.*;

class BigDecimalHelperTest {
    @Test
    @DisplayName("sqrt(4) = 2")
    void test1() {
        final BigDecimal result = sqrt(new BigDecimal("4"), new MathContext(16));

        assertEquals("2", result.stripTrailingZeros().toPlainString());
    }

    @Test
    @DisplayName("sqrt(4) - 2 = 0")
    void test2() {
        final BigDecimal result = sqrt(new BigDecimal("4"), new MathContext(16))
                .subtract(new BigDecimal("2"));

        assertEquals("0", result.stripTrailingZeros().toPlainString());
    }

    @Test
    @DisplayName("sqrt(2) = 1.41421356237309504880168872420969807856967187537694807317667973799")
    void test3() {
        final BigDecimal result = sqrt(new BigDecimal("2"), new MathContext(66));

        assertEquals("1.41421356237309504880168872420969807856967187537694807317667973799", result.stripTrailingZeros().toPlainString());
    }

    @Test
    @DisplayName("sqrt(-2) -> ArithmeticException")
    void test4() {
        assertThrows(ArithmeticException.class, () -> sqrt(new BigDecimal("-2"), new MathContext(16)));
    }

    @Test
    @DisplayName("sqrt(0) = 0")
    void test5() {
        final BigDecimal result = sqrt(BigDecimal.ZERO, new MathContext(16));

        assertEquals("0", result.stripTrailingZeros().toPlainString());
    }

    @Test
    @DisplayName("sqrt(3.231700607131101e+616) = 1.797693134862316e+308")
    void test6() {
        final BigDecimal result = sqrt(new BigDecimal("3.231700607131101e+616"), new MathContext(16));

        assertEquals("1.797693134862316E+308", result.stripTrailingZeros().toString());
    }

    @Test
    @DisplayName("sqrt(0.5) = 0.707106781186547524400844362104849039284835937688474036588339868995")
    void test7() {
        final BigDecimal result = sqrt(new BigDecimal("0.5"), new MathContext(66));

        assertEquals("0.707106781186547524400844362104849039284835937688474036588339868995", result.stripTrailingZeros().toPlainString());
    }

    @Test
    @DisplayName("sqrt(0.99999999999999999999) = 1")
    void test8() {
        final BigDecimal result = sqrt(new BigDecimal("0.99999999999999999999"), new MathContext(20));

        assertEquals("1", result.stripTrailingZeros().toString());
    }
}