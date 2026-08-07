package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.ActivityLevel;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;

/**
 * The View for editing the current user's profile with matching modern styling and horizontal grids.
 */
public class ProfileView extends JPanel implements PropertyChangeListener {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color BG_DARK = new Color(245, 247, 250);
    private static final Color CARD_BORDER = new Color(220, 224, 230);
    private static final Color LIGHT_TEXT_COLOR = new Color(236, 240, 241);

    private static final int PICTURE_PREVIEW_SIZE = 96;
    private static final int DEFAULT_WORKOUT_DURATION = 45;

    private static final float CM_PER_METRE = 100f;
    private static final float LBS_PER_KG = 2.20462f;
    private static final float INCHES_PER_METRE = 39.3701f;

    private static final String FONT_SANS_SERIF = "SansSerif";
    private static final int SECTION_PADDING_VERTICAL = 15;
    private static final int SECTION_PADDING_HORIZONTAL = 20;
    private static final int TITLE_FONT_SIZE = 20;
    private static final int SMALL_FONT_SIZE = 13;
    private static final int SAVE_BUTTON_FONT_SIZE = 14;
    private static final int SAVE_BUTTON_WIDTH = 800;
    private static final int SAVE_BUTTON_HEIGHT = 42;
    private static final int SMALL_GAP = 5;
    private static final int CARD_GAP = 12;
    private static final int CHECKBOX_GRID_COLUMNS = 3;
    private static final int SCROLL_UNIT_INCREMENT = 16;
    private static final int CARD_PADDING_VERTICAL = 8;
    private static final int CARD_PADDING_HORIZONTAL = 12;

    private final String viewName = "profile";
    private final ProfileViewModel profileViewModel;

    private final JLabel usernameLabel = new JLabel();

    private final JTextField heightField = new JTextField(8);
    private final JLabel heightLabel = new JLabel("Height (cm)");

    private final JTextField weightField = new JTextField(8);
    private final JLabel weightLabel = new JLabel("Weight (kg)");

    private final JComboBox<ActivityLevel> activityLevelBox = new JComboBox<>(ActivityLevel.values());
    private final JComboBox<FitnessGoal> goalBox = new JComboBox<>(FitnessGoal.values());
    private final JComboBox<Gender> genderBox = new JComboBox<>(Gender.values());
    private final JComboBox<UnitSystem> unitSystemBox = new JComboBox<>(UnitSystem.values());

    private final JTextField dobField = new JTextField(10);
    private final JTextArea bioArea = new JTextArea(3, 20);

    private final Map<Equipment, JCheckBox> equipmentCheckBoxes = new HashMap<>();
    private final JCheckBox eqCombinedBikeCheckBox = new JCheckBox("Bike (stationary or outdoor)");

    private final Map<DietaryRestriction, JCheckBox> dietaryCheckBoxes = new HashMap<>();
    private final Map<DayOfWeek, JCheckBox> dayCheckBoxes = new HashMap<>();
    private final Map<PrivacySetting, JCheckBox> privacyCheckBoxes = new HashMap<>();

    private final JTextField preferredWorkoutDurationField = new JTextField(8);
    private final JLabel pictureLabel = new JLabel("No picture selected");

    private final JButton choosePictureButton = new JButton("Choose Profile Picture");
    private final JButton saveButton = new JButton("Save Profile");

    private final JLabel statusLabel = new JLabel();

    private String selectedProfilePicturePath;
    private ProfileController profileController;

    /**
     * Constructs a ProfileView instance.
     *
     * @param profileViewModel view model managing profile state
     */
    public ProfileView(final ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
        this.profileViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());
        this.setBackground(BG_DARK);

        final JPanel topPanel = createTopPanel();
        wireControlListeners();
        final JPanel formContainer = buildFormContainer();

        final JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(this.saveButton, BorderLayout.SOUTH);

