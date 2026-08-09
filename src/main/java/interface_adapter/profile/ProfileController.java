package interface_adapter.profile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import javax.swing.SwingWorker;

import interface_adapter.recommendation.RecommendationController;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInputData;

/**
 * The controller for the Edit Profile Use Case.
 *
 * <p>Accepts the interface_adapter-layer {@code *Option} enums from {@code view.ProfileView}
 * and translates them to the entity-layer enums, via {@link ProfileEnumMapper}, only when
 * building {@link EditProfileInputData} - the DTO the use case layer expects. This is the only
 * place in this class that touches the entity layer, and it does so through the mapper rather
 * than importing {@code entity.*} directly.
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
     * Executes the Edit Profile Use Case using view-facing profile parameters.
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
    public void execute(final float height, final float weight, final ActivityLevelOption activityLevel,
                        final FitnessGoalOption goal, final String profilePicturePath,
                        final LocalDate dateOfBirth, final GenderOption gender, final String bio,
                        final UnitSystemOption preferredUnitSystem, final Set<EquipmentOption> equipment,
                        final Set<DietaryRestrictionOption> dietaryRestrictions,
                        final Set<DayOfWeek> preferredWorkoutDays,
                        final int preferredWorkoutDurationMinutes,
                        final Set<PrivacySettingOption> privacySettings) {
        if (this.workoutsViewModel != null) {
            final WorkoutsState state = this.workoutsViewModel.getState();
            state.setLoading(true);
            this.workoutsViewModel.firePropertyChanged();
        }

        final EditProfileInputData inputData = new EditProfileInputData.Builder()
                .height(height)
                .weight(weight)
                .activityLevel(ProfileEnumMapper.toEntity(activityLevel))
                .goal(ProfileEnumMapper.toEntity(goal))
                .profilePicturePath(profilePicturePath)
                .dateOfBirth(dateOfBirth)
                .gender(ProfileEnumMapper.toEntity(gender))
                .bio(bio)
                .preferredUnitSystem(ProfileEnumMapper.toEntity(preferredUnitSystem))
                .equipment(ProfileEnumMapper.toEquipmentEntities(equipment))
                .dietaryRestrictions(ProfileEnumMapper.toDietaryEntities(dietaryRestrictions))
                .preferredWorkoutDays(preferredWorkoutDays)
                .preferredWorkoutDurationMinutes(preferredWorkoutDurationMinutes)
                .privacySettings(ProfileEnumMapper.toPrivacyEntities(privacySettings))
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

