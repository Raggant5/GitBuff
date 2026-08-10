package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final int ROW_WIDTH = 450;
    private static final int ROW_HEIGHT = 45;
    private static final int INPUT_WIDTH = 250;
    private static final int INPUT_HEIGHT = 30;
    private static final int ERROR_HEIGHT = 25;
    private static final int FIELD_COLUMNS = 15;

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(FIELD_COLUMNS);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(FIELD_COLUMNS);
    private final JLabel statusLabel = new JLabel();

    private final JButton logIn;
    private final JButton signup;
    private LoginController loginController;

    /**
     * Constructs a LoginView instance.
     *
     * @param loginViewModel view model for managing login state
     */
    public LoginView(final LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), this.usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), this.passwordInputField);

        final JPanel buttons = new JPanel();
        this.logIn = new JButton("log in");
        buttons.add(this.logIn);
        this.signup = new JButton("signup");
        buttons.add(this.signup);

        this.logIn.addActionListener(evt -> {
            if (evt.getSource().equals(LoginView.this.logIn) && LoginView.this.loginController != null) {
                final LoginState currentState = LoginView.this.loginViewModel.getState();
                LoginView.this.loginController.execute(
                        currentState.getUsername(),
                        currentState.getPassword()
                );
            }
        });

        this.signup.addActionListener(evt -> {
            if (evt.getSource().equals(LoginView.this.signup) && LoginView.this.loginController != null) {
                final LoginState currentState = LoginView.this.loginViewModel.getState();
                currentState.setUsername("");
                currentState.setPassword("");
                currentState.setLoginError("");
                LoginView.this.loginViewModel.firePropertyChanged();
                LoginView.this.loginController.switchToSignupView();
            }
        });

        this.usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = LoginView.this.loginViewModel.getState();
                currentState.setUsername(LoginView.this.usernameInputField.getText());
                LoginView.this.loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = LoginView.this.loginViewModel.getState();
                currentState.setPassword(new String(LoginView.this.passwordInputField.getPassword()));
                LoginView.this.loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                documentListenerHelper();
            }
        });

        final Dimension rowSize = new Dimension(ROW_WIDTH, ROW_HEIGHT);
        final Dimension inputSize = new Dimension(INPUT_WIDTH, INPUT_HEIGHT);

        this.usernameInputField.setPreferredSize(inputSize);
        this.usernameInputField.setMinimumSize(inputSize);
        this.usernameInputField.setMaximumSize(inputSize);

        this.passwordInputField.setPreferredSize(inputSize);
        this.passwordInputField.setMinimumSize(inputSize);
        this.passwordInputField.setMaximumSize(inputSize);

        usernameInfo.setPreferredSize(rowSize);
        usernameInfo.setMinimumSize(rowSize);
        usernameInfo.setMaximumSize(rowSize);

        passwordInfo.setPreferredSize(rowSize);
        passwordInfo.setMinimumSize(rowSize);
        passwordInfo.setMaximumSize(rowSize);

        this.usernameErrorField.setPreferredSize(new Dimension(ROW_WIDTH, ERROR_HEIGHT));
        this.usernameErrorField.setMinimumSize(new Dimension(ROW_WIDTH, ERROR_HEIGHT));
        this.usernameErrorField.setMaximumSize(new Dimension(ROW_WIDTH, ERROR_HEIGHT));

        this.statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.setMaximumSize(buttons.getPreferredSize());

        this.add(Box.createVerticalGlue());
        this.add(title);
        this.add(usernameInfo);
        this.add(this.usernameErrorField);
        this.add(passwordInfo);
        this.add(this.statusLabel);
        this.add(buttons);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Binds to workouts view model to reflect loading status during login.
     *
     * @param workoutsViewModel view model for workout schedule state
     */
    public void setWorkoutsViewModel(final WorkoutsViewModel workoutsViewModel) {
        if (workoutsViewModel != null) {
            workoutsViewModel.addPropertyChangeListener(evt -> {
                if (evt.getNewValue() instanceof WorkoutsState) {
                    this.statusLabel.setText("");
                    final WorkoutsState state = (WorkoutsState) evt.getNewValue();
                    if (state.isLoading()) {
                        this.statusLabel.setText("Loading workout schedule...");
                    }
                }
            });
        }
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        // Reserved for direct action command listening if required
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        setFields(state);
        this.usernameErrorField.setText(state.getLoginError());
    }

    private void setFields(final LoginState state) {
        this.usernameInputField.setText(state.getUsername());
        this.passwordInputField.setText(state.getPassword());
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
     * Sets the controller for executing login actions.
     *
     * @param loginController controller instance
     */
    public void setLoginController(final LoginController loginController) {
        this.loginController = loginController;
    }
}
