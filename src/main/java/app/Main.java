package app;

import data_access.Datainitializer;

import javax.swing.JFrame;

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
                .build();
        try {
            Datainitializer.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        application.pack();
        application.setVisible(true);
    }
}
