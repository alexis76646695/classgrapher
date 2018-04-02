package ui.menu.listeners;

import java.awt.event.ActionEvent;

/**
 * Action Listener.
 *
 * @author David
 * @since 01/04/2018
 */
@FunctionalInterface
public interface ActionListener {
  void exec(ActionEvent e);
}
