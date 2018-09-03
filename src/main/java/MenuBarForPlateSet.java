package pm;

import java.awt.*;
import java.awt.event.*;
import javax.persistence.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.imageio.ImageIO;


public class MenuBarForPlateSet extends JMenuBar {

  private static final long serialVersionUID = 1L;
 
  public MenuBarForPlateSet( ) {

    // Create the menu bar.
    // JMenuBar menuBar = new JMenuBar();
    //    this.em = em;
    // Build the first menu.
    JMenu menu = new JMenu("PlateSet");
    menu.setMnemonic(KeyEvent.VK_P);
    menu.getAccessibleContext()
        .setAccessibleDescription("The only menu in this program that has menu items");
    this.add(menu);

    // a group of JMenuItems
    JMenuItem menuItem = new JMenuItem("Add plate set", KeyEvent.VK_A);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Launch the Add Project dialog.");
    menuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
	    new DialogAddPlateSet();
	}});
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
     Image img = ImageIO.read(getClass().getResource("resources/ddown.png"));
     downbutton.setIcon(new ImageIcon(img));
   } catch (Exception ex) {
     System.out.println(ex);
   }
  downbutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
	  JPanel target =  (JPanel)((JComponent)e.getSource()).getParent().getParent().getParent();
	  CardLayout  cl = (CardLayout)target.getLayout();
	  cl.show( target, DialogMainFrame.PLATESETPANEL);
	 
	}});
    this.add( downbutton );
    
    JButton upbutton = new JButton();
    
    try {
       Image img = ImageIO.read(getClass().getResource("resources/dup.png"));
      upbutton.setIcon(new ImageIcon(img));
  } catch (Exception ex) {
    System.out.println(ex);
  }
 upbutton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
JPanel target =  (JPanel)((JComponent)e.getSource()).getParent().getParent().getParent();
	  CardLayout  cl = (CardLayout)target.getLayout();
	  cl.show( target, DialogMainFrame.PROJECTPANEL);
	}});
 

    
    this.add( upbutton );
   //    menu.setMnemonic(KeyEvent.VK_T);
    //menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
    //this.add(menu);
    this.add(Box.createHorizontalGlue());


    menu = new JMenu("Help");
    menu.setMnemonic(KeyEvent.VK_H);
    menu.getAccessibleContext()
        .setAccessibleDescription("Launch help system");
    this.add(menu);

    
  }

 
}
