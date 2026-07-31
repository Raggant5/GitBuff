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

/**
 * The View for editing the current user's profile with matching modern styling and horizontal grids.
 */
public class ProfileView extends JPanel implements PropertyChangeListener {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color BG_DARK = new Color(245, 247, 250);
    private static final Color CARD_BORDER = new Color(220, 224, 230);

    private static final int PICTURE_PREVIEW_SIZE = 96;
    private static final float CM_PER_METRE = 100f;
    private static final float LBS_PER_KG = 2.20462f;
    private static final float INCHES_PER_METRE = 39.3701f;

    private static final int DUR_120_VAL = 120;
    private static final int DUR_105_VAL = 105;
    private static final int DUR_90_VAL = 90;
    private static final int DUR_75_VAL = 75;
    private static final int DUR_60_VAL = 60;
    private static final int DUR_45_VAL = 45;
    private static final int DUR_30_VAL = 30;
    private static final int DUR_15_VAL = 15;

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

    private final JCheckBox dur15 = new JCheckBox("15mins");
    private final JCheckBox dur30 = new JCheckBox("30mins");
    private final JCheckBox dur45 = new JCheckBox("45mins");
    private final JCheckBox dur60 = new JCheckBox("1h");
    private final JCheckBox dur75 = new JCheckBox("1h 15mins");
    private final JCheckBox dur90 = new JCheckBox("1h 30mins");
    private final JCheckBox dur105 = new JCheckBox("1h 45mins");
    private final JCheckBox dur120 = new JCheckBox("2h");
    private final JCheckBox durOther = new JCheckBox("Other (in minutes):");
    private final JTextField customDurationField = new JTextField(6);

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

        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        final JLabel title = new JLabel("User Profile Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        this.usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        this.usernameLabel.setForeground(new Color(236, 240, 241));

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(this.usernameLabel);

        final JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(BG_DARK);
        formContainer.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        this.choosePictureButton.addActionListener(this::onChoosePicture);
        this.saveButton.addActionListener(this::onSave);
        this.unitSystemBox.addActionListener(e -> updateUnitLabels());

        styleButton(this.choosePictureButton, PRIMARY_COLOR);
        styleButton(this.saveButton, ACCENT_COLOR);
        this.saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        this.saveButton.setPreferredSize(new Dimension(800, 42));

        final JPanel picCard = createCardPanel("Profile Picture");
        picCard.add(this.pictureLabel);
        picCard.add(Box.createRigidArea(new Dimension(0, 5)));
        picCard.add(this.choosePictureButton);
        formContainer.add(picCard);
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        final JPanel bioCard = createCardPanel("Bio");
        this.bioArea.setLineWrap(true);
        this.bioArea.setWrapStyleWord(true);
        bioCard.add(new JScrollPane(this.bioArea));
        formContainer.add(bioCard);
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        formContainer.add(createMetricsCard());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        formContainer.add(createStrategyCard());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        formContainer.add(createEquipmentPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        formContainer.add(createCheckBoxGridPanel("Dietary Restrictions",
                DietaryRestriction.values(), this.dietaryCheckBoxes, 3));
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        formContainer.add(createCheckBoxGridPanel("Preferred Workout Days",
                DayOfWeek.values(), this.dayCheckBoxes, 3));
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        formContainer.add(createDurationPresetPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        formContainer.add(createPrivacyPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        this.statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        this.statusLabel.setForeground(PRIMARY_COLOR);
        formContainer.add(this.statusLabel);

        final JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(this.saveButton, BorderLayout.SOUTH);

        displayState(profileViewModel.getState());
    }

    private JPanel createCardPanel(final String title) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1), title);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        border.setTitleColor(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(8, 12, 8, 12)));
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

        for (final Equipment e : Equipment.values()) {
            if (e.name().equalsIgnoreCase("STATIONARY_BIKE")
                    || e.toString().equalsIgnoreCase("Stationary Bike")) {
                continue;
            }
            final JCheckBox cb = new JCheckBox(e.toString());
            cb.setBackground(Color.WHITE);
            this.equipmentCheckBoxes.put(e, cb);
            grid.add(cb);
        }
        this.eqCombinedBikeCheckBox.setBackground(Color.WHITE);
        grid.add(this.eqCombinedBikeCheckBox);

