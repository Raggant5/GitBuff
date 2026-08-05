package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import entity.CalendarEvent;
import interface_adapter.calendar.CalendarState;
import interface_adapter.calendar.CalendarViewModel;

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

        List<CalendarEvent> eventsList = state.getCalendarEvents();
        Map<LocalDate, List<CalendarEvent>> eventsMap = new HashMap<>();

        for (CalendarEvent event : eventsList) {
            if (eventsMap.containsKey(event.getActivityDate())) {
                eventsMap.get(event.getActivityDate()).add(event);
            }
            else {
                eventsMap.put(event.getActivityDate(), new ArrayList<>());
                eventsMap.get(event.getActivityDate()).add(event);
            }
        }

        add(mainBox(eventsMap), BorderLayout.CENTER);

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
        JPanel weekDaysBox = new JPanel(new FlowLayout(FlowLayout.CENTER));

        weekDaysBox.setLayout(new BoxLayout(weekDaysBox, BoxLayout.X_AXIS));

        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday",  "Thursday", "Friday", "Saturday"};

        for (int i = 0; i < 7; i++) {
            JPanel weekDayBox = new JPanel(new FlowLayout());

            weekDayBox.setBackground(Color.WHITE);
            weekDayBox.setBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            weekDayBox.setPreferredSize(new Dimension(100, 30));

            JLabel weekDayLabel = new JLabel(weekDays[i]);
            weekDayBox.add(weekDayLabel);

            weekDaysBox.add(weekDayBox);
        }

        return weekDaysBox;
    }

    private JPanel monthBox(String month) {
        JPanel monthBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        monthBox.setLayout(new BoxLayout(monthBox, BoxLayout.X_AXIS));

        JLabel monthLabel = new JLabel(month);
        monthBox.add(monthLabel);

        return monthBox;
    }

    private int whichDayMonthBegins() {
        YearMonth currentMonth = YearMonth.now();
        DayOfWeek firstWeekday = currentMonth.atDay(1).getDayOfWeek();

        return firstWeekday.getValue() % 7;
    }

    private JPanel monthBox(Map<LocalDate, List<CalendarEvent>> calendarEvents) {
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
                for (CalendarEvent event : calendarEvents.get(date))
                    toDos.add(event.getTitle());
            }

            monthBox.add(dayBox(day, toDos));
        }
        return monthBox;
    }

    private JPanel mainBox(Map<LocalDate, List<CalendarEvent>> calendarEvents) {
        JPanel mainBox = new JPanel();

        mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.Y_AXIS));
        mainBox.add(monthBox(LocalDate.now().getMonth().toString()));
        mainBox.add(weekDayBox());
        mainBox.add(monthBox(calendarEvents));

        return mainBox;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        CalendarState state = (CalendarState) event.getNewValue();
        displayState(state);
    }
}
