package view;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import interface_adapter.MainViewManagerModel;

/**
 * Manages card layout switching for main application views.
 */
public class MainViewManager implements PropertyChangeListener {

    private final CardLayout cardLayout;
    private final JPanel mainView;
    private final MainViewManagerModel mainViewManagerModel;

    /**
     * Constructs a MainViewManager instance.
     *
     * @param mainView main card panel container
     * @param cardLayout card layout instance
     * @param mainViewManagerModel view manager model
     */
    public MainViewManager(final JPanel mainView, final CardLayout cardLayout,
                           final MainViewManagerModel mainViewManagerModel) {
        this.mainViewManagerModel = mainViewManagerModel;
        this.cardLayout = cardLayout;
        this.mainView = mainView;
        this.mainViewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final String viewModelName = (String) evt.getNewValue();
            this.cardLayout.show(this.mainView, viewModelName);
        }
    }
}
