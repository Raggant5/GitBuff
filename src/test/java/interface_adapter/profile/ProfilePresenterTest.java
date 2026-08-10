package interface_adapter.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;
import entity.User;
import use_case.profile.EditProfileOutputData;

/**
 * Unit tests for the Profile Presenter.
 */
public class ProfilePresenterTest {

    private static final float TEST_HEIGHT = 1.8f;
    private static final float TEST_WEIGHT = 80f;
    private static final int TEST_DURATION = 45;

    private static User buildUser() {
        final User user = new CommonUser("aahir", "password");
        user.setHeight(TEST_HEIGHT);
        user.setWeight(TEST_WEIGHT);
        user.setActivityLevel(ActivityLevel.VERY_ACTIVE);
        user.setGoal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN);
        user.setProfilePicturePath("/tmp/pic.png");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setGender(Gender.MALE);
        user.setBio("Hello world");
        user.setPreferredUnitSystem(UnitSystem.METRIC);
        user.setEquipment(new HashSet<>());
        user.setDietaryRestrictions(new HashSet<>());
        user.setPreferredWorkoutDays(new HashSet<>());
        user.setPreferredWorkoutDurationMinutes(TEST_DURATION);
        user.setPrivacySettings(new HashSet<>());
        return user;
    }

    @Test
    public void prepareSuccessViewMapsEveryFieldOntoProfileState() {
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        final ProfilePresenter presenter = new ProfilePresenter(profileViewModel);

        final User user = buildUser();

        presenter.prepareSuccessView(new EditProfileOutputData(user));

        final ProfileState state = profileViewModel.getState();
        assertEquals("aahir", state.getUsername());
        assertEquals(String.valueOf(TEST_HEIGHT), state.getHeightText());
        assertEquals(String.valueOf(TEST_WEIGHT), state.getWeightText());
        assertEquals(ActivityLevelOption.VERY_ACTIVE, state.getActivityLevel());
        assertEquals(FitnessGoalOption.MUSCLE_AND_STRENGTH_GAIN, state.getGoal());
        assertEquals("/tmp/pic.png", state.getProfilePicturePath());
        assertEquals(LocalDate.of(2000, 1, 1), state.getDateOfBirth());
        assertEquals(GenderOption.MALE, state.getGender());
        assertEquals("Hello world", state.getBio());
        assertEquals(UnitSystemOption.METRIC, state.getPreferredUnitSystem());
        assertEquals(TEST_DURATION, state.getPreferredWorkoutDurationMinutes());
        assertEquals("Profile saved.", state.getSaveConfirmation());
        assertNull(state.getProfileError());
    }

    @Test
    public void prepareFailViewSetsErrorAndClearsConfirmation() {
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        profileViewModel.getState().setSaveConfirmation("Profile saved.");
        final ProfilePresenter presenter = new ProfilePresenter(profileViewModel);

        presenter.prepareFailView("Height must be greater than zero.");

        final ProfileState state = profileViewModel.getState();
        assertEquals("Height must be greater than zero.", state.getProfileError());
        assertNull(state.getSaveConfirmation());
    }
}
