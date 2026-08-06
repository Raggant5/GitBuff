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
import org.jfree.data.general.DefaultPieDataset;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import use_case.dashboard.MacroData;

/**
 * The View for displaying user dashboard analytics and status.
 */
public class DashboardView extends JPanel
        implements PropertyChangeListener {

    private static final int PANEL_PADDING = 20;
    private static final int HORIZONTAL_GAP = 20;
    private static final int VERTICAL_GAP = 20;

    private static final int CHART_WIDTH = 420;
    private static final int CHART_HEIGHT = 260;

    private final String viewName = "dashboard";

    private final JPanel calorieChartContainer =
            new JPanel(new BorderLayout());

    private final JPanel calendarContainer =
            new JPanel(new BorderLayout());

    private final JPanel macroChartContainer =
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

        final JPanel dashboardContent = new JPanel(
                new BorderLayout(
                        HORIZONTAL_GAP,
                        VERTICAL_GAP
                )
        );

        final JPanel topPanel = new JPanel(
                new GridLayout(
                        1,
                        2,
                        HORIZONTAL_GAP,
                        VERTICAL_GAP
                )
        );

        this.calorieChartContainer.setBorder(
                BorderFactory.createTitledBorder(
                        "Calories"
                )
        );

        this.calendarContainer.setBorder(
                BorderFactory.createTitledBorder(
                        "Calendar"
                )
        );

        this.macroChartContainer.setBorder(
                BorderFactory.createTitledBorder(
                        "Today's Macronutrients"
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

        topPanel.add(this.calorieChartContainer);
        topPanel.add(this.calendarContainer);

        dashboardContent.add(
                topPanel,
                BorderLayout.NORTH
        );

        dashboardContent.add(
                this.macroChartContainer,
                BorderLayout.CENTER
        );

        this.add(
                dashboardContent,
                BorderLayout.CENTER
        );

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
        updateCalorieChart(state);
        updateMacroChart(state);

        this.revalidate();
        this.repaint();
    }

    private void updateCalorieChart(
            final DashboardState state
    ) {
        this.calorieChartContainer.removeAll();

        if (state.getErrorMessage() != null) {
            this.calorieChartContainer.add(
                    new JLabel(
                            state.getErrorMessage(),
                            SwingConstants.CENTER
                    ),
                    BorderLayout.CENTER
            );
        }
        else if (state.getCaloriesByDate().isEmpty()) {
            this.calorieChartContainer.add(
                    new JLabel(
                            "No calorie data available.",
                            SwingConstants.CENTER
                    ),
                    BorderLayout.CENTER
            );
        }
        else {
            this.calorieChartContainer.add(
                    createCaloriesChart(
                            state.getCaloriesByDate()
                    ),
                    BorderLayout.CENTER
            );
        }

        this.calorieChartContainer.revalidate();
        this.calorieChartContainer.repaint();
    }

    private void updateMacroChart(
            final DashboardState state
    ) {
        this.macroChartContainer.removeAll();

        final MacroData macroData =
                state.getMacroData();

        if (state.getErrorMessage() != null) {
            this.macroChartContainer.add(
                    new JLabel(
                            state.getErrorMessage(),
                            SwingConstants.CENTER
                    ),
                    BorderLayout.CENTER
            );
        }
        else if (macroData == null
                || hasNoMacroData(macroData)) {
            this.macroChartContainer.add(
                    new JLabel(
                            "No macronutrient data available for today.",
                            SwingConstants.CENTER
                    ),
                    BorderLayout.CENTER
            );
        }
        else {
            this.macroChartContainer.add(
                    createMacroChart(macroData),
                    BorderLayout.CENTER
            );
        }

        this.macroChartContainer.revalidate();
        this.macroChartContainer.repaint();
    }

    private boolean hasNoMacroData(
            final MacroData macroData
    ) {
        return macroData.getProtein() == 0
                && macroData.getCarbs() == 0
                && macroData.getFat() == 0;
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

    private ChartPanel createMacroChart(
            final MacroData macroData
    ) {
        final DefaultPieDataset<String> dataset =
                new DefaultPieDataset<>();

        dataset.setValue(
                "Protein",
                macroData.getProtein()
        );

        dataset.setValue(
                "Carbohydrates",
                macroData.getCarbs()
        );

        dataset.setValue(
                "Fat",
                macroData.getFat()
        );

        final JFreeChart chart =
                ChartFactory.createPieChart(
                        "Today's Macronutrient Breakdown",
                        dataset,
                        true,
                        true,
                        false
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