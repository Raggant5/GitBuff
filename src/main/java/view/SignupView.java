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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;

/**
 * The View for the Signup Use Case.
 */
public class SignupView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final int ROW_WIDTH = 450;
    private static final int ROW_HEIGHT = 45;
    private static final int INPUT_WIDTH = 250;
    private static final int INPUT_HEIGHT = 30;
    private static final int FIELD_COLUMNS = 15;
    private static final int VSTRUT_LARGE = 15;
    private static final int VSTRUT_SMALL = 5;

    private final String viewName = "sign up";

    private final SignupViewModel signupViewModel;
    private final JTextField usernameInputField = new JTextField(FIELD_COLUMNS);
    private final JPasswordField passwordInputField = new JPasswordField(FIELD_COLUMNS);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(FIELD_COLUMNS);
    private final JLabel statusLabel = new JLabel();
    private SignupController signupController;

    private final JButton signUp;
    private final JButton cancel;
    private final JButton toLogin;

    /**
     * Constructs a SignupView instance.
     *
     * @param signupViewModel view model for managing signup state
     */
    public SignupView(final SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        this.signupViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.USERNAME_LABEL), this.usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.PASSWORD_LABEL), this.passwordInputField);
        final LabelTextPanel repeatPasswordInfo = new LabelTextPanel(
                new JLabel(SignupViewModel.REPEAT_PASSWORD_LABEL), this.repeatPasswordInputField);

        final JPanel buttons = new JPanel();
        this.toLogin = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        buttons.add(this.toLogin);
        this.signUp = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        buttons.add(this.signUp);
        this.cancel = new JButton(SignupViewModel.CANCEL_BUTTON_LABEL);
        buttons.add(this.cancel);

        this.signUp.addActionListener(evt -> {
            if (evt.getSource().equals(SignupView.this.signUp) && SignupView.this.signupController != null) {
                final SignupState currentState = SignupView.this.signupViewModel.getState();
                SignupView.this.signupController.execute(
                        currentState.getUsername(),
                        currentState.getPassword(),
                        currentState.getRepeatPassword()
                );
            }
        });

        this.toLogin.addActionListener(evt -> {
            if (SignupView.this.signupController != null) {
                SignupView.this.signupController.switchToLoginView();
            }
        });

        this.cancel.addActionListener(this);

        addUsernameListener();
        addPasswordListener();
        addRepeatPasswordListener();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final Dimension rowSize = new Dimension(ROW_WIDTH, ROW_HEIGHT);
        final Dimension inputSize = new Dimension(INPUT_WIDTH, INPUT_HEIGHT);

        this.usernameInputField.setPreferredSize(inputSize);
        this.usernameInputField.setMinimumSize(inputSize);
        this.usernameInputField.setMaximumSize(inputSize);

        this.passwordInputField.setPreferredSize(inputSize);
        this.passwordInputField.setMinimumSize(inputSize);
        this.passwordInputField.setMaximumSize(inputSize);

        this.repeatPasswordInputField.setPreferredSize(inputSize);
        this.repeatPasswordInputField.setMinimumSize(inputSize);
        this.repeatPasswordInputField.setMaximumSize(inputSize);

        usernameInfo.setPreferredSize(rowSize);
        usernameInfo.setMinimumSize(rowSize);
        usernameInfo.setMaximumSize(rowSize);

        passwordInfo.setPreferredSize(rowSize);
        passwordInfo.setMinimumSize(rowSize);
        passwordInfo.setMaximumSize(rowSize);

        repeatPasswordInfo.setPreferredSize(rowSize);
        repeatPasswordInfo.setMinimumSize(rowSize);
        repeatPasswordInfo.setMaximumSize(rowSize);

        buttons.setMaximumSize(buttons.getPreferredSize());

        this.statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createVerticalGlue());
        this.add(title);
        this.add(Box.createVerticalStrut(VSTRUT_LARGE));
        this.add(usernameInfo);
        this.add(Box.createVerticalStrut(VSTRUT_SMALL));
        this.add(passwordInfo);
        this.add(Box.createVerticalStrut(VSTRUT_SMALL));
        this.add(repeatPasswordInfo);
        this.add(Box.createVerticalStrut(VSTRUT_SMALL));
        this.add(this.statusLabel);
        this.add(Box.createVerticalStrut(VSTRUT_SMALL));
        this.add(buttons);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Binds to workouts view model to reflect schedule loading status.
     *
     * @param workoutsViewModel view model for workout state
     */
    public void setWorkoutsViewModel(final WorkoutsViewModel workoutsViewModel) {
        if (workoutsViewModel != null) {
            workoutsViewModel.addPropertyChangeListener(evt -> {
                if (evt.getNewValue() instanceof WorkoutsState) {
                    final WorkoutsState state = (WorkoutsState) evt.getNewValue();
                    if (state.isLoading()) {
                        this.statusLabel.setText("Loading workout schedule...");
                    }
                    else if ("Loading workout schedule...".equals(this.statusLabel.getText())) {
                        this.statusLabel.setText("");
                    }
                }
            });
        }
    }

    private void addUsernameListener() {
        this.usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final SignupState currentState = SignupView.this.signupViewModel.getState();
                currentState.setUsername(SignupView.this.usernameInputField.getText());
                SignupView.this.signupViewModel.setState(currentState);
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
    }

    private void addPasswordListener() {
        this.passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final SignupState currentState = SignupView.this.signupViewModel.getState();
                currentState.setPassword(new String(SignupView.this.passwordInputField.getPassword()));
                SignupView.this.signupViewModel.setState(currentState);
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
    }

    private void addRepeatPasswordListener() {
        this.repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final SignupState currentState = SignupView.this.signupViewModel.getState();
                currentState.setRepeatPassword(new String(SignupView.this.repeatPasswordInputField.getPassword()));
                SignupView.this.signupViewModel.setState(currentState);
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
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Cancel not implemented yet.");
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
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
     * Sets the signup controller.
     *
     * @param controller controller instance
     */
    public void setSignupController(final SignupController controller) {
        this.signupController = controller;
    }
}
