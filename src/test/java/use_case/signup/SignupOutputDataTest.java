package use_case.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SignupOutputDataTest {

    @Test
    void gettersReturnConstructedValues() {
        final SignupOutputData success = new SignupOutputData("aahir", false);
        final SignupOutputData failure = new SignupOutputData("aahir", true);

        assertEquals("aahir", success.getUsername());
        assertFalse(success.isUseCaseFailed());
        assertTrue(failure.isUseCaseFailed());
    }
}
