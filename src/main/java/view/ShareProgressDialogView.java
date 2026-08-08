package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import interface_adapter.share.ShareProgressController;
import interface_adapter.share.ShareProgressState;
import interface_adapter.share.ShareProgressViewModel;

/**
 * Dialog View showing preview of content to share and recipient input field.
 */
public class ShareProgressDialogView extends JDialog implements PropertyChangeListener {

    private static final int PREVIEW_IMAGE_SIZE = 80;

    private final ShareProgressViewModel viewModel;
    private ShareProgressController controller;

    private final JTextArea previewArea = new JTextArea(12, 40);
    private final JLabel imageLabel = new JLabel();
    private final JTextField recipientEmailField = new JTextField(20);
    private final JButton sendButton = new JButton("Send Email");
    private final JLabel statusLabel = new JLabel();

    /**
     * Constructs a ShareProgressDialogView instance.
     *
     * @param parent parent window frame
     * @param viewModel view model for share progress
     */
    public ShareProgressDialogView(final Frame parent, final ShareProgressViewModel viewModel) {
        super(parent, "Share Progress", true);
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setSize(500, 450);
        setLocationRelativeTo(parent);

        this.previewArea.setEditable(false);
        this.previewArea.setLineWrap(true);

        final JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Sharing Content Preview"));
        centerPanel.add(new JScrollPane(this.previewArea), BorderLayout.CENTER);
        centerPanel.add(this.imageLabel, BorderLayout.EAST);

        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(new JLabel("Recipient Email:"));
        bottomPanel.add(this.recipientEmailField);
        bottomPanel.add(this.sendButton);

        final JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(this.statusLabel);

        final JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.add(centerPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);
        container.add(statusPanel, BorderLayout.NORTH);

        add(container);

        this.sendButton.addActionListener(evt -> {
            if (this.controller != null) {
                this.controller.executeSendEmail(this.recipientEmailField.getText().trim());
            }
        });
    }

    /**
     * Sets the controller for executing share operations.
     *
     * @param controller controller instance
     */
    public void setController(final ShareProgressController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final ShareProgressState state = (ShareProgressState) evt.getNewValue();

        this.previewArea.setText(state.getPreviewText());
        this.statusLabel.setText(state.getStatusMessage());

        if (state.getProfilePicturePath() != null && !state.getProfilePicturePath().isBlank()) {
            try {
                final ImageIcon icon = new ImageIcon(state.getProfilePicturePath());
                final Image scaled = icon.getImage().getScaledInstance(
                        PREVIEW_IMAGE_SIZE, PREVIEW_IMAGE_SIZE, Image.SCALE_SMOOTH);
                this.imageLabel.setIcon(new ImageIcon(scaled));
            }
            catch (final Exception ex) {
                this.imageLabel.setIcon(null);
            }
        }
        else {
            this.imageLabel.setIcon(null);
        }

        if (state.isSuccess()) {
            JOptionPane.showMessageDialog(this, state.getStatusMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
        }
        else if (state.getStatusMessage() != null && !state.getStatusMessage().isEmpty()
                && state.getPreviewText().isEmpty()) {
            JOptionPane.showMessageDialog(getParent(), state.getStatusMessage(), "Sharing Notice",
                    JOptionPane.WARNING_MESSAGE);
        }
        else if (!state.getPreviewText().isEmpty()) {
            setVisible(true);
        }
    }
}
