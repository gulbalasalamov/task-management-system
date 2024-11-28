package com.gulbalasalamov.taskmanagementsystem;


import com.gulbalasalamov.taskmanagementsystem.exception.CustomAccessDeniedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomAccessDeniedExceptionTest {

    @Test
    void shouldReturnCorrectMessage() {
        String message = "Access Denied";
        CustomAccessDeniedException exception = new CustomAccessDeniedException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }
}
