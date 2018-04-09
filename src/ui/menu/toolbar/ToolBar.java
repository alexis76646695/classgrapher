package ui.menu.toolbar;

import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;

import ui.menu.listeners.ActionListener;

public interface ToolBar {
  static JComponent getToolBar(List<JButton> buttons){
    JPanel toolbar = new JPanel(new GridLayout(0, 2));
    buttons.forEach(toolbar::add);
    return toolbar;
  }

  static JButton getButton(String pathImage, ActionListener listener){
    JButton button = new JButton("");
    button.setSize(500,500);
    button.setIcon(new ImageIcon(Class.class.getResource(pathImage)));
    button.addActionListener(listener::exec);
    return button;
  }
}
