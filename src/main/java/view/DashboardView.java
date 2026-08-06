package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;

/**
 * The View for displaying user dashboard analytics and status.
 */
public class DashboardView extends JPanel
        implements PropertyChangeListener {

    private static final int HORIZONTAL_GAP = 20;
    private static final int VERTICAL_GAP = 20;
    private static final int PANEL_PADDING = 20;

    private static final int CHART_WIDTH = 420;
    private static final int CHART_HEIGHT = 300;

    private final String viewName = "dashboard";

    private final JPanel chartContainer =
            new JPanel(new BorderLayout());

    private final JPanel calendarContainer =
            new JPanel(new BorderLayout());

    /**
     * Constructs a DashboardView instance.
     *
     * @param dashboardViewModel view model managing dashboard state
     */
    public DashboardView(
            final DashboardViewModel dashboardViewModel
    ) {
        dashboardViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBorder(
                BorderFactory.createEmptyBorder(
                        PANEL_PADDING,
                        PANEL_PADDING,
                        PANEL_PADDING,
                        PANEL_PADDING
                )
        );

        final JPanel contentPanel = new JPanel(
                new GridLayout(
                        1,
                        2,
                        HORIZONTAL_GAP,
                        VERTICAL_GAP
                )
        );

        this.chartContainer.setBorder(
                BorderFactory.createTitledBorder(
                        "Calories"
                )
        );

        this.calendarContainer.setBorder(
                BorderFactory.createTitledBorder(
                        "Calendar"
                )
        );

        final JLabel calendarPlaceholder =
                new JLabel(
                        "Calendar goes here",
                        SwingConstants.CENTER
                );

        this.calendarContainer.add(
                calendarPlaceholder,
                BorderLayout.CENTER
        );

        contentPanel.add(this.chartContainer);
        contentPanel.add(this.calendarContainer);

        this.add(contentPanel, BorderLayout.NORTH);

        updateDashboard(
                dashboardViewModel.getState()
        );
    }

    @Override
    public void propertyChange(
            final PropertyChangeEvent event
    ) {
        final DashboardState state =
                (DashboardState) event.getNewValue();

        updateDashboard(state);
    }

    private void updateDashboard(
            final DashboardState state
    ) {
        this.chartContainer.removeAll();

        if (state.getErrorMessage() != null) {
            final JLabel errorLabel = new JLabel(
                    state.getErrorMessage(),
                    SwingConstants.CENTER
            );

            this.chartContainer.add(
                    errorLabel,
                    BorderLayout.CENTER
            );
        }
        else if (state.getCaloriesByDate().isEmpty()) {
            final JLabel emptyLabel = new JLabel(
                    "No calorie data available.",
                    SwingConstants.CENTER
            );

            this.chartContainer.add(
                    emptyLabel,
                    BorderLayout.CENTER
            );
        }
        else {
            this.chartContainer.add(
                    createCaloriesChart(
                            state.getCaloriesByDate()
                    ),
                    BorderLayout.CENTER
            );
        }

        this.chartContainer.revalidate();
        this.chartContainer.repaint();
    }

    private ChartPanel createCaloriesChart(
            final Map<LocalDate, Double> caloriesByDate
    ) {
        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        for (Map.Entry<LocalDate, Double> entry
                : caloriesByDate.entrySet()) {

            dataset.addValue(
                    entry.getValue(),
                    "Calories",
                    entry.getKey().toString()
            );
        }

        final JFreeChart chart =
                ChartFactory.createLineChart(
                        "Calories Eaten Per Day",
                        "Date",
                        "Calories",
                        dataset
                );

        final ChartPanel chartPanel =
                new ChartPanel(chart);

        chartPanel.setPreferredSize(
                new Dimension(
                        CHART_WIDTH,
                        CHART_HEIGHT
                )
        );

        return chartPanel;
    }

    /**
     * Gets the view name.
     *
     * @return view name string
     */
    public String getViewName() {
        return this.viewName;
    }
}