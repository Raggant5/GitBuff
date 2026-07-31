package view;

import entity.*;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
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

    // Duration Multi-Select Checkboxes + Custom Text Field
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

    public ProfileView(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
        profileViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(BG_DARK);

        // Header Banner
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        final JLabel title = new JLabel("User Profile Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        usernameLabel.setForeground(new Color(236, 240, 241));

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(usernameLabel);

        final JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(BG_DARK);
        formContainer.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        choosePictureButton.addActionListener(this::onChoosePicture);
        saveButton.addActionListener(this::onSave);
        unitSystemBox.addActionListener(e -> updateUnitLabels());

        styleButton(choosePictureButton, PRIMARY_COLOR);
        styleButton(saveButton, ACCENT_COLOR);
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(800, 42));

        // 1. Profile Picture Card
        final JPanel picCard = createCardPanel("Profile Picture");
        picCard.add(pictureLabel);
        picCard.add(Box.createRigidArea(new Dimension(0, 5)));
        picCard.add(choosePictureButton);
        formContainer.add(picCard);
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 2. Separate Bio Card
        final JPanel bioCard = createCardPanel("Bio");
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioCard.add(new JScrollPane(bioArea));
        formContainer.add(bioCard);
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 3. Basic Metrics Card
        formContainer.add(createMetricsCard());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 4. Fitness Strategy Card
        formContainer.add(createStrategyCard());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 5. Equipment Panel
        formContainer.add(createEquipmentPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 6. Dietary Restrictions
        formContainer.add(createCheckBoxGridPanel("Dietary Restrictions", DietaryRestriction.values(), dietaryCheckBoxes, 3));
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 7. Preferred Workout Days
        formContainer.add(createCheckBoxGridPanel("Preferred Workout Days", DayOfWeek.values(), dayCheckBoxes, 3));
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 8. Duration Panel
        formContainer.add(createDurationPresetPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // 9. Privacy Settings Panel
        formContainer.add(createPrivacyPanel());
        formContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        statusLabel.setForeground(PRIMARY_COLOR);
        formContainer.add(statusLabel);

        final JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        displayState(profileViewModel.getState());
    }

    private JPanel createCardPanel(String title) {
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
        grid.add(unitSystemBox);
        grid.add(heightLabel);
        grid.add(heightField);

        grid.add(weightLabel);
        grid.add(weightField);
        grid.add(new JLabel("DOB (YYYY-MM-DD):"));
        grid.add(dobField);

        card.add(grid);
        return card;
    }

    private JPanel createStrategyCard() {
        final JPanel card = createCardPanel("Fitness Strategy");
        final JPanel grid = new JPanel(new GridLayout(3, 2, 10, 8));
        grid.setBackground(Color.WHITE);

        grid.add(new JLabel("Gender:"));
        grid.add(genderBox);

        grid.add(new JLabel("Activity Level:"));
        grid.add(activityLevelBox);

        grid.add(new JLabel("Fitness Goal:"));
        grid.add(goalBox);

        card.add(grid);
        return card;
    }

    private JPanel createEquipmentPanel() {
        final JPanel card = createCardPanel("Available Equipment");
        final JPanel grid = new JPanel(new GridLayout(0, 3, 8, 4));
        grid.setBackground(Color.WHITE);

        for (Equipment e : Equipment.values()) {
            // Filter out individual stationary bike to replace with combined option
            if (e.name().equalsIgnoreCase("STATIONARY_BIKE") || e.toString().equalsIgnoreCase("Stationary Bike")) {
                continue;
            }
            final JCheckBox cb = new JCheckBox(e.toString());
            cb.setBackground(Color.WHITE);
            equipmentCheckBoxes.put(e, cb);
            grid.add(cb);
        }
        eqCombinedBikeCheckBox.setBackground(Color.WHITE);
        grid.add(eqCombinedBikeCheckBox);

        card.add(grid);
        return card;
    }

    private <E extends Enum<E>> JPanel createCheckBoxGridPanel(String title, E[] values, Map<E, JCheckBox> checkBoxMap, int columns) {
        final JPanel card = createCardPanel(title);
        final JPanel grid = new JPanel(new GridLayout(0, columns, 8, 4));
        grid.setBackground(Color.WHITE);

        for (E value : values) {
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

        final JCheckBox[] boxes = {dur15, dur30, dur45, dur60, dur75, dur90, dur105, dur120, durOther};
        for (JCheckBox cb : boxes) {
            cb.setBackground(Color.WHITE);
            flow.add(cb);
        }
        flow.add(customDurationField);
        card.add(flow);
        return card;
    }

    private JPanel createPrivacyPanel() {
        final JPanel card = createCardPanel("Privacy Settings");
        for (PrivacySetting setting : PrivacySetting.values()) {
            String labelText = setting.toString();
            if (setting.name().equalsIgnoreCase("SHARE_WORKOUT_ACTIVITY") ||
                    labelText.toLowerCase().contains("workout activity")) {
                labelText = "Share workout activity (includes completed workouts, calories burned, progress stats, graphs)";
            } else if (labelText.toLowerCase().contains("meal logs")) {
                continue;
            }
            final JCheckBox checkBox = new JCheckBox(labelText);
            checkBox.setBackground(Color.WHITE);
            privacyCheckBoxes.put(setting, checkBox);
            card.add(checkBox);
        }
        return card;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void updateUnitLabels() {
        final UnitSystem selectedUnit = (UnitSystem) unitSystemBox.getSelectedItem();
        if (selectedUnit == UnitSystem.IMPERIAL) {
            heightLabel.setText("Height (in)");
            weightLabel.setText("Weight (lbs)");
        } else {
            heightLabel.setText("Height (cm)");
            weightLabel.setText("Weight (kg)");
        }
    }

    private void onChoosePicture(ActionEvent evt) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif"));
        final int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();
            selectedProfilePicturePath = selectedFile.getAbsolutePath();
            setPicturePreview(selectedProfilePicturePath);
        }
    }

    private int calculateSelectedDurationMinutes() {
        if (durOther.isSelected() && !customDurationField.getText().trim().isEmpty()) {
            return Integer.parseInt(customDurationField.getText().trim());
        }
        if (dur120.isSelected()) return 120;
        if (dur105.isSelected()) return 105;
        if (dur90.isSelected()) return 90;
        if (dur75.isSelected()) return 75;
        if (dur60.isSelected()) return 60;
        if (dur45.isSelected()) return 45;
        if (dur30.isSelected()) return 30;
        if (dur15.isSelected()) return 15;
        return 45;
    }

    private void onSave(ActionEvent evt) {
        if (profileController == null) {
            return;
        }
        try {
            final UnitSystem selectedUnit = (UnitSystem) unitSystemBox.getSelectedItem();
            float rawHeight = Float.parseFloat(heightField.getText().trim());
            float rawWeight = Float.parseFloat(weightField.getText().trim());

            float heightMetres = (selectedUnit == UnitSystem.IMPERIAL) ? (rawHeight / INCHES_PER_METRE) : (rawHeight / CM_PER_METRE);
            float weightKg = (selectedUnit == UnitSystem.IMPERIAL) ? (rawWeight / LBS_PER_KG) : rawWeight;

            LocalDate dob = null;
            if (!dobField.getText().trim().isEmpty()) {
                dob = LocalDate.parse(dobField.getText().trim());
            }

            final int durationMinutes = calculateSelectedDurationMinutes();

            // Map combined bike checkbox to Equipment enum state if selected
            final Set<Equipment> selectedEquipment = getSelectedItems(equipmentCheckBoxes);
            if (eqCombinedBikeCheckBox.isSelected()) {
                for (Equipment e : Equipment.values()) {
                    if (e.name().equalsIgnoreCase("STATIONARY_BIKE") || e.toString().equalsIgnoreCase("Stationary Bike")) {
                        selectedEquipment.add(e);
                    }
                }
            }

            profileController.execute(
                    heightMetres,
                    weightKg,
                    (ActivityLevel) activityLevelBox.getSelectedItem(),
                    (FitnessGoal) goalBox.getSelectedItem(),
                    selectedProfilePicturePath,
                    dob,
                    (Gender) genderBox.getSelectedItem(),
                    bioArea.getText().trim(),
                    selectedUnit,
                    selectedEquipment,
                    getSelectedItems(dietaryCheckBoxes),
                    getSelectedItems(dayCheckBoxes),
                    durationMinutes,
                    getSelectedItems(privacyCheckBoxes)
            );
        } catch (NumberFormatException ex) {
            statusLabel.setText("Height, weight, and duration must be valid numbers.");
        } catch (DateTimeParseException ex) {
            statusLabel.setText("Date of birth must be in YYYY-MM-DD format.");
        }
    }

    private <E extends Enum<E>> Set<E> getSelectedItems(Map<E, JCheckBox> map) {
        final Set<E> selected = new HashSet<>();
        for (Map.Entry<E, JCheckBox> entry : map.entrySet()) {
            if (entry.getValue().isSelected()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }

    private <E extends Enum<E>> void setSelectedItems(Map<E, JCheckBox> map, Set<E> selected) {
        for (Map.Entry<E, JCheckBox> entry : map.entrySet()) {
            entry.getValue().setSelected(selected != null && selected.contains(entry.getKey()));
        }
    }

    private void setPicturePreview(String path) {
        if (path == null) {
            pictureLabel.setText("No picture selected");
            pictureLabel.setIcon(null);
            return;
        }
        final ImageIcon icon = new ImageIcon(path);
        final Image scaled = icon.getImage().getScaledInstance(PICTURE_PREVIEW_SIZE, PICTURE_PREVIEW_SIZE, Image.SCALE_SMOOTH);
        pictureLabel.setIcon(new ImageIcon(scaled));
        pictureLabel.setText(null);
    }

    private void displayState(ProfileState state) {
        usernameLabel.setText("Username: " + state.getUsername());
        unitSystemBox.setSelectedItem(state.getPreferredUnitSystem());
        updateUnitLabels();

        try {
            float heightM = Float.parseFloat(state.getHeightText());
            float weightKg = Float.parseFloat(state.getWeightText());
            if (state.getPreferredUnitSystem() == UnitSystem.IMPERIAL) {
                heightField.setText(String.valueOf(Math.round(heightM * INCHES_PER_METRE)));
                weightField.setText(String.valueOf(Math.round(weightKg * LBS_PER_KG)));
            } else {
                heightField.setText(String.valueOf(Math.round(heightM * CM_PER_METRE)));
                weightField.setText(state.getWeightText());
            }
        } catch (NumberFormatException ex) {
            heightField.setText("");
            weightField.setText("");
        }

        dobField.setText(state.getDateOfBirth() != null ? state.getDateOfBirth().toString() : "");
        genderBox.setSelectedItem(state.getGender());
        bioArea.setText(state.getBio());
        activityLevelBox.setSelectedItem(state.getActivityLevel());
        goalBox.setSelectedItem(state.getGoal());

        setSelectedItems(equipmentCheckBoxes, state.getEquipment());
        setSelectedItems(dietaryCheckBoxes, state.getDietaryRestrictions());
        setSelectedItems(dayCheckBoxes, state.getPreferredWorkoutDays());
        setSelectedItems(privacyCheckBoxes, state.getPrivacySettings());

        // Sync combined bike checkbox state
        if (state.getEquipment() != null) {
            boolean hasBike = false;
            for (Equipment e : state.getEquipment()) {
                if (e.name().contains("BIKE") || e.toString().toLowerCase().contains("bike")) {
                    hasBike = true;
                    break;
                }
            }
            eqCombinedBikeCheckBox.setSelected(hasBike);
        }

        selectedProfilePicturePath = state.getProfilePicturePath();
        setPicturePreview(selectedProfilePicturePath);

        if (state.getProfileError() != null) {
            statusLabel.setText(state.getProfileError());
        } else if (state.getSaveConfirmation() != null) {
            statusLabel.setText(state.getSaveConfirmation());
        } else {
            statusLabel.setText("");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        displayState((ProfileState) evt.getNewValue());
    }

    public String getViewName() {
        return viewName;
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void setUsername(String username) {
        final ProfileState state = profileViewModel.getState();
        state.setUsername(username);
        displayState(state);
    }
}
