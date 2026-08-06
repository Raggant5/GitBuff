package use_case.dashboard;

import java.time.LocalDate;
import java.util.Map;

/**
 * Output data containing calories consumed per day.
 */
public class DashboardOutputData {

    private final Map<LocalDate, Double> caloriesByDate;

    public DashboardOutputData(
            final Map<LocalDate, Double> caloriesByDate
    ) {
        this.caloriesByDate = caloriesByDate;
    }

    public Map<LocalDate, Double> getCaloriesByDate() {
        return caloriesByDate;
    }
}