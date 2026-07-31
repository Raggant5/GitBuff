package app;

import javax.swing.JFrame;

import data_access.Datainitializer;

/**
 * The Main class of our application.
 */
public class Main {

    /**
     * Builds and runs the Clean Architecture implementation of the application.
     *
     * @param args unused arguments
     */
    public static void main(final String[] args) {
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
                .build();

        try {
            Datainitializer.initialize();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }

        application.pack();
        application.setVisible(true);
    }
}
