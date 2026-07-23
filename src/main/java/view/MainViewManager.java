package view;

import interface_adapter.MainViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainViewManager implements PropertyChangeListener {

    private final CardLayout cardLayout;
    private final JPanel mainView;
    private final MainViewManagerModel mainViewManagerModel;

    public MainViewManager(JPanel mainView, CardLayout cardLayout, MainViewManagerModel mainViewManagerModel) {
        this.mainViewManagerModel = mainViewManagerModel;
        this.cardLayout = cardLayout;
        this.mainView = mainView;
        this.mainViewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final String viewModelName = (String) evt.getNewValue();
            cardLayout.show(this.mainView, viewModelName);
        }
    }
}