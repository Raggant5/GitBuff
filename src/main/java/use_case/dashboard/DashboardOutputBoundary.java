package use_case.dashboard;

/**
 * Output boundary for dashboard data.
 */
public interface DashboardOutputBoundary {

    /**
     * Prepares the calorie data for display.
     *
     * @param outputData dashboard output data
     */
    void prepareSuccessView(DashboardOutputData outputData);

    /**
     * Prepares an error message for display.
     *
     * @param errorMessage explanation of the error
     */
    void prepareFailView(String errorMessage);
}
