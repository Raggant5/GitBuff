package view;

import entity.*;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;

import javax.swing.*;
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
 * The View for editing the current user's profile.
 */
public class ProfileView extends JPanel implements PropertyChangeListener {

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
    private final JTextField durationField = new JTextField(5);

    private final Map<Equipment, JCheckBox> equipmentCheckBoxes = new HashMap<>();
    private final Map<DietaryRestriction, JCheckBox> dietaryCheckBoxes = new HashMap<>();
    private final Map<DayOfWeek, JCheckBox> dayCheckBoxes = new HashMap<>();
    private final Map<PrivacySetting, JCheckBox> privacyCheckBoxes = new HashMap<>();

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

        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("My Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        choosePictureButton.addActionListener(this::onChoosePicture);
        saveButton.addActionListener(this::onSave);
        unitSystemBox.addActionListener(e -> updateUnitLabels());

        final Dimension comboBoxSize = new Dimension(300, 30);
        activityLevelBox.setMaximumSize(comboBoxSize);
        goalBox.setMaximumSize(comboBoxSize);
        genderBox.setMaximumSize(comboBoxSize);
        unitSystemBox.setMaximumSize(comboBoxSize);

        final Dimension rowSize = new Dimension(300, 40);
        final LabelTextPanel heightInfo = new LabelTextPanel(heightLabel, heightField);
        final LabelTextPanel weightInfo = new LabelTextPanel(weightLabel, weightField);
        final LabelTextPanel dobInfo = new LabelTextPanel(new JLabel("Date of Birth (YYYY-MM-DD)"), dobField);
        final LabelTextPanel durationInfo = new LabelTextPanel(new JLabel("Preferred Duration (mins)"), durationField);

        heightInfo.setMaximumSize(rowSize);
        weightInfo.setMaximumSize(rowSize);
        dobInfo.setMaximumSize(rowSize);
        durationInfo.setMaximumSize(rowSize);

        contentPanel.add(title);
        contentPanel.add(usernameLabel);
        contentPanel.add(new JLabel("Unit System"));
        contentPanel.add(unitSystemBox);
        contentPanel.add(heightInfo);
        contentPanel.add(weightInfo);
        contentPanel.add(dobInfo);
        contentPanel.add(new JLabel("Gender"));
        contentPanel.add(genderBox);
        contentPanel.add(new JLabel("Bio"));
        bioArea.setLineWrap(true);
        contentPanel.add(new JScrollPane(bioArea));
        contentPanel.add(new JLabel("Activity Level"));
        contentPanel.add(activityLevelBox);
        contentPanel.add(new JLabel("Fitness Goal"));
        contentPanel.add(goalBox);

        // Checkbox Panels
        contentPanel.add(createCheckBoxPanel("Available Equipment", Equipment.values(), equipmentCheckBoxes));
        contentPanel.add(createCheckBoxPanel("Dietary Restrictions", DietaryRestriction.values(), dietaryCheckBoxes));
        contentPanel.add(createCheckBoxPanel("Preferred Workout Days", DayOfWeek.values(), dayCheckBoxes));
        contentPanel.add(durationInfo);
        contentPanel.add(createCheckBoxPanel("Privacy Settings", PrivacySetting.values(), privacyCheckBoxes));

        contentPanel.add(pictureLabel);
        contentPanel.add(choosePictureButton);
        contentPanel.add(saveButton);
        contentPanel.add(statusLabel);

        final JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);

        displayState(profileViewModel.getState());
    }

    private <E extends Enum<E>> JPanel createCheckBoxPanel(String title, E[] values, Map<E, JCheckBox> checkBoxMap) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        for (E value : values) {
            final JCheckBox checkBox = new JCheckBox(value.toString());
            checkBoxMap.put(value, checkBox);
            panel.add(checkBox);
        }
        return panel;
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

            final int duration = Integer.parseInt(durationField.getText().trim());

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
                    getSelectedItems(equipmentCheckBoxes),
                    getSelectedItems(dietaryCheckBoxes),
                    getSelectedItems(dayCheckBoxes),
                    duration,
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
        durationField.setText(String.valueOf(state.getPreferredWorkoutDurationMinutes()));

        setSelectedItems(equipmentCheckBoxes, state.getEquipment());
        setSelectedItems(dietaryCheckBoxes, state.getDietaryRestrictions());
        setSelectedItems(dayCheckBoxes, state.getPreferredWorkoutDays());
        setSelectedItems(privacyCheckBoxes, state.getPrivacySettings());

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
