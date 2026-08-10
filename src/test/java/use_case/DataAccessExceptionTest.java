package use_case;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class DataAccessExceptionTest {

    @Test
    void messageOnlyConstructorSetsMessageAndNoCause() {
        final DataAccessException exception = new DataAccessException("failed");

        assertEquals("failed", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void messageAndCauseConstructorSetsBoth() {
        final Throwable cause = new RuntimeException("root cause");

        final DataAccessException exception = new DataAccessException("failed", cause);

        assertEquals("failed", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}
