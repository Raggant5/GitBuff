package interface_adapter.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SignupStateTest {

    @Test
    void gettersAndSettersRoundTrip() {
        final SignupState state = new SignupState();

        state.setUsername("aahir");
        state.setUsernameError("username taken");
        state.setPassword("password123");
        state.setPasswordError("too short");
        state.setRepeatPassword("password123");
        state.setRepeatPasswordError("does not match");

        assertEquals("aahir", state.getUsername());
        assertEquals("username taken", state.getUsernameError());
        assertEquals("password123", state.getPassword());
        assertEquals("too short", state.getPasswordError());
        assertEquals("password123", state.getRepeatPassword());
        assertEquals("does not match", state.getRepeatPasswordError());
    }

    @Test
    void toStringIncludesUsernameAndPasswords() {
        final SignupState state = new SignupState();
        state.setUsername("aahir");
        state.setPassword("password123");
        state.setRepeatPassword("password123");

        final String result = state.toString();

        assertTrue(result.contains("aahir"));
        assertTrue(result.contains("password123"));
    }
}
