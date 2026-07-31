package app;

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
     * Builds and runs the Clean Architecture implementation of the application.
     *
     * @param args command line arguments
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
            // Gracefully log initialization exception
        }

        application.pack();
        application.setVisible(true);
    }
}
