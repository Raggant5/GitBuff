package view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

/**
 * A panel containing a label and a text field.
 */
class LabelTextPanel extends JPanel {
    LabelTextPanel(JLabel label, JTextField textField) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        this.add(label);
        this.add(textField);
    }
}
