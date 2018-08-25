package com.vaz.projects.calculator.model;

import com.vaz.projects.calculator.controller.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ModelTest {
    @Test
    @DisplayName("Illegal process operator")
    void testProcessMathOperator_IllegalEvent() {
        final Model model = new Model(new Controller());
        model.updateCurrentNumber(new BigDecimal("1"));
        model.processMathOperator(Operator.Neg);
        model.updateCurrentNumber(new BigDecimal("1"));

        final String expectedErrorMessage = "Calculate method. Incorrect operator: C";
        final Executable executeProcessMathOperator = () -> model
                .processMathOperator(Operator.Neg);

        assertThrows(IllegalArgumentException.class, executeProcessMathOperator, expectedErrorMessage);
    }

    @Test
    @DisplayName("Illegal transform operator")
    void testProcessNumberTransform_IllegalEvent() {
        final String expectedErrorMessage = "Calculate method. Incorrect operator: C";
        final Executable executeProcessNumberTransform = () -> new Model(new Controller())
                .processNumberTransform(Operator.Eq);

        assertThrows(IllegalArgumentException.class, executeProcessNumberTransform, expectedErrorMessage);
    }
}