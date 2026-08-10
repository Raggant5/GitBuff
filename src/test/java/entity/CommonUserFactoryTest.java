package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommonUserFactoryTest {

    @Test
    void createReturnsCommonUserWithGivenNameAndPassword() {
        final CommonUserFactory factory = new CommonUserFactory();

        final User user = factory.create("amir", "secret");

        assertTrue(user instanceof CommonUser);
        assertEquals("amir", user.getName());
        assertEquals("secret", user.getPassword());
    }
}
