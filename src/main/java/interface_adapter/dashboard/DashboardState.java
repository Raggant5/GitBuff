package interface_adapter.dashboard;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * State for the dashboard view.
 */
public class DashboardState {

    private Map<LocalDate, Double> caloriesByDate = new LinkedHashMap<>();
    private MacroDisplayData macroData = new MacroDisplayData(0, 0, 0);
    private String errorMessage;

    /**
     * Gets calories map by date.
     *
     * @return map of dates to calorie totals.
     */
    public Map<LocalDate, Double> getCaloriesByDate() {
        return this.caloriesByDate;
    }

    /**
     * Sets calories map by date.
     *
     * @param caloriesByDate map of dates to calorie totals.
     */
    public void setCaloriesByDate(final Map<LocalDate, Double> caloriesByDate) {
        this.caloriesByDate = caloriesByDate;
    }

    /**
     * Gets macronutrient data for today.
     *
     * @return MacroDisplayData object.
     */
    public MacroDisplayData getMacroData() {
        return this.macroData;
    }

    /**
     * Sets macronutrient data for today.
     *
     * @param macroData MacroDisplayData object.
     */
    public void setMacroData(final MacroDisplayData macroData) {
        this.macroData = macroData;
    }

    /**
     * Gets error message.
     *
     * @return error message string.
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Sets error message.
     *
     * @param errorMessage error message string.
     */
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