        displayState(this.profileViewModel.getState());
    }

    private JPanel createTopPanel() {
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(
                SECTION_PADDING_VERTICAL,
                SECTION_PADDING_HORIZONTAL,
                SECTION_PADDING_VERTICAL,
                SECTION_PADDING_HORIZONTAL
        ));

        final JLabel title = new JLabel("User Profile Settings");
        title.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, TITLE_FONT_SIZE));
        title.setForeground(Color.WHITE);

        this.usernameLabel.setFont(new Font(FONT_SANS_SERIF, Font.PLAIN, SMALL_FONT_SIZE));
        this.usernameLabel.setForeground(LIGHT_TEXT_COLOR);

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, SMALL_GAP)));
        topPanel.add(this.usernameLabel);

        return topPanel;
    }

    private void wireControlListeners() {
        this.choosePictureButton.addActionListener(this::onChoosePicture);
        this.saveButton.addActionListener(this::onSave);
        this.unitSystemBox.addActionListener(event -> updateUnitLabels());

        styleButton(this.choosePictureButton, PRIMARY_COLOR);
        styleButton(this.saveButton, ACCENT_COLOR);

        this.saveButton.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, SAVE_BUTTON_FONT_SIZE));
        this.saveButton.setPreferredSize(new Dimension(SAVE_BUTTON_WIDTH, SAVE_BUTTON_HEIGHT));
    }

    private JPanel buildFormContainer() {
        final JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(BG_DARK);
        formContainer.setBorder(BorderFactory.createEmptyBorder(
                SECTION_PADDING_VERTICAL,
                SECTION_PADDING_HORIZONTAL,
                SECTION_PADDING_VERTICAL,
                SECTION_PADDING_HORIZONTAL
        ));

        final JPanel picCard = createCardPanel("Profile Picture");
        picCard.add(this.pictureLabel);
        picCard.add(Box.createRigidArea(new Dimension(0, SMALL_GAP)));
        picCard.add(this.choosePictureButton);

        formContainer.add(picCard);
        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        final JPanel bioCard = createCardPanel("Bio");
        this.bioArea.setLineWrap(true);
        this.bioArea.setWrapStyleWord(true);
        bioCard.add(new JScrollPane(this.bioArea));

        formContainer.add(bioCard);
        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        formContainer.add(createMetricsCard());
        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        formContainer.add(createStrategyCard());
        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        formContainer.add(createEquipmentPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        addPreferencesSections(formContainer);

        return formContainer;
    }

    private void addPreferencesSections(final JPanel formContainer) {
        formContainer.add(createCheckBoxGridPanel(
                "Dietary Restrictions",
                DietaryRestriction.values(),
                this.dietaryCheckBoxes,
                CHECKBOX_GRID_COLUMNS
        ));

        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        formContainer.add(createCheckBoxGridPanel(
                "Preferred Workout Days",
                DayOfWeek.values(),
                this.dayCheckBoxes,
                CHECKBOX_GRID_COLUMNS
        ));

        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        formContainer.add(createDurationPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        formContainer.add(createPrivacyPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, CARD_GAP)));

        this.statusLabel.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, SMALL_FONT_SIZE));
        this.statusLabel.setForeground(PRIMARY_COLOR);

        formContainer.add(this.statusLabel);
    }

    /**
     * Binds to workouts view model to reflect schedule loading state on profile screen.
     *
     * @param workoutsViewModel view model for workout schedule state
     */
    public void setWorkoutsViewModel(final WorkoutsViewModel workoutsViewModel) {
        if (workoutsViewModel != null) {
            workoutsViewModel.addPropertyChangeListener(evt -> {
                if (evt.getNewValue() instanceof WorkoutsState) {
                    final WorkoutsState state = (WorkoutsState) evt.getNewValue();
                    if (state.isLoading()) {
                        this.statusLabel.setText("Loading workout schedule...");
                    }
                    else if ("Loading workout schedule...".equals(this.statusLabel.getText())) {
                        this.statusLabel.setText("Profile saved.");
                    }
                }
            });
        }
    }

    private JPanel createCardPanel(final String title) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                title
        );

        border.setTitleFont(new Font(FONT_SANS_SERIF, Font.BOLD, SMALL_FONT_SIZE));
        border.setTitleColor(PRIMARY_COLOR);

        panel.setBorder(BorderFactory.createCompoundBorder(
                border,
                BorderFactory.createEmptyBorder(
                        CARD_PADDING_VERTICAL,
                        CARD_PADDING_HORIZONTAL,
                        CARD_PADDING_VERTICAL,
                        CARD_PADDING_HORIZONTAL
                )
        ));

        return panel;
    }

    private JPanel createMetricsCard() {
        final JPanel card = createCardPanel("Basic Metrics");
        final JPanel grid = new JPanel(new GridLayout(2, 4, 10, 8));
        grid.setBackground(Color.WHITE);

        grid.add(new JLabel("Unit System:"));
        grid.add(this.unitSystemBox);
        grid.add(this.heightLabel);
        grid.add(this.heightField);

        grid.add(this.weightLabel);
        grid.add(this.weightField);
        grid.add(new JLabel("DOB (YYYY-MM-DD):"));
        grid.add(this.dobField);

        card.add(grid);
        return card;
    }

    private JPanel createStrategyCard() {
        final JPanel card = createCardPanel("Fitness Strategy");
        final JPanel grid = new JPanel(new GridLayout(3, 2, 10, 8));
        grid.setBackground(Color.WHITE);

        grid.add(new JLabel("Gender:"));
        grid.add(this.genderBox);

        grid.add(new JLabel("Activity Level:"));
        grid.add(this.activityLevelBox);

        grid.add(new JLabel("Fitness Goal:"));
        grid.add(this.goalBox);

        card.add(grid);
        return card;
    }

    private JPanel createEquipmentPanel() {
        final JPanel card = createCardPanel("Available Equipment");
        final JPanel grid = new JPanel(new GridLayout(0, 3, 8, 4));
        grid.setBackground(Color.WHITE);

        for (final Equipment equipment : Equipment.values()) {
            if (equipment.name().equalsIgnoreCase("STATIONARY_BIKE")
                    || equipment.toString().equalsIgnoreCase("Stationary Bike")) {
                continue;
            }

            final JCheckBox checkBox = new JCheckBox(equipment.toString());
            checkBox.setBackground(Color.WHITE);
            this.equipmentCheckBoxes.put(equipment, checkBox);
            grid.add(checkBox);
        }

        this.eqCombinedBikeCheckBox.setBackground(Color.WHITE);
        grid.add(this.eqCombinedBikeCheckBox);

        card.add(grid);
        return card;
    }

    private <E extends Enum<E>> JPanel createCheckBoxGridPanel(
            final String title,
            final E[] values,
            final Map<E, JCheckBox> checkBoxMap,
            final int columns
    ) {
        final JPanel card = createCardPanel(title);
        final JPanel grid = new JPanel(new GridLayout(0, columns, 8, 4));
        grid.setBackground(Color.WHITE);

        for (final E value : values) {
            final JCheckBox checkBox = new JCheckBox(value.toString());
            checkBox.setBackground(Color.WHITE);
            checkBoxMap.put(value, checkBox);
            grid.add(checkBox);
        }

        card.add(grid);
        return card;
    }

    private JPanel createDurationPanel() {
        final JPanel card = createCardPanel("Preferred Workout Duration");
        final JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        flow.setBackground(Color.WHITE);

        flow.add(new JLabel("Enter preferred workout time (minutes):"));
        flow.add(this.preferredWorkoutDurationField);

        card.add(flow);
        return card;
    }

    private JPanel createPrivacyPanel() {
        final JPanel card = createCardPanel("Privacy Settings");

        for (final PrivacySetting setting : PrivacySetting.values()) {
            String labelText = setting.toString();

            if (setting.name().equalsIgnoreCase("SHARE_WORKOUT_ACTIVITY")
                    || labelText.toLowerCase().contains("workout activity")) {
                labelText = "Share workout activity (includes completed workouts, calories burned, progress stats)";
            }
            else if (labelText.toLowerCase().contains("meal logs")) {
                continue;
            }

            final JCheckBox checkBox = new JCheckBox(labelText);
            checkBox.setBackground(Color.WHITE);
            this.privacyCheckBoxes.put(setting, checkBox);
            card.add(checkBox);
        }

        return card;
    }

    private void styleButton(final JButton button, final Color background) {
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void updateUnitLabels() {
        final UnitSystem selectedUnit = (UnitSystem) this.unitSystemBox.getSelectedItem();
        if (selectedUnit == UnitSystem.IMPERIAL) {
            this.heightLabel.setText("Height (in)");
            this.weightLabel.setText("Weight (lbs)");
        }
        else {
            this.heightLabel.setText("Height (cm)");
            this.weightLabel.setText("Weight (kg)");
        }
    }

    private void onChoosePicture(final ActionEvent event) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif"));

        final int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();
            this.selectedProfilePicturePath = selectedFile.getAbsolutePath();
            setPicturePreview(this.selectedProfilePicturePath);
        }
    }

    private int getPreferredWorkoutDurationMinutes() {
        final String durationText = this.preferredWorkoutDurationField.getText().trim();

        final int durationMinutes;
        if (durationText.isEmpty()) {
            durationMinutes = DEFAULT_WORKOUT_DURATION;
        }
        else {
            durationMinutes = Integer.parseInt(durationText);
            if (durationMinutes <= 0) {
                throw new NumberFormatException("Workout duration must be positive.");
            }
        }

        return durationMinutes;
    }

    private void onSave(final ActionEvent event) {
        if (this.profileController != null) {
            trySaveProfile();
        }
    }

    private void trySaveProfile() {
        try {
            final UnitSystem selectedUnit = (UnitSystem) this.unitSystemBox.getSelectedItem();
            final float rawHeight = Float.parseFloat(this.heightField.getText().trim());
            final float rawWeight = Float.parseFloat(this.weightField.getText().trim());

            final float heightMetres;
            if (selectedUnit == UnitSystem.IMPERIAL) {
                heightMetres = rawHeight / INCHES_PER_METRE;
            }
            else {
                heightMetres = rawHeight / CM_PER_METRE;
            }

            final float weightKg;
            if (selectedUnit == UnitSystem.IMPERIAL) {
                weightKg = rawWeight / LBS_PER_KG;
            }
            else {
                weightKg = rawWeight;
            }

            LocalDate dob = null;
            if (!this.dobField.getText().trim().isEmpty()) {
                dob = LocalDate.parse(this.dobField.getText().trim());
            }

            final int durationMinutes = getPreferredWorkoutDurationMinutes();
            final Set<Equipment> selectedEquipment = getSelectedItems(this.equipmentCheckBoxes);

            if (this.eqCombinedBikeCheckBox.isSelected()) {
                for (final Equipment equipment : Equipment.values()) {
                    if (equipment.name().equalsIgnoreCase("STATIONARY_BIKE")
                            || equipment.toString().equalsIgnoreCase("Stationary Bike")) {
                        selectedEquipment.add(equipment);
                    }
                }
            }

            this.profileController.execute(
                    heightMetres,
                    weightKg,
                    (ActivityLevel) this.activityLevelBox.getSelectedItem(),
                    (FitnessGoal) this.goalBox.getSelectedItem(),
                    this.selectedProfilePicturePath,
                    dob,
                    (Gender) this.genderBox.getSelectedItem(),
                    this.bioArea.getText().trim(),
                    selectedUnit,
                    selectedEquipment,
                    getSelectedItems(this.dietaryCheckBoxes),
                    getSelectedItems(this.dayCheckBoxes),
                    durationMinutes,
                    getSelectedItems(this.privacyCheckBoxes)
            );
        }
        catch (final NumberFormatException exception) {
            this.statusLabel.setText("Height, weight, and workout duration must be valid positive numbers.");
        }
        catch (final DateTimeParseException exception) {
            this.statusLabel.setText("Date of birth must be in YYYY-MM-DD format.");
        }
    }

    private <E extends Enum<E>> Set<E> getSelectedItems(final Map<E, JCheckBox> map) {
        final Set<E> selected = new HashSet<>();
        for (final Map.Entry<E, JCheckBox> entry : map.entrySet()) {
            if (entry.getValue().isSelected()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }

    private <E extends Enum<E>> void setSelectedItems(final Map<E, JCheckBox> map, final Set<E> selected) {
        for (final Map.Entry<E, JCheckBox> entry : map.entrySet()) {
            entry.getValue().setSelected(selected != null && selected.contains(entry.getKey()));
        }
    }

    private void setPicturePreview(final String path) {
        if (path == null || path.isBlank()) {
            this.pictureLabel.setText("No picture selected");
            this.pictureLabel.setIcon(null);
        }
        else {
            final ImageIcon icon = new ImageIcon(path);
            final Image scaled = icon.getImage().getScaledInstance(PICTURE_PREVIEW_SIZE,
                    PICTURE_PREVIEW_SIZE, Image.SCALE_SMOOTH);

            this.pictureLabel.setIcon(new ImageIcon(scaled));
            this.pictureLabel.setText(null);
        }
    }

    private void displayState(final ProfileState state) {
        this.usernameLabel.setText("Username: " + state.getUsername());
        this.unitSystemBox.setSelectedItem(state.getPreferredUnitSystem());

        updateUnitLabels();

        displayHeightAndWeight(state);
        displayDateOfBirth(state);

        this.genderBox.setSelectedItem(state.getGender());
        this.bioArea.setText(state.getBio());
        this.activityLevelBox.setSelectedItem(state.getActivityLevel());
        this.goalBox.setSelectedItem(state.getGoal());

        setSelectedItems(this.equipmentCheckBoxes, state.getEquipment());
        setSelectedItems(this.dietaryCheckBoxes, state.getDietaryRestrictions());
        setSelectedItems(this.dayCheckBoxes, state.getPreferredWorkoutDays());

        displayWorkoutDuration(state);

        setSelectedItems(this.privacyCheckBoxes, state.getPrivacySettings());

        displayCombinedBikeCheckbox(state);

        this.selectedProfilePicturePath = state.getProfilePicturePath();
        setPicturePreview(this.selectedProfilePicturePath);

        displayStatusMessage(state);
    }

    private void displayHeightAndWeight(final ProfileState state) {
        try {
            final float heightMetres = Float.parseFloat(state.getHeightText());
            final float weightKg = Float.parseFloat(state.getWeightText());

            if (state.getPreferredUnitSystem() == UnitSystem.IMPERIAL) {
                this.heightField.setText(String.valueOf(Math.round(heightMetres * INCHES_PER_METRE)));
                this.weightField.setText(String.valueOf(Math.round(weightKg * LBS_PER_KG)));
            }
            else {
                this.heightField.setText(String.valueOf(Math.round(heightMetres * CM_PER_METRE)));
                this.weightField.setText(state.getWeightText());
            }
        }
        catch (final NumberFormatException exception) {
            this.heightField.setText("");
            this.weightField.setText("");
        }
    }

    private void displayDateOfBirth(final ProfileState state) {
        if (state.getDateOfBirth() == null) {
            this.dobField.setText("");
        }
        else {
            this.dobField.setText(state.getDateOfBirth().toString());
        }
    }

    private void displayWorkoutDuration(final ProfileState state) {
        final int savedDuration = state.getPreferredWorkoutDurationMinutes();
        if (savedDuration > 0) {
            this.preferredWorkoutDurationField.setText(String.valueOf(savedDuration));
        }
        else {
            this.preferredWorkoutDurationField.setText(String.valueOf(DEFAULT_WORKOUT_DURATION));
        }
    }

    private void displayCombinedBikeCheckbox(final ProfileState state) {
        if (state.getEquipment() != null) {
            boolean hasBike = false;
            for (final Equipment equipment : state.getEquipment()) {
                if (equipment.name().contains("BIKE")
                        || equipment.toString().toLowerCase().contains("bike")) {
                    hasBike = true;
                    break;
                }
            }
            this.eqCombinedBikeCheckBox.setSelected(hasBike);
        }
    }

    private void displayStatusMessage(final ProfileState state) {
        if (state.getProfileError() != null) {
            this.statusLabel.setText(state.getProfileError());
        }
        else if (state.getSaveConfirmation() != null) {
            this.statusLabel.setText(state.getSaveConfirmation());
        }
        else {
            this.statusLabel.setText("");
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        displayState((ProfileState) event.getNewValue());
    }

    /**
     * Gets the view name.
     *
     * @return view name string
     */
    public String getViewName() {
        return this.viewName;
    }

    /**
     * Sets the profile controller.
     *
     * @param profileController controller instance
     */
    public void setProfileController(final ProfileController profileController) {
        this.profileController = profileController;
    }

    /**
     * Sets the active username in profile view state.
     *
     * @param username username string
     */
    public void setUsername(final String username) {
        final ProfileState state = this.profileViewModel.getState();
        state.setUsername(username);
        displayState(state);
    }
}
