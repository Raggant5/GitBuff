package interface_adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class ViewModelTest {

    private static final int AWAIT_SECONDS = 5;

    @Test
    void getViewNameReturnsConstructedName() {
        final ViewModel<String> viewModel = new ViewModel<>("dashboard");

        assertEquals("dashboard", viewModel.getViewName());
    }

    @Test
    void setStateAndGetStateRoundTrip() {
        final ViewModel<String> viewModel = new ViewModel<>("dashboard");

        viewModel.setState("hello");

        assertEquals("hello", viewModel.getState());
    }

    @Test
    void firePropertyChangedWithNameNotifiesListeners() throws InterruptedException {
        final ViewModel<String> viewModel = new ViewModel<>("dashboard");
        viewModel.setState("initial");
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] receivedPropertyName = {null};

        viewModel.addPropertyChangeListener(event -> {
            receivedPropertyName[0] = event.getPropertyName();
            latch.countDown();
        });

        viewModel.firePropertyChanged("customProperty");

        if (!latch.await(AWAIT_SECONDS, TimeUnit.SECONDS)) {
            fail("Expected the listener to be notified within " + AWAIT_SECONDS + " seconds.");
        }
        assertEquals("customProperty", receivedPropertyName[0]);
    }
}
