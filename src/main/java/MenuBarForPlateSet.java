package pm;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class MenuBarForPlateSet extends JMenuBar {

  DialogMainFrame dmf;
  CustomTable plate_set_table;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public MenuBarForPlateSet(DialogMainFrame _dmf, CustomTable _plate_set_table) {

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
          public void actionPerformed(ActionEvent e) {
            dmf.getDatabaseManager().groupPlateSets(plate_set_table);
          }
        });
    utilitiesMenu.add(menuItem);

        menuItem = new JMenuItem("Reformat");
    menuItem.setMnemonic(KeyEvent.VK_R);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            dmf.getDatabaseManager().reformatPlateSet(plate_set_table);
          }
        });
    utilitiesMenu.add(menuItem);

    menuItem = new JMenuItem("Import assay data");
    menuItem.setMnemonic(KeyEvent.VK_I);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
	    if(!plate_set_table.getSelectionModel().isSelectionEmpty()){
		Object[][] results = plate_set_table.getSelectedRowsAndHeaderAsStringArray();	   
		String plate_set_sys_name = (String) results[1][0];
		int  plate_set_id = Integer.parseInt(plate_set_sys_name.substring(3));
		int format_id = Integer.parseInt((String)results[1][2]);
		int plate_num = Integer.parseInt((String)results[1][3]);
		
		new DialogAddPlateSetData(
					  dmf, plate_set_sys_name, plate_set_id, format_id, plate_num);}
	    else{
	      JOptionPane.showMessageDialog(dmf, "Select a Plate Set to populate with data!");	      
	    }
          }
        });
    utilitiesMenu.add(menuItem);

    menuItem = new JMenuItem("Export", KeyEvent.VK_E);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Export as .csv.");
    menuItem.putClientProperty("mf", dmf);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            Object[][] results = plate_set_table.getSelectedRowsAndHeaderAsStringArray();
            POIUtilities poi = new POIUtilities(dmf);
            poi.writeJTableToSpreadsheet("Plate Sets", results);
            try {
              Desktop d = Desktop.getDesktop();
              d.open(new File("./Writesheet.xlsx"));
            } catch (IOException ioe) {
            }
          }
        });
    utilitiesMenu.add(menuItem);


    
    JButton downbutton = new JButton();
    try {
      ImageIcon down =
          new ImageIcon(
              this.getClass().getResource("/toolbarButtonGraphics/navigation/Down16.gif"));
      downbutton.setIcon(down);
    } catch (Exception ex) {
      System.out.println(ex + " ddown.PNG image not found");
    }
    downbutton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            try {
              int i = plate_set_table.getSelectedRow();
              String plate_set_sys_name = (String) plate_set_table.getValueAt(i, 0);
              dmf.getSession().setPlateSetSysName(plate_set_sys_name);
	      dmf.getSession().setPlateSetID(Integer.parseInt(plate_set_sys_name.substring(3)));
              //System.out.println("plate_set_sys_name: " + plate_set_sys_name);
              dmf.showPlateTable(plate_set_sys_name);
            } catch (ArrayIndexOutOfBoundsException s) {
            }
          }
        });
    this.add(downbutton);

    JButton upbutton = new JButton();

    try {
      ImageIcon up =
          new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/navigation/Up16.gif"));
      upbutton.setIcon(up);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    upbutton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            dmf.showProjectTable();
	    	      dmf.setMainFrameTitle("");

          }
        });
    this.add(upbutton);
    menu = new ViewerMenu(dmf);
    this.add(menu);
   
    this.add(Box.createHorizontalGlue());

    menu = new HelpMenu();
    this.add(menu);
  }
}
