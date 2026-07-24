package view;

import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProfileView extends JPanel implements PropertyChangeListener {

    private final String viewName = "profile";
    private final JLabel profileLabel;
    private final ProfileViewModel profileViewModel;

    public ProfileView(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
        this.profileViewModel.addPropertyChangeListener(this);
        profileViewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        profileLabel = new JLabel();
        this.add(profileLabel, BorderLayout.CENTER);
        profileLabel.setText(profileViewModel.getState().getUsername());
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        profileLabel.setText(((ProfileState) evt.getNewValue()).getUsername());
    }
    public String getViewName() {
        return viewName;
    }
}