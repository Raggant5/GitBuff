package interface_adapter.profile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import javax.swing.SwingWorker;

import entity.ActivityLevel;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;
import interface_adapter.recommendation.RecommendationController;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInputData;

/**
 * The controller for the Edit Profile Use Case.
 */
public class ProfileController {

    private final EditProfileInputBoundary editProfileUseCaseInteractor;
    private RecommendationController recommendationController;
    private WorkoutsViewModel workoutsViewModel;

    /**
     * Constructs a ProfileController instance.
     *
     * @param editProfileUseCaseInteractor interactor boundary for profile modifications
     */
    public ProfileController(final EditProfileInputBoundary editProfileUseCaseInteractor) {
        this.editProfileUseCaseInteractor = editProfileUseCaseInteractor;
    }

    /**
     * Sets recommendation dependencies for triggering schedule updates.
     *
     * @param recommendationController controller to execute recommendations
     * @param workoutsViewModel view model for workout state
     */
    public void setRecommendationDependencies(final RecommendationController recommendationController,
                                              final WorkoutsViewModel workoutsViewModel) {
        this.recommendationController = recommendationController;
        this.workoutsViewModel = workoutsViewModel;
    }

    /**
     * Executes the Edit Profile Use Case using primitive profile parameters.
     *
     * @param height height in meters
     * @param weight weight in kg
     * @param activityLevel activity level selection
     * @param goal fitness goal selection
     * @param profilePicturePath profile image file path
     * @param dateOfBirth date of birth
     * @param gender gender selection
     * @param bio user bio description
     * @param preferredUnitSystem preferred measurement units
     * @param equipment set of available equipment
     * @param dietaryRestrictions set of dietary restrictions
     * @param preferredWorkoutDays set of preferred workout days
     * @param preferredWorkoutDurationMinutes target workout duration in minutes
     * @param privacySettings set of enabled privacy settings
     */
    public void execute(final float height, final float weight, final ActivityLevel activityLevel,
                        final FitnessGoal goal, final String profilePicturePath,
                        final LocalDate dateOfBirth, final Gender gender, final String bio,
                        final UnitSystem preferredUnitSystem, final Set<Equipment> equipment,
                        final Set<DietaryRestriction> dietaryRestrictions,
                        final Set<DayOfWeek> preferredWorkoutDays,
                        final int preferredWorkoutDurationMinutes,
                        final Set<PrivacySetting> privacySettings) {
        if (this.workoutsViewModel != null) {
            final WorkoutsState state = this.workoutsViewModel.getState();
            state.setLoading(true);
            this.workoutsViewModel.firePropertyChanged();
        }

        final EditProfileInputData inputData = new EditProfileInputData.Builder()
                .height(height)
                .weight(weight)
                .activityLevel(activityLevel)
                .goal(goal)
                .profilePicturePath(profilePicturePath)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .bio(bio)
                .preferredUnitSystem(preferredUnitSystem)
                .equipment(equipment)
                .dietaryRestrictions(dietaryRestrictions)
                .preferredWorkoutDays(preferredWorkoutDays)
                .preferredWorkoutDurationMinutes(preferredWorkoutDurationMinutes)
                .privacySettings(privacySettings)
                .build();

        final SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                editProfileUseCaseInteractor.execute(inputData);
                if (recommendationController != null) {
                    recommendationController.execute();
                }
                return null;
            }
        };
        worker.execute();
    }

    /**
     * Executes the Edit Profile Use Case using pre-constructed input data.
     *
     * @param inputData input data object
     */
    public void execute(final EditProfileInputData inputData) {
        this.editProfileUseCaseInteractor.execute(inputData);
    }
}
