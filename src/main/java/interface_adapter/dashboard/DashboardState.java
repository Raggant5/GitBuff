package interface_adapter.dashboard;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import use_case.dashboard.MacroData;

/**
 * State for the dashboard view.
 */
public class DashboardState {

    private Map<LocalDate, Double> caloriesByDate =
            new LinkedHashMap<>();

    private MacroData macroData =
            new MacroData(0, 0, 0);

    private String errorMessage;

    public Map<LocalDate, Double> getCaloriesByDate() {
        return this.caloriesByDate;
    }

    public void setCaloriesByDate(
            final Map<LocalDate, Double> caloriesByDate
    ) {
        this.caloriesByDate = caloriesByDate;
    }

    public MacroData getMacroData() {
        return this.macroData;
    }

    public void setMacroData(
            final MacroData macroData
    ) {
        this.macroData = macroData;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(
            final String errorMessage
    ) {
        this.errorMessage = errorMessage;
    }
}