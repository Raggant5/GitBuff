package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import interface_adapter.dashboard.DashboardState;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.share.ShareProgressController;
import use_case.dashboard.MacroData;

/**
 * The View for displaying user dashboard analytics and status.
 */
public class DashboardView extends JPanel implements PropertyChangeListener {

    private static final int PANEL_PADDING = 10;
    private static final int HORIZONTAL_GAP = 10;
    private static final int VERTICAL_GAP = 10;

    private static final int CHART_MIN_WIDTH = 250;
    private static final int CHART_MIN_HEIGHT = 150;
    private static final double SPLIT_PANE_RESIZE_WEIGHT = 0.35;

    private final String viewName = "dashboard";

    private final JPanel calorieChartContainer = new JPanel(new BorderLayout());
    private final JPanel calendarContainer = new JPanel(new BorderLayout());
    private final JPanel macroChartContainer = new JPanel(new BorderLayout());

    private final JButton shareProgressButton = new JButton("Share Progress");
    private ShareProgressController shareProgressController;

    /**
     * Constructs a DashboardView instance.
     *
     * @param dashboardViewModel view model managing dashboard state
     * @param calendarPanel calendar component
     */
    public DashboardView(final DashboardViewModel dashboardViewModel, final CalendarPanel calendarPanel) {
        dashboardViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(
                PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));

        // Share Progress Button top-right
        final JPanel topActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topActionPanel.add(this.shareProgressButton);

        this.shareProgressButton.addActionListener(evt -> {
            if (this.shareProgressController != null) {
                this.shareProgressController.executeFetchSharePreview();
            }
        });

        // Top section: Combine both Nutrition Charts side-by-side
        final JPanel chartsTopSection = new JPanel(new GridLayout(1, 2, HORIZONTAL_GAP, VERTICAL_GAP));

        this.calorieChartContainer.setBorder(BorderFactory.createTitledBorder("Calories"));
        this.macroChartContainer.setBorder(BorderFactory.createTitledBorder("Today's Macronutrients"));
        this.calendarContainer.setBorder(BorderFactory.createTitledBorder("Calendar Schedule"));

        chartsTopSection.add(this.calorieChartContainer);
        chartsTopSection.add(this.macroChartContainer);

        // Bottom section: Large Calendar panel
        this.calendarContainer.add(calendarPanel, BorderLayout.CENTER);

        // Vertical Split Pane: Top gets ~35% height, Bottom Calendar gets ~65% height
        final JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, chartsTopSection, this.calendarContainer);
        splitPane.setResizeWeight(SPLIT_PANE_RESIZE_WEIGHT);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);

        this.add(topActionPanel, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);

        updateDashboard(dashboardViewModel.getState());
    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        final DashboardState state = (DashboardState) event.getNewValue();
        updateDashboard(state);
    }

    private void updateDashboard(final DashboardState state) {
        updateCalorieChart(state);
        updateMacroChart(state);

        this.revalidate();
        this.repaint();
    }

    private void updateCalorieChart(final DashboardState state) {
        this.calorieChartContainer.removeAll();

        if (state.getErrorMessage() != null) {
            this.calorieChartContainer.add(
                    new JLabel(state.getErrorMessage(), SwingConstants.CENTER), BorderLayout.CENTER);
        }
        else if (state.getCaloriesByDate().isEmpty()) {
            this.calorieChartContainer.add(
                    new JLabel("No calorie data available.", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        else {
            this.calorieChartContainer.add(
                    createCaloriesChart(state.getCaloriesByDate()), BorderLayout.CENTER);
        }

        this.calorieChartContainer.revalidate();
        this.calorieChartContainer.repaint();
    }

    private void updateMacroChart(final DashboardState state) {
        this.macroChartContainer.removeAll();

        final MacroData macroData = state.getMacroData();

        if (state.getErrorMessage() != null) {
            this.macroChartContainer.add(
                    new JLabel(state.getErrorMessage(), SwingConstants.CENTER), BorderLayout.CENTER);
        }
        else if (macroData == null || hasNoMacroData(macroData)) {
            this.macroChartContainer.add(
                    new JLabel("No macronutrient data available for today.", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        else {
            this.macroChartContainer.add(createMacroChart(macroData), BorderLayout.CENTER);
        }

        this.macroChartContainer.revalidate();
        this.macroChartContainer.repaint();
    }

    private boolean hasNoMacroData(final MacroData macroData) {
        return macroData.getProtein() == 0 && macroData.getCarbs() == 0 && macroData.getFat() == 0;
    }

    private ChartPanel createCaloriesChart(final Map<LocalDate, Double> caloriesByDate) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (final Map.Entry<LocalDate, Double> entry : caloriesByDate.entrySet()) {
            dataset.addValue(entry.getValue(), "Calories", entry.getKey().toString());
        }

        final JFreeChart chart = ChartFactory.createBarChart(
                "Calories Eaten Per Day", "Date", "Calories", dataset);

        final CategoryPlot plot = chart.getCategoryPlot();
        final NumberAxis calorieAxis = (NumberAxis) plot.getRangeAxis();

        calorieAxis.setAutoRange(true);
        calorieAxis.setAutoRangeIncludesZero(true);
        calorieAxis.setAutoRangeStickyZero(true);
        calorieAxis.setLowerMargin(0.0);
        calorieAxis.setUpperMargin(0.15);
        calorieAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMinimumDrawWidth(CHART_MIN_WIDTH);
        chartPanel.setMinimumDrawHeight(CHART_MIN_HEIGHT);
        return chartPanel;
    }

    private ChartPanel createMacroChart(final MacroData macroData) {
        final DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        dataset.setValue("Protein", macroData.getProtein());
        dataset.setValue("Carbohydrates", macroData.getCarbs());
        dataset.setValue("Fat", macroData.getFat());

        final JFreeChart chart = ChartFactory.createPieChart(
                "Today's Macronutrient Breakdown", dataset, true, true, false);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMinimumDrawWidth(CHART_MIN_WIDTH);
        chartPanel.setMinimumDrawHeight(CHART_MIN_HEIGHT);
        return chartPanel;
    }

    public String getViewName() {
        return this.viewName;
    }

    /**
     * Sets the controller for share progress.
     *
     * @param shareProgressController controller instance
     */
    public void setShareProgressController(final ShareProgressController shareProgressController) {
        this.shareProgressController = shareProgressController;
    }
}
