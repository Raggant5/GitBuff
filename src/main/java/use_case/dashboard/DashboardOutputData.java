package use_case.dashboard;

import java.time.LocalDate;
import java.util.Map;

/**
 * Output data for dashboard analytics.
 */
public class DashboardOutputData {

    private final Map<LocalDate, Double> caloriesByDate;
    private final MacroData macroData;

    /**
     * Constructs dashboard output data.
     *
     * @param caloriesByDate calories grouped by date
     * @param macroData today's macronutrient totals
     */
    public DashboardOutputData(
            final Map<LocalDate, Double> caloriesByDate,
            final MacroData macroData
    ) {
        this.caloriesByDate = caloriesByDate;
        this.macroData = macroData;
    }

    public Map<LocalDate, Double> getCaloriesByDate() {
        return this.caloriesByDate;
    }

    public MacroData getMacroData() {
        return this.macroData;
    }
}
