package use_case.dashboard;

/**
 * Input boundary for loading dashboard data.
 */
public interface DashboardInputBoundary {

    /**
     * Loads dashboard data for the specified user.
     *
     * @param userId ID of the logged-in user
     */
    void execute(String userId);
}