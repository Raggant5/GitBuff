package view;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;

/**
 * The View Manager for the whole program. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 */
public class ViewManager implements PropertyChangeListener {

    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs a ViewManager instance.
     *
     * @param views parent panel containing all view card screens
     * @param cardLayout layout manager handling card transitions
     * @param viewManagerModel model notifying view state changes
     */
    public ViewManager(final JPanel views, final CardLayout cardLayout,
                       final ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final String viewModelName = (String) evt.getNewValue();
            this.cardLayout.show(this.views, viewModelName);
        }
    }
}
