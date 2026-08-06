package use_case.dashboard;

import java.time.LocalDate;
import java.util.Map;

/**
 * Data access interface for dashboard analytics.
 */
public interface DashboardDataAccessInterface {

    /**
     * Gets total calories grouped by date for a user.
     *
     * @param userId ID of the logged-in user
     * @return calories grouped by date
     */
    Map<LocalDate, Double> getCaloriesByDate(String userId);

    /**
     * Gets today's macronutrient totals for a user.
     *
     * @param userId ID of the logged-in user
     * @return today's protein, carbohydrate, and fat totals
     */
    MacroData getMacrosForToday(String userId);
}
