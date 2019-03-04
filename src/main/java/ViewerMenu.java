package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

import javax.help.*;
import javax.swing.*;
import javax.swing.JComponent.*;

import bllm.*;

public class ViewerMenu extends JMenu {

   DialogMainFrame dmf;
  // J/Table table;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public ViewerMenu(DialogMainFrame _dmf) {

      this.dmf = _dmf;
    this.setText("Viewers");
    this.setMnemonic(KeyEvent.VK_V);
    this.getAccessibleContext().setAccessibleDescription("Viewers");

    JMenuItem menuItem = new JMenuItem("Plate Layouts", KeyEvent.VK_P);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new LayoutViewer(dmf);
          }
        });
    this.add(menuItem);

    menuItem = new JMenuItem("License", KeyEvent.VK_L);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new bllm.DialogLicenseManager(
                "My Application", "./license.ser", "nszpx5U5Kt6d91JB3CW31n3SiNjSUzcZ");
          }
        });
    this.add(menuItem);

    menuItem = new JMenuItem("About LIMS*Nucleus", KeyEvent.VK_A);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));

    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new DialogHelpAbout();
          }
        });
    this.add(menuItem);
  }
}
