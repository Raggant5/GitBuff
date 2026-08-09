package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import interface_adapter.calendar.CalendarEventDisplayData;
import interface_adapter.calendar.CalendarState;
import interface_adapter.calendar.CalendarViewModel;

/**
 * Swing panel rendering the current month's calendar with each day's scheduled events.
 *
 * <p>Reads {@link CalendarEventDisplayData} from {@link CalendarState}, rather than the
 * {@code entity.CalendarEvent} entity, so this view-layer class does not depend on the entity
 * layer.
 */
public class CalendarPanel extends JPanel implements PropertyChangeListener {
    private final CalendarViewModel calendarViewModel;

    public CalendarPanel(CalendarViewModel calendarViewModel) {
        this.calendarViewModel = calendarViewModel;
        this.calendarViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        displayState(this.calendarViewModel.getState());
    }

    private void displayState(CalendarState state) {
        removeAll();

        List<CalendarEventDisplayData> eventsList = state.getCalendarEvents();
        Map<LocalDate, List<CalendarEventDisplayData>> eventsMap = new HashMap<>();

        for (CalendarEventDisplayData event : eventsList) {
            if (event.getActivityDate() == null) {
                continue;
            }
            if (eventsMap.containsKey(event.getActivityDate())) {
                eventsMap.get(event.getActivityDate()).add(event);
            }
            else {
                eventsMap.put(event.getActivityDate(), new ArrayList<>());
                eventsMap.get(event.getActivityDate()).add(event);
            }
        }

        add(mainBox(eventsMap), BorderLayout.CENTER);

        if (state.getErrorMessage() != null && !state.getErrorMessage().isBlank()) {
            final JLabel errorLabel = new JLabel(
                    state.getErrorMessage(), SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            add(errorLabel, BorderLayout.SOUTH);
        }

        revalidate();
        repaint();

    }

    private JPanel dayBox(int dayNumber, List<String> toDos) {
        JPanel dayBox = new JPanel();

        dayBox.setLayout(new BoxLayout(dayBox, BoxLayout.Y_AXIS));

        if (dayNumber % 2 == 0)
            dayBox.setBackground(Color.WHITE);
        else
            dayBox.setBackground(new Color(6, 158, 208));

        dayBox.setBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        dayBox.setPreferredSize(new Dimension(100, 80));

        JLabel dayNumberLabel = new JLabel(String.valueOf(dayNumber));
        dayBox.add(dayNumberLabel);

        for (String toDo: toDos) {
            JLabel toDoLabel = new JLabel(toDo);
            dayBox.add(toDoLabel);
        }

        return dayBox;
    }

    private JPanel weekDayBox() {
        final JPanel weekDaysBox = new JPanel(new GridLayout(1, 7));
        final String[] weekDays = {
                "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
        };

        for (String weekDay : weekDays) {
            final JLabel weekDayLabel = new JLabel(
                    weekDay, SwingConstants.CENTER);
            weekDayLabel.setOpaque(true);
            weekDayLabel.setBackground(Color.WHITE);
            weekDayLabel.setBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            weekDaysBox.add(weekDayLabel);
        }

        return weekDaysBox;
    }

    private JPanel monthBox(String month) {
        final JPanel monthBox = new JPanel(new BorderLayout());
        final JLabel monthLabel = new JLabel(month, SwingConstants.CENTER);
        monthBox.add(monthLabel, BorderLayout.CENTER);

        return monthBox;
    }

    private int whichDayMonthBegins() {
        YearMonth currentMonth = YearMonth.now();
        DayOfWeek firstWeekday = currentMonth.atDay(1).getDayOfWeek();

        return firstWeekday.getValue() % 7;
    }

    private JPanel monthBox(Map<LocalDate, List<CalendarEventDisplayData>> calendarEvents) {
        JPanel monthBox = new JPanel(new GridLayout(0, 7));

        int dayMonthBegins = whichDayMonthBegins();

        for (int i = 0; i < dayMonthBegins; i++) {
            monthBox.add(new JPanel());
        }

        YearMonth month = YearMonth.now();

        for (int day = 1; day <= month.lengthOfMonth(); day++) {
            LocalDate date = month.atDay(day);
            List<String> toDos = new ArrayList<>();

            if (calendarEvents.containsKey(date)) {
                for (CalendarEventDisplayData event : calendarEvents.get(date))
                    toDos.add(event.getTitle());
            }

            monthBox.add(dayBox(day, toDos));
        }
        return monthBox;
    }

    private JPanel mainBox(Map<LocalDate, List<CalendarEventDisplayData>> calendarEvents) {
        final JPanel mainBox = new JPanel(new BorderLayout());
        final JPanel calendarBody = new JPanel(new BorderLayout());

        calendarBody.add(weekDayBox(), BorderLayout.NORTH);
        calendarBody.add(monthBox(calendarEvents), BorderLayout.CENTER);

        mainBox.add(
                monthBox(LocalDate.now().getMonth().toString()),
                BorderLayout.NORTH);
        mainBox.add(calendarBody, BorderLayout.CENTER);

        return mainBox;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        CalendarState state = (CalendarState) event.getNewValue();
        displayState(state);
    }
}


