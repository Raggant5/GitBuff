package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

import entity.CalendarEvent;
import interface_adapter.calendar.CalendarState;
import interface_adapter.calendar.CalendarViewModel;

public class CalendarPanel extends JPanel implements PropertyChangeListener {
    private final CalendarViewModel calendarViewModel;

    public CalendarPanel(CalendarViewModel calendarViewModel) {
        this.calendarViewModel = calendarViewModel;
        this.calendarViewModel.addPropertyChangeListener(this);

        displayState(this.calendarViewModel.getState());
    }

    private void displayState(CalendarState state) {
        removeAll();

        // TODO: put everything together and display it on the screen

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
            JLabel toDoLabel = new JLabel(String.valueOf(dayNumber));
            dayBox.add(toDoLabel);
        }

        return dayBox;
    }

    private JPanel weekDayLabel(String weekDay) {
        JPanel weekDayBox = new JPanel();

        weekDayBox.setLayout(new BoxLayout(weekDayBox, BoxLayout.Y_AXIS));

        weekDayBox.setBackground(Color.WHITE);

        weekDayBox.setBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        weekDayBox.setPreferredSize(new Dimension(100, 30));

        JLabel weekDayLabel = new JLabel(weekDay);
        weekDayBox.add(weekDayLabel);

        return weekDayBox;
    }

    private JPanel monthBox(String month) {
        JPanel monthBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        monthBox.setLayout(new BoxLayout(monthBox, BoxLayout.X_AXIS));

        JLabel monthLabel = new JLabel(month);
        monthBox.add(monthLabel);

        return monthBox;
    }

    private JPanel mainBox(List<CalendarEvent> calendarEvents) {
        JPanel mainBox = new JPanel();

        mainBox.setLayout(new BoxLayout(mainBox, BoxLayout.Y_AXIS));

        List<JPanel> weekDays = new ArrayList<>();

        // TODO: make the mainbox of putting everything together
        return mainBox;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        CalendarState state = (CalendarState) event.getNewValue();
        displayState(state);
    }
}
