package view;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A panel container pairing a label and a text field with standard horizontal flow.
 */
class LabelTextPanel extends JPanel {

    private static final int HORIZONTAL_GAP = 10;
    private static final int VERTICAL_GAP = 5;

    /**
     * Constructs a LabelTextPanel instance.
     *
     * @param label label component
     * @param textField input field component
     */
    LabelTextPanel(final JLabel label, final JTextField textField) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, HORIZONTAL_GAP, VERTICAL_GAP));
        this.add(label);
        this.add(textField);
    }
}
