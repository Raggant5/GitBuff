package view;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.awt.Container;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import interface_adapter.calendar.CalendarEventDisplayData;
import interface_adapter.calendar.CalendarViewModel;

class CalendarPanelTest {

    @Test
    void panelRendersEventsErrorsAndPropertyChanges() throws Exception {
        final CalendarViewModel viewModel = new CalendarViewModel();
        viewModel.getState().setCalendarEvents(List.of(
                event("Current workout", LocalDate.now()),
                event("No date", null)));
        viewModel.getState().setErrorMessage("Calendar unavailable");
        final AtomicReference<CalendarPanel> panelReference = new AtomicReference<>();

        SwingUtilities.invokeAndWait(
                () -> panelReference.set(new CalendarPanel(viewModel)));

        final CalendarPanel panel = panelReference.get();
        List<String> labels = labelTexts(panel);
        assertTrue(labels.contains(LocalDate.now().getMonth().toString()));
        assertTrue(labels.contains("Sun"));
        assertTrue(labels.contains("Sat"));
        assertTrue(labels.contains("Current workout"));
        assertTrue(labels.contains("Calendar unavailable"));

        SwingUtilities.invokeAndWait(() -> {
            viewModel.getState().setCalendarEvents(List.of(
                    event("Updated meal", LocalDate.now())));
            viewModel.getState().setErrorMessage(null);
            viewModel.firePropertyChanged();
        });

        labels = labelTexts(panel);
        assertTrue(labels.contains("Updated meal"));
        assertFalse(labels.contains("Current workout"));
        assertFalse(labels.contains("Calendar unavailable"));
    }

    private static CalendarEventDisplayData event(String title, LocalDate date) {
        return new CalendarEventDisplayData(
                "event-id", "amir", title, "description", date);
    }

    private static List<String> labelTexts(Component component) {
        final List<String> labels = new ArrayList<>();
        collectLabelTexts(component, labels);
        return labels;
    }

    private static void collectLabelTexts(Component component, List<String> labels) {
        if (component instanceof JLabel label) {
            labels.add(label.getText());
        }
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                collectLabelTexts(child, labels);
            }
        }
    }
}
