package pm;

import bllm.*;
import java.awt.*;
import java.awt.Dialog.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.help.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class MenuBarForProject extends JMenuBar {

  DialogMainFrame dmf;
  CustomTable project_table;

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public MenuBarForProject(DialogMainFrame _dmf, CustomTable _project_table) {

    dmf = _dmf;
    project_table = _project_table;

    JMenu menu = new JMenu("Project");
    menu.setMnemonic(KeyEvent.VK_P);
    menu.getAccessibleContext().setAccessibleDescription("Project");
    this.add(menu);

    // a group of JMenuItems
    JMenuItem menuItem = new JMenuItem("Add project", KeyEvent.VK_A);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Add Project dialog.");
    menuItem.putClientProperty("mf", dmf);
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
    menuItem.putClientProperty("mf", dmf);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              int rowIndex = project_table.getSelectedRow();
              String projectid = project_table.getValueAt(rowIndex, 0).toString();
              String name = project_table.getValueAt(rowIndex, 1).toString();
              String owner = project_table.getValueAt(rowIndex, 2).toString();
              String description = project_table.getValueAt(rowIndex, 3).toString();
              if (owner.equals(dmf.getSession().getUserName())) {
                new DialogEditProject(dmf, projectid, name, description);
              } else {
                JOptionPane.showMessageDialog(
                    dmf,
                    "Only the owner can modify a project.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
              }
            } catch (ArrayIndexOutOfBoundsException aioob) {
              JOptionPane.showMessageDialog(
                  dmf, "Please select a project!", "Error", JOptionPane.ERROR_MESSAGE);
            }
          }
        });
    menu.add(menuItem);

    menu = new JMenu("Utilities");
    menu.setMnemonic(KeyEvent.VK_U);
    menu.getAccessibleContext().setAccessibleDescription("Project utilities");
    this.add(menu);

    menuItem = new JMenuItem("Export", KeyEvent.VK_E);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Export as .csv.");
    menuItem.putClientProperty("mf", dmf);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            String[][] results = project_table.getSelectedRowsAndHeaderAsStringArray();
            POIUtilities poi = new POIUtilities(dmf);
            poi.writeJTableToSpreadsheet("Projects", results);
            try {
              Desktop d = Desktop.getDesktop();
              d.open(new File("./Writesheet.xlsx"));
            } catch (IOException ioe) {
            }
            // JWSFileChooserDemo jwsfcd = new JWSFileChooserDemo();
            // jwsfcd.createAndShowGUI();

          }
        });
    menu.add(menuItem);

    JButton downbutton = new JButton();
    try {
      ImageIcon down =
          new ImageIcon(
              this.getClass().getResource("/toolbarButtonGraphics/navigation/Down16.gif"));
      downbutton.setIcon(down);
    } catch (Exception ex) {
      System.out.println(ex + " down image not found");
    }
    downbutton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            try {
              int i = project_table.getSelectedRow();
              String project_sys_name = (String) project_table.getValueAt(i, 0);
              dmf.getDatabaseManager().updateSessionWithProject(project_sys_name);

              dmf.showPlateSetTable(project_sys_name);
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
