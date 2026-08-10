package use_case.share.report;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import entity.CommonUser;
import entity.FitnessGoal;
import entity.User;

class ProfileReportSectionTest {

    @Test
    void renderWithBioAndGoalIncludesThem() {
        final User user = new CommonUser("aahir", "password");
        user.setBio("Fitness enthusiast");
        user.setGoal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN);

        final String rendered = new ProfileReportSection(user).render();

        assertTrue(rendered.contains("Fitness enthusiast"));
        assertTrue(rendered.contains(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN.toString()));
    }

    @Test
    void renderWithoutBioOrGoalUsesDefaults() {
        final User user = new CommonUser("aahir", "password");
        user.setBio(null);
        user.setGoal(null);

        final String rendered = new ProfileReportSection(user).render();

        assertTrue(rendered.contains("Bio: None"));
        assertTrue(rendered.contains("Goal: Not set"));
    }

    @Test
    void hasContentIsAlwaysTrue() {
        final User user = new CommonUser("aahir", "password");

        assertTrue(new ProfileReportSection(user).hasContent());
    }
}
