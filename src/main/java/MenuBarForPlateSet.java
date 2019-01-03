package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.help.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class MenuBarForPlateSet extends JMenuBar {

  DialogMainFrame dmf;
  JTable plate_set_table;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public MenuBarForPlateSet(DialogMainFrame _dmf, JTable _plate_set_table) {

    dmf = _dmf;
    plate_set_table = _plate_set_table;

    JMenu menu = new JMenu("Plate Set");
    menu.setMnemonic(KeyEvent.VK_P);
    menu.getAccessibleContext().setAccessibleDescription("Menu items related to plate sets");
    this.add(menu);

    // a group of JMenuItems
    JMenuItem menuItem = new JMenuItem("Add plate set", KeyEvent.VK_A);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Add Plate Set dialog.");
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new DialogAddPlateSet(dmf);
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("Edit plate set");
    menuItem.setMnemonic(KeyEvent.VK_E);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            // new DialogEditPlateSetData();
          }
        });
    menu.add(menuItem);

    JMenu utilitiesMenu = new JMenu("Utilities");
    menu.setMnemonic(KeyEvent.VK_U);
    this.add(utilitiesMenu);

    menuItem = new JMenuItem("Group");
    menuItem.setMnemonic(KeyEvent.VK_G);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {}
        });
    utilitiesMenu.add(menuItem);

    menuItem = new JMenuItem("Import assay data");
    menuItem.setMnemonic(KeyEvent.VK_I);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new DialogAddPlateSetData();
          }
        });
    utilitiesMenu.add(menuItem);

    JButton downbutton = new JButton();
    try {
      ImageIcon down = new ImageIcon(this.getClass().getResource("images/ddown.png"));
      downbutton.setIcon(down);
    } catch (Exception ex) {
      System.out.println(ex + " ddown.PNG image not found");
    }
    downbutton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            try {
              int i = plate_set_table.getSelectedRow();
              String plate_sys_name = (String) plate_set_table.getValueAt(i, 0);
              System.out.println("plate_sys_name: " + plate_sys_name);
              dmf.showPlateTable(plate_sys_name);
            } catch (ArrayIndexOutOfBoundsException s) {
            }
          }
        });
    this.add(downbutton);

    JButton upbutton = new JButton();

    try {
      ImageIcon up = new ImageIcon(this.getClass().getResource("images/dup.png"));
      upbutton.setIcon(up);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    upbutton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            dmf.showProjectTable();
          }
        });
    this.add(upbutton);

    this.add(Box.createHorizontalGlue());

    menu = new HelpMenu();
    this.add(menu);
  }
}
