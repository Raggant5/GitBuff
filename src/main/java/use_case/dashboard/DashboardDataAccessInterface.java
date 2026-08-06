package use_case.dashboard;

import java.time.LocalDate;
import java.util.Map;

public interface DashboardDataAccessInterface {

    Map<LocalDate, Double> getCaloriesByDate(String userId);
}
