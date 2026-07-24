package view;

import entity.ActivityLevel;
import entity.FitnessGoal;
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

/**
 * The View for editing the current user's profile: height, weight, activity level,
 * fitness goal, and a custom profile picture.
 */
public class ProfileView extends JPanel implements PropertyChangeListener {

    private static final int PICTURE_PREVIEW_SIZE = 96;
    private static final float CM_PER_METRE = 100f;

    private final String viewName = "profile";
    private final ProfileViewModel profileViewModel;

    private final JLabel usernameLabel = new JLabel();
    private final JTextField heightField = new JTextField(8);
    private final JTextField weightField = new JTextField(8);
    private final JComboBox<ActivityLevel> activityLevelBox = new JComboBox<>(ActivityLevel.values());
    private final JComboBox<FitnessGoal> goalBox = new JComboBox<>(FitnessGoal.values());
    private final JLabel pictureLabel = new JLabel("No picture selected");
    private final JButton choosePictureButton = new JButton("Choose Profile Picture");
    private final JButton saveButton = new JButton("Save Profile");
    private final JLabel statusLabel = new JLabel();

    private String selectedProfilePicturePath;
    private ProfileController profileController;

    public ProfileView(ProfileViewModel profileViewModel) {

        this.profileViewModel = profileViewModel;
        profileViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("My Profile");

        choosePictureButton.addActionListener(this::onChoosePicture);
        saveButton.addActionListener(this::onSave);

        final Dimension comboBoxSize = new Dimension(300, 30);
        activityLevelBox.setMaximumSize(comboBoxSize);
        activityLevelBox.setPreferredSize(comboBoxSize);
        activityLevelBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        goalBox.setMaximumSize(comboBoxSize);
        goalBox.setPreferredSize(comboBoxSize);
        goalBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        final Dimension rowSize = new Dimension(300, 40);
        final LabelTextPanel heightInfo = new LabelTextPanel(new JLabel("Height (cm)"), heightField);
        final LabelTextPanel weightInfo = new LabelTextPanel(new JLabel("Weight (kg)"), weightField);
        heightInfo.setMaximumSize(rowSize);
        weightInfo.setMaximumSize(rowSize);

        this.add(title);
        this.add(usernameLabel);
        this.add(heightInfo);
        this.add(weightInfo);
        this.add(new JLabel("Activity Level"));
        this.add(activityLevelBox);
        this.add(new JLabel("Fitness Goal"));
        this.add(goalBox);
        this.add(pictureLabel);
        this.add(choosePictureButton);
        this.add(saveButton);
        this.add(statusLabel);

        displayState(profileViewModel.getState());
    }

    private void onChoosePicture(ActionEvent evt) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image files", "png", "jpg", "jpeg", "gif"));
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
            final float heightCm = Float.parseFloat(heightField.getText().trim());
            final float weight = Float.parseFloat(weightField.getText().trim());
            final ActivityLevel activityLevel = (ActivityLevel) activityLevelBox.getSelectedItem();
            final FitnessGoal goal = (FitnessGoal) goalBox.getSelectedItem();
            profileController.execute(heightCm / CM_PER_METRE, weight, activityLevel, goal, selectedProfilePicturePath);
        }
        catch (NumberFormatException ex) {
            statusLabel.setText("Height and weight must be numbers.");
        }
    }

    private void setPicturePreview(String path) {
        if (path == null) {
            pictureLabel.setText("No picture selected");
            pictureLabel.setIcon(null);
            return;
        }
        final ImageIcon icon = new ImageIcon(path);
        final Image scaled = icon.getImage().getScaledInstance(
                PICTURE_PREVIEW_SIZE, PICTURE_PREVIEW_SIZE, Image.SCALE_SMOOTH);
        pictureLabel.setIcon(new ImageIcon(scaled));
        pictureLabel.setText(null);
    }

    private String heightMetresTextToCentimetresText(String heightMetresText) {
        if (heightMetresText == null || heightMetresText.isEmpty()) {
            return "";
        }
        try {
            final float heightMetres = Float.parseFloat(heightMetresText);
            return String.valueOf(Math.round(heightMetres * CM_PER_METRE));
        }
        catch (NumberFormatException ex) {
            return heightMetresText;
        }
    }

    private void displayState(ProfileState state) {
        usernameLabel.setText("Username: " + state.getUsername());
        heightField.setText(heightMetresTextToCentimetresText(state.getHeightText()));
        weightField.setText(state.getWeightText());
        activityLevelBox.setSelectedItem(state.getActivityLevel());
        goalBox.setSelectedItem(state.getGoal());
        selectedProfilePicturePath = state.getProfilePicturePath();
        setPicturePreview(selectedProfilePicturePath);

        if (state.getProfileError() != null) {
            statusLabel.setText(state.getProfileError());
        }
        else if (state.getSaveConfirmation() != null) {
            statusLabel.setText(state.getSaveConfirmation());
        }
        else {
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
