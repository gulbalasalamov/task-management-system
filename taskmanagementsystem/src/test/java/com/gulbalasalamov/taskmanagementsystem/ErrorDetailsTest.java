package com.gulbalasalamov.taskmanagementsystem;

import com.gulbalasalamov.taskmanagementsystem.exception.ErrorDetails;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorDetailsTest {

    @Test
    void shouldCreateErrorDetailsWithCorrectAttributes() {
        Date now = new Date();
        String message = "An error occurred";
        String details = "Error details here";

        ErrorDetails errorDetails = new ErrorDetails(now, message, details);

        assertThat(errorDetails.getTimeStamp()).isEqualTo(now);
        assertThat(errorDetails.getMessage()).isEqualTo(message);
        assertThat(errorDetails.getDetails()).isEqualTo(details);
    }

    @Test
    void shouldSetAndGetTimeStamp() {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "", "");
        Date newTimeStamp = new Date();

        errorDetails.setTimeStamp(newTimeStamp);

        assertThat(errorDetails.getTimeStamp()).isEqualTo(newTimeStamp);
    }

    @Test
    void shouldSetAndGetMessage() {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "", "");
        String newMessage = "New error message";

        errorDetails.setMessage(newMessage);

        assertThat(errorDetails.getMessage()).isEqualTo(newMessage);
    }

    @Test
    void shouldSetAndGetDetails() {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "", "");
        String newDetails = "New error details";

        errorDetails.setDetails(newDetails);

        assertThat(errorDetails.getDetails()).isEqualTo(newDetails);
    }
}
