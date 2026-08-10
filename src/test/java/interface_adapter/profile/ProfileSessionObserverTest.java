package interface_adapter.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import entity.ActivityLevel;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;
import org.junit.jupiter.api.Test;
import use_case.login.LoginOutputData;
import use_case.session.UserLoggedInEvent;

class ProfileSessionObserverTest {

    @Test
    void onUserLoggedInPopulatesProfileStateFromLoginData() {
        final ProfileViewModel profileViewModel = new ProfileViewModel();
        final ProfileSessionObserver observer = new ProfileSessionObserver(profileViewModel);

        final LoginOutputData loginOutputData = new LoginOutputData(
                "amir", 1.8f, 80f, ActivityLevel.VERY_ACTIVE, FitnessGoal.MUSCLE_AND_STRENGTH_GAIN,
                "/tmp/pic.png", LocalDate.of(2000, 1, 1), Gender.MALE, "Hello world", UnitSystem.METRIC,
                Set.of(Equipment.BARBELL), Set.of(DietaryRestriction.VEGAN), Set.of(DayOfWeek.MONDAY),
                45, Set.of(PrivacySetting.SHARE_STREAK), false);

        observer.onUserLoggedIn(new UserLoggedInEvent(loginOutputData));

        final ProfileState state = profileViewModel.getState();
        assertEquals("amir", state.getUsername());
        assertEquals("1.8", state.getHeightText());
        assertEquals("80.0", state.getWeightText());
        assertEquals(ActivityLevelOption.VERY_ACTIVE, state.getActivityLevel());
        assertEquals(FitnessGoalOption.MUSCLE_AND_STRENGTH_GAIN, state.getGoal());
        assertEquals("/tmp/pic.png", state.getProfilePicturePath());
        assertEquals(LocalDate.of(2000, 1, 1), state.getDateOfBirth());
        assertEquals(GenderOption.MALE, state.getGender());
        assertEquals("Hello world", state.getBio());
        assertEquals(UnitSystemOption.METRIC, state.getPreferredUnitSystem());
        assertTrue(state.getEquipment().contains(EquipmentOption.BARBELL));
        assertTrue(state.getDietaryRestrictions().contains(DietaryRestrictionOption.VEGAN));
        assertEquals(Set.of(DayOfWeek.MONDAY), state.getPreferredWorkoutDays());
        assertEquals(45, state.getPreferredWorkoutDurationMinutes());
        assertTrue(state.getPrivacySettings().contains(PrivacySettingOption.SHARE_STREAK));
        assertNull(state.getProfileError());
        assertNull(state.getSaveConfirmation());
    }
}
