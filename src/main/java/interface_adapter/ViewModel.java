package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingUtilities;

/**
 * The ViewModel for our Clean Architecture implementation.
 *
 * @param <T> The type of state object contained in the model.
 */
public class ViewModel<T> {

    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private T state;

    /**
     * Constructs a ViewModel instance.
     *
     * @param viewName name identifying the view.
     */
    public ViewModel(final String viewName) {
        this.viewName = viewName;
    }

    /**
     * Gets the view name.
     *
     * @return view name string.
     */
    public String getViewName() {
        return this.viewName;
    }

    /**
     * Gets the state object.
     *
     * @return state object instance.
     */
    public T getState() {
        return this.state;
    }

    /**
     * Sets the state object.
     *
     * @param state state object instance.
     */
    public void setState(final T state) {
        this.state = state;
    }

    /**
     * Fires a property changed event for the state of this ViewModel.
     */
    public void firePropertyChanged() {
        runOnEdt(() -> this.support.firePropertyChange("state", null, this.state));
    }

    /**
     * Fires a property changed event with a specific property name.
     *
     * @param propertyName property identifier label.
     */
    public void firePropertyChanged(final String propertyName) {
        runOnEdt(() -> this.support.firePropertyChange(propertyName, null, this.state));
    }

    private void runOnEdt(final Runnable dispatch) {
        if (SwingUtilities.isEventDispatchThread()) {
            dispatch.run();
        }
        else {
            SwingUtilities.invokeLater(dispatch);
        }
    }

    /**
     * Adds a PropertyChangeListener to this ViewModel.
     *
     * @param listener listener instance to be added.
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }
}
