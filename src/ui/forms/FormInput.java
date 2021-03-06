package ui.forms;

import java.awt.*;
import javax.swing.*;

public interface FormInput {
  static String getNameFromInput(){
    JTextField field1 = new JTextField("");
    JPanel panel = preparejPanel(field1);
    int result = JOptionPane.showConfirmDialog(null, panel, "Crear clase.",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    return result == JOptionPane.OK_OPTION ? field1.getText() : "";
  }

  static JPanel preparejPanel(JTextField field1) {
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Nombre de Clase:"));
    panel.add(field1);
    return panel;
  }
}
