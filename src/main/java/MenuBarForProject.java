package pm;

import bllm.*;
import java.awt.*;
import java.awt.Dialog.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.help.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class MenuBarForProject extends JMenuBar {

  DialogMainFrame mainFrame;
  JTable project_table;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public MenuBarForProject(DialogMainFrame _dmf, JTable _project_table) {

    mainFrame = _dmf;
    project_table = _project_table;

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

    menuItem = new JMenuItem("Edit project", KeyEvent.VK_E);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Edit Project dialog.");
    menuItem.putClientProperty("mf", mainFrame);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              int rowIndex = project_table.getSelectedRow();
              LOGGER.info(project_table.getValueAt(rowIndex, 0).toString());
              LOGGER.info(project_table.getValueAt(rowIndex, 1).toString());
              LOGGER.info(project_table.getValueAt(rowIndex, 2).toString());
              String projectid = project_table.getValueAt(rowIndex, 0).toString();
              String name = project_table.getValueAt(rowIndex, 1).toString();
              String description = project_table.getValueAt(rowIndex, 3).toString();

              new DialogEditProject(mainFrame, projectid, name, description);
            } catch (ArrayIndexOutOfBoundsException aioob) {
              JOptionPane.showMessageDialog(
                  mainFrame, "Please select a project!", "Error", JOptionPane.ERROR_MESSAGE);
            }
          }
        });
    menu.add(menuItem);

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

    menu = new HelpMenu();

    this.add(menu);
  }
}
