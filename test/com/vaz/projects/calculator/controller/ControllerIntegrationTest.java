package com.vaz.projects.calculator.controller;

import com.vaz.projects.calculator.MyApplicationTest;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static com.vaz.projects.calculator.controller.Controller.MAX_DIGIT_NUMBER;
import static com.vaz.projects.calculator.model.Model.ERR_DIV_BY_ZERO;
import static com.vaz.projects.calculator.model.Model.ERR_UNDEFINED;
import static org.junit.jupiter.api.Assertions.*;

class ControllerIntegrationTest extends MyApplicationTest {

    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonDot;
    private Button buttonNeg;
    private Button buttonInv;
    private Button buttonSqrt;
    private Button buttonPerc;
    private Button buttonBs;
    private Button buttonCE;
    private Button buttonC;
    private Button buttonAdd;
    private Button buttonSub;
    private Button buttonMult;
    private Button buttonDiv;
    private Button buttonEq;
    private Button buttonMC;
    private Button buttonMR;
    private Button buttonMS;
    private Button buttonMAdd;
    private Button buttonMSub;

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();

        button0 = findButton("0");
        button1 = findButton("1");
        button2 = findButton("2");
        button3 = findButton("3");
        button4 = findButton("4");
        button5 = findButton("5");
        button6 = findButton("6");
        button7 = findButton("7");
        button8 = findButton("8");
        button9 = findButton("9");

        buttonDot = findButton(".");
        buttonNeg = findButton("±");
        buttonInv = findButton("1/x");
        buttonSqrt = findButton("√");
        buttonPerc = findButton("%");

        buttonBs = findButton("⬅");
        buttonCE = findButton("CE");
        buttonC = findButton("C");

        buttonAdd = findButton("+");
        buttonSub = findButton("-");
        buttonMult = findButton("*");
        buttonDiv = findButton("/");
        buttonEq = findButton("=");

        buttonMC = findButton("MC");
        buttonMR = findButton("MR");
        buttonMS = findButton("MS");
        buttonMAdd = findButton("M+");
        buttonMSub = findButton("M-");

