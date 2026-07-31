package app;

import java.sql.SQLException;

import javax.swing.JFrame;

import data_access.Datainitializer;

/**
 * The Main class of our application.
 */
public class Main {
    /**
     * Builds and runs the CA architecture of the application.
     * @param args unused arguments
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();

        final JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addMainViews()
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