        card.add(grid);
        return card;
    }

    private <E extends Enum<E>> JPanel createCheckBoxGridPanel(final String title, final E[] values,
                                                               final Map<E, JCheckBox> checkBoxMap,
                                                               final int columns) {
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

    private JPanel createDurationPresetPanel() {
        final JPanel card = createCardPanel("Preferred Workout Duration");
        final JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        flow.setBackground(Color.WHITE);

        final JCheckBox[] boxes = {this.dur15, this.dur30, this.dur45, this.dur60,
                this.dur75, this.dur90, this.dur105, this.dur120, this.durOther};
        for (final JCheckBox cb : boxes) {
            cb.setBackground(Color.WHITE);
            flow.add(cb);
        }
        flow.add(this.customDurationField);
        card.add(flow);
        return card;
    }

    private JPanel createPrivacyPanel() {
        final JPanel card = createCardPanel("Privacy Settings");
        for (final PrivacySetting setting : PrivacySetting.values()) {
            String labelText = setting.toString();
            if (setting.name().equalsIgnoreCase("SHARE_WORKOUT_ACTIVITY")
                    || labelText.toLowerCase().contains("workout activity")) {
                labelText = "Share workout activity (includes completed workouts, calories burned, progress stats, graphs)";
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

    private void styleButton(final JButton btn, final Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    private void onChoosePicture(final ActionEvent evt) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif"));
        final int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();
            this.selectedProfilePicturePath = selectedFile.getAbsolutePath();
            setPicturePreview(this.selectedProfilePicturePath);
        }
    }

    private int calculateSelectedDurationMinutes() {
        if (this.durOther.isSelected() && !this.customDurationField.getText().trim().isEmpty()) {
            return Integer.parseInt(this.customDurationField.getText().trim());
        }
        if (this.dur120.isSelected()) { return DUR_120_VAL; }
        if (this.dur105.isSelected()) { return DUR_105_VAL; }
        if (this.dur90.isSelected()) { return DUR_90_VAL; }
        if (this.dur75.isSelected()) { return DUR_75_VAL; }
        if (this.dur60.isSelected()) { return DUR_60_VAL; }
        if (this.dur45.isSelected()) { return DUR_45_VAL; }
        if (this.dur30.isSelected()) { return DUR_30_VAL; }
        if (this.dur15.isSelected()) { return DUR_15_VAL; }
        return DUR_45_VAL;
    }

    private void onSave(final ActionEvent evt) {
        if (this.profileController == null) {
            return;
        }
        try {
            final UnitSystem selectedUnit = (UnitSystem) this.unitSystemBox.getSelectedItem();
            final float rawHeight = Float.parseFloat(this.heightField.getText().trim());
            final float rawWeight = Float.parseFloat(this.weightField.getText().trim());

            final float heightMetres = (selectedUnit == UnitSystem.IMPERIAL)
                    ? (rawHeight / INCHES_PER_METRE) : (rawHeight / CM_PER_METRE);
            final float weightKg = (selectedUnit == UnitSystem.IMPERIAL)
                    ? (rawWeight / LBS_PER_KG) : rawWeight;

            LocalDate dob = null;
            if (!this.dobField.getText().trim().isEmpty()) {
                dob = LocalDate.parse(this.dobField.getText().trim());
            }

            final int durationMinutes = calculateSelectedDurationMinutes();

            final Set<Equipment> selectedEquipment = getSelectedItems(this.equipmentCheckBoxes);
            if (this.eqCombinedBikeCheckBox.isSelected()) {
                for (final Equipment e : Equipment.values()) {
                    if (e.name().equalsIgnoreCase("STATIONARY_BIKE")
                            || e.toString().equalsIgnoreCase("Stationary Bike")) {
                        selectedEquipment.add(e);
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
        catch (final NumberFormatException ex) {
            this.statusLabel.setText("Height, weight, and duration must be valid numbers.");
        }
        catch (final DateTimeParseException ex) {
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
        if (path == null) {
            this.pictureLabel.setText("No picture selected");
            this.pictureLabel.setIcon(null);
            return;
        }
        final ImageIcon icon = new ImageIcon(path);
        final Image scaled = icon.getImage().getScaledInstance(
                PICTURE_PREVIEW_SIZE, PICTURE_PREVIEW_SIZE, Image.SCALE_SMOOTH);
        this.pictureLabel.setIcon(new ImageIcon(scaled));
        this.pictureLabel.setText(null);
    }

    private void displayState(final ProfileState state) {
        this.usernameLabel.setText("Username: " + state.getUsername());
        this.unitSystemBox.setSelectedItem(state.getPreferredUnitSystem());
        updateUnitLabels();

        try {
            final float heightM = Float.parseFloat(state.getHeightText());
            final float weightKg = Float.parseFloat(state.getWeightText());
            if (state.getPreferredUnitSystem() == UnitSystem.IMPERIAL) {
                this.heightField.setText(String.valueOf(Math.round(heightM * INCHES_PER_METRE)));
                this.weightField.setText(String.valueOf(Math.round(weightKg * LBS_PER_KG)));
            }
            else {
                this.heightField.setText(String.valueOf(Math.round(heightM * CM_PER_METRE)));
                this.weightField.setText(state.getWeightText());
            }
        }
        catch (final NumberFormatException ex) {
            this.heightField.setText("");
            this.weightField.setText("");
        }

        this.dobField.setText(state.getDateOfBirth() != null ? state.getDateOfBirth().toString() : "");
        this.genderBox.setSelectedItem(state.getGender());
        this.bioArea.setText(state.getBio());
        this.activityLevelBox.setSelectedItem(state.getActivityLevel());
        this.goalBox.setSelectedItem(state.getGoal());

        setSelectedItems(this.equipmentCheckBoxes, state.getEquipment());
        setSelectedItems(this.dietaryCheckBoxes, state.getDietaryRestrictions());
        setSelectedItems(this.dayCheckBoxes, state.getPreferredWorkoutDays());
        setSelectedItems(this.privacyCheckBoxes, state.getPrivacySettings());

        if (state.getEquipment() != null) {
            boolean hasBike = false;
            for (final Equipment e : state.getEquipment()) {
                if (e.name().contains("BIKE") || e.toString().toLowerCase().contains("bike")) {
                    hasBike = true;
                    break;
                }
            }
            this.eqCombinedBikeCheckBox.setSelected(hasBike);
        }

        this.selectedProfilePicturePath = state.getProfilePicturePath();
        setPicturePreview(this.selectedProfilePicturePath);

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
    public void propertyChange(final PropertyChangeEvent evt) {
        displayState((ProfileState) evt.getNewValue());
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
