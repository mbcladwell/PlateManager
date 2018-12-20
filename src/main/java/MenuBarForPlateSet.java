package pm;

import java.awt.*;
import java.awt.event.*;
import javax.help.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class MenuBarForPlateSet extends JMenuBar {

  DialogMainFrame dmf;
  JTable plate_set_table;

  public MenuBarForPlateSet(DialogMainFrame _dmf, JTable _plate_set_table) {

    dmf = _dmf;
    plate_set_table = _plate_set_table;
    // Create the menu bar.
    // JMenuBar menuBar = new JMenuBar();
    //    this.em = em;
    // Build the first menu.
    JMenu menu = new JMenu("Plate Set");
    menu.setMnemonic(KeyEvent.VK_P);
    menu.getAccessibleContext()
        .setAccessibleDescription("The only menu in this program that has menu items");
    this.add(menu);

    // a group of JMenuItems
    JMenuItem menuItem = new JMenuItem("Add plate set", KeyEvent.VK_A);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Add Plate Set dialog.");
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new DialogAddProject(dmf);
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("Import assay data by plate");
    menuItem.setMnemonic(KeyEvent.VK_I);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new DialogAddPlateSetData();
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
    menuItem.setMnemonic(KeyEvent.VK_D);
    menu.add(menuItem);

    // a submenu
    menu.addSeparator();
    JMenu submenu = new JMenu("A submenu");
    submenu.setMnemonic(KeyEvent.VK_S);

    menuItem = new JMenuItem("An item in the submenu");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
    submenu.add(menuItem);

    menuItem = new JMenuItem("Another item");
    submenu.add(menuItem);
    menu.add(submenu);

    menu = new JMenu("Another Menu");
    menu.setMnemonic(KeyEvent.VK_N);
    menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
    this.add(menu);

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
    //    menu.setMnemonic(KeyEvent.VK_T);
    // menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
    // this.add(menu);
    this.add(Box.createHorizontalGlue());

    menu = new JMenu("Help");
    menu.setMnemonic(KeyEvent.VK_H);
    menu.getAccessibleContext().setAccessibleDescription("Launch help system");

    menuItem = new JMenuItem("Launch Help", KeyEvent.VK_L);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));

    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new OpenHelpDialog();
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("License", KeyEvent.VK_L);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));

    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            //  new llm.DialogLicenseManager("./license.ser");
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("About PlateManager", KeyEvent.VK_A);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));

    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new DialogHelpAbout();
          }
        });
    menu.add(menuItem);

    this.add(menu);
  }
}
