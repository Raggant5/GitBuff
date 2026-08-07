package app;

import java.sql.SQLException;

import javax.swing.JFrame;

import data_access.Datainitializer;

/**
 * The Main entry point class of the GitBuff application.
 */
public final class Main {

    private Main() {
        // Utility class constructor
    }

    /**
     * Builds and runs the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final AppBuilder appBuilder = new AppBuilder();

        final JFrame application = appBuilder
                .addCalendarPanel()
                .addLoginView()
                .addSignupView()
                .addMainViews()
                .addDashboardUseCase()
                .addCalendarUseCase()
                .addNavbarView()
                .addShellView()
                .addSignupUseCase()
                .addRecommendationUseCase()
                .addProfileUseCase()
                .addLoginUseCase()
                .addLogoutUseCase()
                .addAddFoodUseCase()
                .addAddMealUseCase()
                .addEditFoodUseCase()
                .addEditMealUseCase()
                .addDeleteMealUseCase()
                .addDeleteFoodUseCase()
                .addSearchFoodUseCase()
                .addAddExerciseUseCase()
                .addAddWorkoutUseCase()
                .addEditExerciseUseCase()
                .addEditWorkoutUseCase()
                .addDeleteWorkoutUseCase()
                .addDeleteExerciseUseCase()
                .build();

        try {
            Datainitializer.initialize();
        }
        catch (SQLException exc) {
            exc.printStackTrace();
        }

        application.pack();
        application.setVisible(true);
    }
}