        clickOn(buttonC);
    }

    @Test
    @DisplayName("One-digit number should be displayed")
    void testOneDigitNumberInput() {
        clickOn(button7);

        assertEquals("7", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Two-digit number should be displayed")
    void testTwoAndMoreDigitNumberInput() {
        clickButtonSequence(button1, button2);

        assertEquals("12", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName(MAX_DIGIT_NUMBER + "-digit number should be displayed")
    void testMaxDisplayedDigits() {
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER + 4);

        assertEquals("9999999999999999", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("12345.06789")
    void testAllDigitButtonsAndDot() {
        clickButtonSequence(button1, button2, button3,
                button4, button5, buttonDot, button0,
                button6, button7, button8, button9);
        assertEquals("12345.06789", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Only one 0 symbol should be displayed on multiple hit 0 button")
    void testOneZeroDigit() {
        clickButtonNTimes(button0, 5);

        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Fractional number should contain one dot only")
    void testOneDecimalPoint() {
        clickButtonSequence(buttonDot, buttonDot);
        assertEquals("0.", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonDot, buttonDot, buttonEq);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonDot, button1, buttonDot, button2, buttonDot, button3, buttonEq);
        assertEquals("0.123", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonDot, button1, buttonDot, button2, buttonDot, button3);
        assertEquals("0.123", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName(".N should be displayed as 0.N")
    void testLeadingZeroForFractionalNumber() {
        clickButtonSequence(buttonDot, button1, buttonEq);
        assertEquals("0.1", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonDot, button0, button9);
        assertEquals("0.09", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("1 = 2 = 2")
    void testNoOperator() {
        clickButtonSequence(button1, buttonEq, button2, buttonEq);

        assertEquals("2", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("The '±' button should make number opposite")
    void testNegateNumber() {
        clickOn(button0);
        clickButtonNTimes(buttonNeg, 3);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, buttonNeg);
        assertEquals("-1", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonNeg);
        assertEquals("1", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonAdd);
        assertEquals("1 +", getSuperscriptText());

        clickButtonSequence(button1, buttonEq);
        assertEquals("2", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonNeg);
        assertEquals("-2", getOutputText());
        assertEquals("negate(2)", getSuperscriptText());
    }

    @Test
    @DisplayName("Backspace")
    void testBackSpace() {
        clickOn(buttonBs);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, buttonBs);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonNTimes(button9, 5);
        clickButtonNTimes(buttonBs, 6);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonNTimes(button1, 5);
        clickButtonNTimes(buttonBs, 2);
        assertEquals("111", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Clear all")
    void testClear() {
        clickButtonSequence(button9, buttonC);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button9, button9, buttonC);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button9, buttonAdd, button9, buttonC, buttonEq);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button9, buttonAdd, button9, buttonC, button2, buttonEq);
        assertEquals("2", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button9, buttonAdd, buttonC, button9, buttonEq);
        assertEquals("9", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button9, buttonAdd, button9, buttonC, button2, buttonAdd, button7, buttonEq);
        assertEquals("9", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Clear the last number only")
    void testClearLast() {
        clickButtonSequence(button9, buttonCE);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button9, button2, buttonCE);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, button0, buttonAdd, button7, buttonCE, button9, buttonEq);
        assertEquals("19", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, button0, buttonAdd, button7, buttonCE);
        assertEquals("0", getOutputText());
        assertEquals("10 +", getSuperscriptText());
    }

    @Test
    @DisplayName("+ 2 = 2; 1 + 1 = 2; + 2 = 4")
    void testSimpleAddition() {
        clickButtonSequence(buttonAdd, button2);
        assertEquals("0 +", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("2", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, buttonAdd, button1, buttonEq);
        assertEquals("2", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonAdd, button2);
        assertEquals("2 +", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("4", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("9999999999999999 + 9999999999999999 = 2.e+16")
    void testSimpleAddition_MaxValues() {
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER + 4);
        clickOn(buttonAdd);
        assertEquals("9999999999999999 +", getSuperscriptText());
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER + 3);
        clickOn(buttonEq);

        assertEquals("2.e+16", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("1 + 9 + 9 + 1 = 20")
    void testComplexAddition() {
        clickButtonSequence(
                button1, buttonAdd,
                button9, buttonAdd,
                button9, buttonAdd);
        assertEquals("1 + 9 + 9 +", getSuperscriptText());

        clickButtonSequence(button1, buttonEq);
        assertEquals("20", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("2 + = + = + = 16")
    void testComplexComputation() {
        clickButtonSequence(button2, buttonAdd, buttonEq,
                buttonAdd, buttonEq, buttonAdd, buttonEq);
        assertEquals("16", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button2, buttonAdd, button2, buttonEq, buttonAdd);
        assertEquals("4 +", getSuperscriptText());

        clickButtonSequence(buttonEq, buttonAdd);
        assertEquals("8 +", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("16", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("2 + 5 = 7 -> 3 = 8 = 13 -> 4 = 9 = 14")
    void testComplexComputation_2() {
        clickButtonSequence(button2, buttonAdd, button5,
                buttonEq, button3, buttonEq);
        assertEquals("8", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("13", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button4, buttonEq);
        assertEquals("9", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("14", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Bunch of simple subtractions")
    void testSimpleSubtraction() {
        clickButtonSequence(buttonSub, button9);
        assertEquals("0 -", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("-9", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, buttonSub);
        assertEquals("1 -", getSuperscriptText());
        clickButtonSequence(button1, buttonEq);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, buttonSub, button2, buttonEq);
        assertEquals("-1", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonSub, button9);
        assertEquals("-1 -", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("-10", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("-9999999999999999 - 9999999999999999 = -2.e+16")
    void testSimpleSubtraction_MaxValues() {
        clickOn(buttonSub);
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER + 3);
        assertEquals("0 -", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("-9999999999999999", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonSub);
        assertEquals("-9999999999999999 -", getSuperscriptText());
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER + 4);
        clickOn(buttonEq);
        assertEquals("-2.e+16", getOutputText());
    }

    @Test
    @DisplayName("20 + 5 - 9 - 1 + 5 = 20")
    void testComplexSubtraction() {
        clickButtonSequence(
                button2, button0,
                buttonAdd, button5,
                buttonSub, button9,
                buttonSub, button1,
                buttonAdd);
        assertEquals("20 + 5 - 9 - 1 +", getSuperscriptText());

        clickButtonSequence(button5, buttonEq);
        assertEquals("20", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("2 * 2 = 4")
    void testSimpleMultiplication() {
        clickButtonSequence(button2, buttonMult);
        assertEquals("2 *", getSuperscriptText());

        clickButtonSequence(button2, buttonEq);
        assertEquals("4", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("9999999999999999 * 9999999999999999 = 9.999999999999998e+31")
    void testSimpleMultiplication_MaxValues() {
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER + 2);
        clickButtonSequence(buttonMult, buttonEq);
        assertEquals("9.999999999999998e+31", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("0.9999999999999999 * 0.9999999999999999 = 0.9999999999999998")
    void testSimpleMultiplication_FractValues() {
        clickOn(buttonDot);
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER + 3);
        clickButtonSequence(buttonMult, buttonEq);
        assertEquals("0.9999999999999998", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("0.0000000000000009 * 0.0000000000000009 = 8.1e-31")
    void testSimpleMultiplication_MinValues() {
        clickOn(buttonDot);
        clickButtonNTimes(button0, MAX_DIGIT_NUMBER - 1);
        clickButtonSequence(button9, buttonMult);
        assertEquals("0.0000000000000009 *", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("8.1e-31", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("-0.000000000000001 * 0.1 = -0.0000000000000001 * 0.1 = -1.e-17")
    void testSimpleMultiplication_NegativeValue() {
        clickOn(buttonDot);
        clickButtonNTimes(button0, MAX_DIGIT_NUMBER - 2);
        clickButtonSequence(button1, buttonNeg, buttonMult, buttonDot, button1, buttonEq);
        assertEquals("-0.0000000000000001", getOutputText());

        clickOn(buttonMult);
        assertEquals("-0.0000000000000001 *", getSuperscriptText());

        clickButtonSequence(buttonDot, button1, buttonEq);
        assertEquals("-1.e-17", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("7 * -20 = -140")
    void testSimpleMultiplication_NegateSecondNumber() {
        clickButtonSequence(button7, buttonMult, buttonNeg);
        assertEquals("7 * negate(7)", getSuperscriptText());
        clickOn(button2);
        assertEquals("7 *", getSuperscriptText());
        clickButtonSequence(button0, buttonEq);
        assertEquals("140", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button7, buttonMult, button2, buttonNeg);
        assertEquals("7 *", getSuperscriptText());
        clickButtonSequence(button0, buttonEq);
        assertEquals("-140", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button7, buttonMult, button2, button0, buttonNeg);
        assertEquals("7 *", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("-140", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("1 * 2 * 7 * 9 = 126")
    void testComplexMultiplication() {
        clickButtonSequence(button1, buttonMult, button2, buttonMult, button7, buttonMult, button9);
        assertEquals("1 * 2 * 7 *", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("126", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("10 / 2 = 5; 1 / 2 = 0.5")
    void testSimpleDivision() {
        clickButtonSequence(button1, button0, buttonDiv);
        assertEquals("10 /", getSuperscriptText());
        clickButtonSequence(button2, buttonEq);
        assertEquals("5", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, buttonDiv, button2, buttonEq);
        assertEquals("0.5", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("12 / -2 = -6")
    void testSimpleDivision_NegateSecondNumber() {
        clickButtonSequence(button1, button2, buttonDiv, buttonNeg);
        assertEquals("12 / negate(12)", getSuperscriptText());
        clickOn(button2);
        assertEquals("12 /", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("6", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, button2, buttonDiv, button2, buttonNeg);
        assertEquals("12 /", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("-6", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("1 / 9 = 0.1111111111111111")
    void testInfiniteFraction() {
        clickButtonSequence(button1, buttonDiv, button9, buttonEq);
        assertEquals("0.1111111111111111", getOutputText());
    }

    @Test
    @DisplayName("1.e+16 / 10 = 1000000000000000; -1.e+16 / 10 = -1000000000000000")
    void testSimpleDivision_PositiveScientificNotation() {
        clickOn(button1);
        clickButtonNTimes(button0, MAX_DIGIT_NUMBER);
        clickButtonSequence(buttonMult, button1, button0, buttonEq, buttonDiv);
        assertEquals("1.e+16 /", getSuperscriptText());

        clickButtonSequence(button1, button0, buttonEq);
        assertEquals("1000000000000000", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(button1);
        clickButtonNTimes(button0, MAX_DIGIT_NUMBER);
        clickButtonSequence(buttonNeg, buttonMult, button1, button0, buttonEq, buttonDiv);
        assertEquals("-1.e+16 /", getSuperscriptText());

        clickButtonSequence(button1, button0, buttonEq);
        assertEquals("-1000000000000000", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("1.e-17 * 10 = 0.0000000000000001; -1.e-17 * 10 = -0.0000000000000001")
    void testSimpleDivision_NegativeScientificNotation() {
        clickOn(buttonDot);
        clickButtonNTimes(button0, MAX_DIGIT_NUMBER - 1);
        clickButtonSequence(button1, buttonDiv, button1, button0, buttonEq, buttonMult);
        assertEquals("1.e-17 *", getSuperscriptText());

        clickButtonSequence(button1, button0, buttonEq);
        assertEquals("0.0000000000000001", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonDot);
        clickButtonNTimes(button0, MAX_DIGIT_NUMBER - 1);
        clickButtonSequence(button1, buttonNeg, buttonDiv, button1, button0, buttonEq, buttonMult);
        assertEquals("-1.e-17 *", getSuperscriptText());

        clickButtonSequence(button1, button0, buttonEq);
        assertEquals("-0.0000000000000001", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("N / 0;  0 / 0")
    void testDivisionByZero() {
        clickButtonSequence(button1, buttonDiv, button0, buttonEq);
        assertEquals(ERR_DIV_BY_ZERO, getOutputText());
        assertEquals("1 /", getSuperscriptText());

        clickOn(buttonC);

        clickButtonSequence(buttonDiv, buttonEq);
        assertEquals(ERR_UNDEFINED, getOutputText());
        assertEquals("0 /", getSuperscriptText());
    }

    @Test
    @DisplayName("1 / 9 / (1/9) = 1")
    void testComplexDivision() {
        clickButtonSequence(button1, buttonDiv, button9, buttonDiv, button9, buttonInv);
        assertEquals("1 / 9 / reciproc(9)", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("1", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("1.2 / 0.2 = 6")
    void testSimpleDivision_FractValues() {
        clickButtonSequence(button1, buttonDot, button2, buttonDiv, button0, buttonDot, button2, buttonEq);
        assertEquals("6", getOutputText());
    }

    @Test
    @DisplayName("Max / Min; Min / Max")
    void testSimpleDivision_MaxMinValues() {
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER);
        clickButtonSequence(buttonDiv, buttonDot);
        clickButtonNTimes(button0, MAX_DIGIT_NUMBER - 1);
        clickButtonSequence(button1, buttonEq);
        assertEquals("9.999999999999999e+31", getOutputText());

        clickOn(buttonDot);
        clickButtonNTimes(button0, MAX_DIGIT_NUMBER - 1);
        clickButtonSequence(button1, buttonNeg, buttonDiv);
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER);
        clickOn(buttonEq);
        assertEquals("-1.e-32", getOutputText());
    }

    @Test
    @DisplayName("2 +  - / * 5 = 10")
    void testOperatorReplacement() {
        clickButtonSequence(button2, buttonAdd);
        assertEquals("2 +", getSuperscriptText());
        clickOn(buttonSub);
        assertEquals("2 -", getSuperscriptText());
        clickOn(buttonDiv);
        assertEquals("2 /", getSuperscriptText());
        clickOn(buttonMult);
        assertEquals("2 *", getSuperscriptText());
        clickButtonSequence(button5, buttonEq);
        assertEquals("10", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("inverse(inverse(2)) = 2; inverse(inverse(9)) = 9")
    void testInversionTwice() {
        clickButtonSequence(button2, buttonInv, buttonInv);
        assertEquals("2", getOutputText());
        assertEquals("reciproc(reciproc(2))", getSuperscriptText());

        clickOn(button9);
        clickButtonNTimes(buttonInv, 4);
        assertEquals("9", getOutputText());
        assertEquals("c(reciproc(reciproc(9))))", getSuperscriptText());
        assertTrue(getLongSuperscriptFlag().isVisible());
    }

    @Test
    @DisplayName("inverse(0) -> ArithmeticException")
    void testInversion_Zero() {
        clickOn(buttonInv);
        assertEquals("Cannot divide by zero", getOutputText());
        assertEquals("reciproc(0)", getSuperscriptText());
    }

    @Test
    @DisplayName("inverse(1) = 1")
    void testInversion_One() {
        clickButtonSequence(button1, buttonInv);
        assertEquals("1", getOutputText());
        assertEquals("reciproc(1)", getSuperscriptText());

        clickButtonSequence(button1, buttonInv, buttonEq);
        assertEquals("1", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, buttonEq, buttonInv);
        assertEquals("1", getOutputText());
        assertEquals("reciproc(1)", getSuperscriptText());
    }

    @Test
    @DisplayName("inverse(2) = 0.5")
    void testInversion_Two() {
        clickButtonSequence(button2, buttonInv);
        assertEquals("0.5", getOutputText());

        clickButtonSequence(button2, buttonInv, buttonEq);
        assertEquals("0.5", getOutputText());

        clickButtonSequence(button2, buttonEq, buttonInv);
        assertEquals("0.5", getOutputText());
    }

    @Test
    @DisplayName("inverse(9) = 0.1111111111111111")
    void testInversion_Nine() {
        clickButtonSequence(button9, buttonInv);
        assertEquals("0.1111111111111111", getOutputText());

        clickButtonSequence(button9, buttonInv, buttonEq);
        assertEquals("0.1111111111111111", getOutputText());

        clickButtonSequence(button9, buttonEq, buttonInv);
        assertEquals("0.1111111111111111", getOutputText());
    }

    @Test
    @DisplayName("9 * inverse(9) = 1")
    void testInversion_Complex() {
        clickButtonSequence(button9, buttonMult, button9, buttonInv);
        assertEquals("9 * reciproc(9)", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("1", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button9, buttonInv, buttonMult);
        assertEquals("reciproc(9) *", getSuperscriptText());
        clickButtonSequence(button9, buttonEq);
        assertEquals("1", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("sqrt(9) = 3")
    void testSquareRoot_9() {
        clickButtonSequence(button9, buttonSqrt);
        assertEquals("sqrt(9)", getSuperscriptText());
        assertEquals("3", getOutputText());
    }

    @Test
    @DisplayName("sqrt(0.9999999999999999) = 0.9999999999999999")
    void testSquareRoot_FractValue() {
        clickOn(buttonDot);
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER);
        clickOn(buttonSqrt);
        assertEquals("sqrt(0.9999999999999999)", getSuperscriptText());
        assertEquals("0.9999999999999999", getOutputText());
    }

    @Test
    @DisplayName("Long superscript value")
    void testSuperscript_LongValue() {
        clickOn(buttonDot);
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER);
        clickButtonSequence(buttonSqrt, buttonAdd, button1);
        assertEquals("sqrt(0.9999999999999999) +", getSuperscriptText());
        assertFalse(getLongSuperscriptFlag().isVisible());

        clickOn(buttonAdd);
        assertEquals("2", getOutputText());
        assertEquals("0.9999999999999999) + 1 +", getSuperscriptText());
        assertTrue(getLongSuperscriptFlag().isVisible());
    }

    @Test
    @DisplayName("sqrt(0.9999999999999999) * sqrt(0.9999999999999999) = 0.9999999999999999")
    void testSquareRoot_MultiplicationFractValue() {
        clickOn(buttonDot);
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER);
        clickButtonSequence(buttonSqrt, buttonMult, buttonDot);
        clickButtonNTimes(button9, MAX_DIGIT_NUMBER);
        clickOn(buttonEq);

        assertEquals("0.9999999999999999", getOutputText());
    }

    @Test
    @DisplayName("sqrt(0.2) * sqrt(0.2) = 0.2")
    void testSquareRoot_MultiplicationFractValue2() {
        clickButtonSequence(buttonDot, button2, buttonSqrt, buttonMult,
                buttonDot, button2, buttonSqrt, buttonEq);

        assertEquals("0.2", getOutputText());
    }

    @Test
    @DisplayName("sqrt(9) - 3 = 0")
    void testSquareRoot_Complex() {
        clickButtonSequence(button9, buttonSqrt, buttonAdd, button3, buttonNeg, buttonEq);

        assertEquals("0", getOutputText());
    }

    @Test
    @DisplayName("sqrt(1 + ... + 1 = 9) = 4 = 5")
    void testComplexNumberTransform() {
        clickButtonSequence(button1, buttonAdd, button1);
        clickButtonNTimes(buttonEq, 8);
        clickOn(buttonSqrt);
        assertEquals("sqrt(9)", getSuperscriptText());

        clickButtonSequence(buttonEq, buttonEq);
        assertEquals("5", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("100% = 0")
    void testSimplePercent_OneNumberOnly() {
        clickButtonSequence(button1, button0, button0, buttonPerc);
        assertEquals("0", getSuperscriptText());
        clickOn(buttonEq);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, button0, button0, buttonPerc);
        assertEquals("0", getOutputText());
        assertEquals("0", getSuperscriptText());
    }

    @Test
    @DisplayName("3% of 150 = 4.5")
    void testSimplePercent() {
        clickButtonSequence(button1, button5, button0, buttonMult, button3, buttonPerc);
        assertEquals("4.5", getOutputText());
        assertEquals("150 * 4.5", getSuperscriptText());
    }

    @Test
    @DisplayName("10 + % = 11")
    void testSimplePercent_NoSecondNumberInput() {
        clickButtonSequence(button1, button0, buttonAdd, buttonPerc);
        assertEquals("10 + 1", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("11", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("10 + 5% = 10.5")
    void testAddPercent() {
        clickButtonSequence(button1, button0, buttonAdd, button5, buttonPerc);
        assertEquals("10 + 0.5", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("10.5", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("20 - 3% = 19.4")
    void testSubtractPercent() {
        clickButtonSequence(button2, button0, buttonSub, button3, buttonPerc);
        assertEquals("20 - 0.6", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("19.4", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("10 * 5% = 5")
    void testMultiplyPercent() {
        clickButtonSequence(button1, button0, buttonMult, button5, buttonPerc);
        assertEquals("10 * 0.5", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("5", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("20 / 20% = 5")
    void testDividePercent() {
        clickButtonSequence(button2, button0, buttonDiv, button2, button0, buttonPerc);
        assertEquals("20 / 4", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("5", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("20 + 5% - 5 = 16")
    void testComplexPercent() {
        clickButtonSequence(button2, button0, buttonAdd, button5, buttonPerc, buttonSub);
        assertEquals("20 + 1 -", getSuperscriptText());

        clickButtonSequence(button5, buttonEq);
        assertEquals("16", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("100 + 10%% = 110; 100 + 10% =%% = 133.1")
    void testPercent_MultipleClick() {
        clickButtonSequence(button1, button0, button0, buttonAdd,
                button1, button0, buttonPerc, buttonPerc);
        assertEquals("100 + 10", getSuperscriptText());

        clickOn(buttonEq);
        assertEquals("110", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(button1, button0, button0, buttonAdd,
                button1, button0, buttonPerc);
        assertEquals("100 + 10", getSuperscriptText());

        clickButtonSequence(buttonEq, buttonPerc);
        assertEquals("121", getSuperscriptText());

        clickOn(buttonPerc);
        assertEquals("133.1", getOutputText());
        assertEquals("133.1", getSuperscriptText());
    }

    @Test
    @DisplayName("null operator")
    void testProcessOperator_Null() {
        clickButtonSequence(button1, buttonAdd, button2);

        interact(() -> {
            final Button buttonNull = new Button();
            buttonNull.setText(null);
            final ActionEvent wrongEvent = new ActionEvent(buttonNull, null);
            final String expectedErrorMessage = "Empty operator value";

            assertThrows(IllegalArgumentException.class, () -> getController().processOperationEvent(wrongEvent), expectedErrorMessage);
        });
    }

    @Test
    @DisplayName("sqrt(-9) -> ArithmeticException")
    void testProcessNumberTransform_SqrtForNegativeValue() {
        clickButtonSequence(button9, buttonNeg, buttonSqrt);
        assertEquals("Invalid input", getOutputText());
        assertEquals("sqrt(-9)", getSuperscriptText());
    }

    @Test
    @DisplayName("All buttons except clear are disabled on error")
    void testError_allOperationsDisabled() {
        clickButtonSequence(button0, buttonDiv, buttonEq);

        final Set<Button> allButtons = findAllButtons();
        allButtons.forEach(button -> {
            final String buttonLabel = button.getText();

            if (!"C".equals(buttonLabel) && !"CE".equals(buttonLabel)) {
                clickOn(button);
                assertEquals(ERR_UNDEFINED, getOutputText());
                assertEquals("0 /", getSuperscriptText());
            }
        });
    }

    @Test
    @DisplayName("Clear buttons are enabled on error only")
    void testError_clear() {
        clickButtonSequence(buttonDiv, buttonEq);
        assertEquals(ERR_UNDEFINED, getOutputText());

        clickOn(buttonCE);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonDiv, buttonEq);
        assertEquals(ERR_UNDEFINED, getOutputText());

        clickOn(buttonC);
        assertEquals("0", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("0 can not be added to memory")
    void testMemory_noEffectIfZero() {
        clickOn(buttonMS);
        assertEquals("0", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMAdd);
        assertEquals("0", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMSub);
        assertEquals("0", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMR);
        assertEquals("0", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMC);
        assertEquals("0", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Current number is stored in memory")
    void testMemory_storeAndRetrieve() {
        clickButtonSequence(button5, buttonMS, buttonC, buttonMR);

        assertEquals("5", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("It is possible to add to and subtract from memory")
    void testMemory_addAndSubtract() {
        clickButtonSequence(button5, buttonMAdd, buttonC, button6, buttonMAdd, buttonEq, buttonC, buttonMR);
        assertEquals("11", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMC);
        clickButtonSequence(button5, buttonMAdd, button7, buttonMAdd, button6, buttonMSub, button8, buttonMR);
        assertEquals("6", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMC);
        clickButtonSequence(button5, buttonMAdd, button5, buttonMSub);
        assertEquals("5", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMR);
        assertEquals("0", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Hit M+ or M- multiple times")
    void testMemory_complexAddAndSubtract() {
        clickOn(button5);
        clickButtonNTimes(buttonMAdd, 5);
        assertEquals("5", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMR);
        assertEquals("25", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonMC, buttonC, button5);
        clickButtonNTimes(buttonMAdd, 5);
        clickButtonNTimes(buttonMSub, 3);
        assertEquals("5", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMR);
        assertEquals("10", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonMC, buttonC, button5);
        clickButtonNTimes(buttonMAdd, 5);
        clickButtonNTimes(buttonMSub, 5);
        assertEquals("5", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickButtonNTimes(buttonMSub, 2);
        assertEquals("5", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMR);
        assertEquals("-10", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Memory processing should not break the flow")
    void testMemory_processFlow() {
        clickButtonSequence(button5, buttonAdd, button1);
        assertEquals("5 +", getSuperscriptText());
        clickButtonNTimes(buttonEq, 5);
        clickOn(buttonMAdd);
        clickButtonNTimes(buttonEq, 5);
        assertEquals("15", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMR);
        clickButtonNTimes(buttonEq, 2);
        assertEquals("12", getOutputText());
        assertEquals("", getSuperscriptText());

        clickButtonSequence(buttonMR, buttonMAdd, buttonMAdd);
        clickButtonNTimes(buttonEq, 5);
        assertEquals("15", getOutputText());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMR);
        clickButtonNTimes(buttonEq, 5);
        assertEquals("35", getOutputText());
        assertEquals("", getSuperscriptText());
    }

    @Test
    @DisplayName("Memory should be cleared without affect displayed number")
    void testMemory_clear() {
        clickButtonSequence(button5, buttonMAdd, button6, buttonMR);
        assertEquals("5", getOutputText());
        assertTrue(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMC);
        assertEquals("5", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());

        clickOn(buttonMR);
        assertEquals("0", getOutputText());
        assertFalse(getMemoryFlag().isVisible());
        assertEquals("", getSuperscriptText());
    }

    private void clickButtonSequence(final Button... buttons) {
        Arrays.stream(buttons).forEach(this::clickOn);
    }

    private void clickButtonNTimes(final Button button, final int times) {
        for (int i = 0; i < times; i++) {
            clickOn(button);
        }
    }

    private String getOutputText() {
        return ((Text) find("#output")).getText();
    }

    private String getSuperscriptText() {
        return ((Text) find("#superscript")).getText();
    }

    private Text getLongSuperscriptFlag() {
        return find("#longSuperscript");
    }

    private Text getMemoryFlag() {
        return find("#memory");
    }
}