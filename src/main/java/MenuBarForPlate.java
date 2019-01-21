package pm;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class MenuBarForPlate extends JMenuBar {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  DialogMainFrame dmf;
  CustomTable plate_table;

  public MenuBarForPlate(DialogMainFrame _dmf, CustomTable _table) {
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

    menu = new JMenu("Utilities");
    menu.setMnemonic(KeyEvent.VK_U);
    menu.getAccessibleContext().setAccessibleDescription("Plate utilities");
    this.add(menu);

    menuItem = new JMenuItem("Export", KeyEvent.VK_E);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Export as .csv.");
    menuItem.putClientProperty("mf", dmf);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            String[][] results = plate_table.getSelectedRowsAndHeaderAsStringArray();
            LOGGER.info("results(plate): " + results);
            POIUtilities poi = new POIUtilities(dmf);
            poi.writeJTableToSpreadsheet("Plates", results);
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
      ImageIcon img =
          new ImageIcon(
              this.getClass().getResource("/toolbarButtonGraphics/navigation/Down16.gif"));
      downbutton.setIcon(img);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    downbutton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              int i = plate_table.getSelectedRow();
              String plate_sys_name = (String) plate_table.getValueAt(i, 0);
              dmf.showWellTable(plate_sys_name);
            } catch (ArrayIndexOutOfBoundsException s) {
            }
          }
        });
    this.add(downbutton);

    JButton upbutton = new JButton();

    try {
      ImageIcon img =
          new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/navigation/Up16.gif"));
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
