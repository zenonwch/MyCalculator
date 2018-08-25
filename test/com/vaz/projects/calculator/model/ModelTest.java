package com.vaz.projects.calculator.model;

import com.vaz.projects.calculator.controller.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ModelTest {
    @Test
    @DisplayName("Illegal process operator")
    void testProcessMathOperator_IllegalEvent() {
        final Model model = new Model(new Controller());
        model.updateCurrentNumber(new BigDecimal("1"));
        model.processMathOperator(Operator.Neg);
        model.updateCurrentNumber(new BigDecimal("1"));

        final Executable executeProcessMathOperator = () -> model
                .processMathOperator(Operator.Neg);
        final String expectedErrorMessage = "Calculate method. Incorrect operator: Neg";

        final IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, executeProcessMathOperator, expectedErrorMessage);

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Illegal transform operator")
    void testProcessNumberTransform_IllegalEvent() {
        final Executable executeProcessNumberTransform = () -> new Model(new Controller())
                .processNumberTransform(Operator.Eq);
        final String expectedErrorMessage = "Transform method. Incorrect operator: Eq";

        final IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, executeProcessNumberTransform, expectedErrorMessage);

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void testProcessMemory_IllegalEvent() {
        final Executable executeProcessMemory = () -> new Model(new Controller())
                .processMemory(Operator.Eq);
        final String expectedErrorMessage = "Process Memory. Incorrect operator: Eq";

        final IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, executeProcessMemory, expectedErrorMessage);

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}