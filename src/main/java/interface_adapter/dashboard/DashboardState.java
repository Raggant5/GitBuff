package interface_adapter.dashboard;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * State for the dashboard view.
 */
public class DashboardState {

    private Map<LocalDate, Double> caloriesByDate =
            new LinkedHashMap<>();

    private String errorMessage;

    public Map<LocalDate, Double> getCaloriesByDate() {
        return caloriesByDate;
    }

    public void setCaloriesByDate(
            final Map<LocalDate, Double> caloriesByDate
    ) {
        this.caloriesByDate = caloriesByDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}