package pm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.imageio.ImageIO;
import java.util.logging.*;


public class MenuBarForPlates extends JMenuBar {

  private static final long serialVersionUID = 1L;
private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
 
  public MenuBarForPlates( ) {

    // Create the menu bar.
    // JMenuBar menuBar = new JMenuBar();
    //    this.em = em;
    // Build the first menu.
    JMenu menu = new JMenu("Plates");
    menu.setMnemonic(KeyEvent.VK_P);
    menu.getAccessibleContext()
        .setAccessibleDescription("The only menu in this program that has menu items");
    this.add(menu);

    // a group of JMenuItems
    JMenuItem menuItem = new JMenuItem("Add plate", KeyEvent.VK_A);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Add Project dialog.");
    menuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
	  // new DialogAddProject();
	}});
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
this.add(Box.createHorizontalGlue());


    menu = new JMenu("Help");
    menu.setMnemonic(KeyEvent.VK_H);
    menu.getAccessibleContext()
        .setAccessibleDescription("Launch help system");
    this.add(menu);

    
  }

 
}
