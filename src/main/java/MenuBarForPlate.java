package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class MenuBarForPlate extends JMenuBar {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  DialogMainFrame dmf;
  JTable plate_table;

  public MenuBarForPlate(DialogMainFrame _dmf, JTable _table) {
    plate_table = _table;
    dmf = _dmf;
    // Create the menu bar.
    // JMenuBar menuBar = new JMenuBar();
    //    this.em = em;
    // Build the first menu.
    JMenu menu = new JMenu("Plate");
    menu.setMnemonic(KeyEvent.VK_P);
    menu.getAccessibleContext()
        .setAccessibleDescription("The only menu in this program that has menu items");
    this.add(menu);

    // a group of JMenuItems
    JMenuItem menuItem = new JMenuItem("Add plate set", KeyEvent.VK_A);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Add Project dialog.");
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new DialogAddPlateSet(dmf);
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("Both text and icon");
    menuItem.setMnemonic(KeyEvent.VK_B);
    menu.add(menuItem);

    menuItem = new JMenuItem();
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
      ImageIcon img = new ImageIcon(this.getClass().getResource("images/ddown.png"));
      downbutton.setIcon(img);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    downbutton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              int i = plate_table.getSelectedRow();
              String plate_sys_name = (String) plate_table.getValueAt(i, 1);
              System.out.println("i: " + plate_sys_name);
              dmf.showWellTable(plate_sys_name);
            } catch (ArrayIndexOutOfBoundsException s) {
            }
          }
        });
    this.add(downbutton);

    JButton upbutton = new JButton();

    try {
      ImageIcon img = new ImageIcon(this.getClass().getResource("images/dup.png"));
      upbutton.setIcon(img);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    upbutton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            dmf.flipToPlateSet();
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
    this.add(menu);
  }
}
