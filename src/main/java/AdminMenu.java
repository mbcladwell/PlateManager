package pm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class AdminMenu extends JMenu {

DialogMainFrame dmf;
  CustomTable project_table;
    
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public AdminMenu(DialogMainFrame _dmf, CustomTable _project_table) {

      dmf = _dmf;
    project_table = _project_table;

    this.setText("Admin");
    this.setMnemonic(KeyEvent.VK_A);
    this.getAccessibleContext().setAccessibleDescription("Administrative activities");

    JMenuItem menuItem = new JMenuItem("Add user", KeyEvent.VK_U);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
	      new DialogAddUser(dmf);           
          }
        });
    this.add(menuItem);

    menuItem = new JMenuItem("Add Project", KeyEvent.VK_P);
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Add Project dialog.");
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
	      new DialogAddProject(dmf);
   
          }
        });
    
    this.add(menuItem);

    menuItem = new JMenuItem("Edit Project", KeyEvent.VK_E);
	   menuItem.getAccessibleContext().setAccessibleDescription("Launch the Edit Project dialog.");
    menuItem.putClientProperty("mf", dmf);
 
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
	      try{
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
            } catch(ArrayIndexOutOfBoundsException aioob) {
              JOptionPane.showMessageDialog(
                  dmf, "Please select a project!", "Error", JOptionPane.ERROR_MESSAGE);
            }catch(IndexOutOfBoundsException ioob) {
              JOptionPane.showMessageDialog(
                  dmf, "Please select a project!", "Error", JOptionPane.ERROR_MESSAGE);
            }
	 	
	  }
	});
    
    this.add(menuItem);

    menuItem = new JMenuItem("Import Plate Layout", KeyEvent.VK_I);
    menuItem.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
	      new DialogImportPlateLayout(dmf);

	  }
        });
    this.add(menuItem);


    
  }
}
