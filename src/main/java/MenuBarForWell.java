package pm;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class MenuBarForWell extends JMenuBar {

  private static final long serialVersionUID = 1L;
  DialogMainFrame dmf;
  CustomTable well_table;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public MenuBarForWell(DialogMainFrame _dmf, CustomTable _table) {
    dmf = _dmf;
    well_table = _table;
    // Create the menu bar.
    // JMenuBar menuBar = new JMenuBar();
    //    this.em = em;
    // Build the first menu.
    JMenu menu = new JMenu("Well");
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
    menu.getAccessibleContext().setAccessibleDescription("Project utilities");
    this.add(menu);

    menuItem = new JMenuItem("Export", KeyEvent.VK_E);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Export as .csv.");
    menuItem.putClientProperty("mf", dmf);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            String[][] results = well_table.getSelectedRowsAndHeaderAsStringArray();
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
            dmf.flipToPlate();
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
