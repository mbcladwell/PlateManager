package pm;

import bllm.*;
import java.awt.*;
import java.awt.event.*;
import javax.help.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import java.util.logging.*;

public class MenuBarForProject extends JMenuBar {

  DialogMainFrame mainFrame;
  JTable project_table;
private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public MenuBarForProject(DialogMainFrame _dmf, JTable _project_table) {

    mainFrame = _dmf;
    project_table = _project_table;

    // Create the menu bar.
    // JMenuBar menuBar = new JMenuBar();
    //    this.em = em;
    // Build the first menu.
    JMenu menu = new JMenu("Project");
    menu.setMnemonic(KeyEvent.VK_P);
    menu.getAccessibleContext()
        .setAccessibleDescription("The only menu in this program that has menu items");
    this.add(menu);

    // a group of JMenuItems
    JMenuItem menuItem = new JMenuItem("Add project", KeyEvent.VK_A);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Add Project dialog.");
    menuItem.putClientProperty("mf", mainFrame);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            new DialogAddProject(
                (DialogMainFrame) ((JMenuItem) e.getSource()).getClientProperty("mf"));
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("Both text and icon", new ImageIcon("images/middle.gif"));
    menuItem.setMnemonic(KeyEvent.VK_B);
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
              int i = project_table.getSelectedRow();
              String project_sys_name = (String) project_table.getValueAt(i, 0);
              System.out.println("i: " + project_sys_name);
              mainFrame.showPlateSetTable(project_sys_name);
            } catch (ArrayIndexOutOfBoundsException s) {
            }
          }
        });
    this.add(downbutton);

    this.add(Box.createHorizontalGlue());

    menu = new JMenu("Help");
    menu.setMnemonic(KeyEvent.VK_H);
    menu.getAccessibleContext().setAccessibleDescription("Launch help system");

    menuItem = new JMenuItem("Launch Help System", KeyEvent.VK_L);
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
            new bllm.DialogLicenseManager(
                "My Application", "./license.ser", "nszpx5U5Kt6d91JB3CW31n3SiNjSUzcZ");
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("About My 2019 Application", KeyEvent.VK_A);
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
