package pm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.net.*;
import java.awt.Desktop;

import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;

public class DialogPropertiesNotFound extends JDialog
    implements java.awt.event.ActionListener, javax.swing.event.DocumentListener {
  static JButton button;
  static JLabel label;
  static JLabel nLabel;
    
  static JTextField fileField;
  static JTextField nameField;
  static JTextField descrField;
  static JTextField layoutField;
  static JTextField nField;
    
  static JButton okButton;
  static JButton elephantsql;

  static JButton select;
  static JButton cancelButton;
  private static final long serialVersionUID = 1L;
  private DialogMainFrame dmf;
   
  private JFileChooser fileChooser;
    private Session session;
    
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public DialogPropertiesNotFound( Session _s) {

    session = _s;
    dmf = session.getDialogMainFrame();
    
    fileChooser = new JFileChooser();

    JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    // Image img = new
    // ImageIcon(DialogAddProject.class.getResource("../resources/mwplate.png")).getImage();
    // this.setIconImage(img);
    this.setTitle("Properties File Not Found!");
    // c.gridwidth = 2;
    label = new JLabel("OR");
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.insets = new Insets(5, 5, 2, 2);
  pane.add(label, c);
    
   
  

    elephantsql =  new JButton( "Connect to ElephantSQL");
    elephantsql.setMnemonic(KeyEvent.VK_E);
    
    elephantsql.setEnabled(true);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    elephantsql.addActionListener(this);
    pane.add(elephantsql, c);

    label = new JLabel("for evaluation purposes only - no personal data.");
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 3;
    c.gridheight = 1;
    c.insets = new Insets(5, 5, 2, 2);
  pane.add(label, c);

    
    select =
        new JButton(
            "Select properties file...", createImageIcon("/toolbarButtonGraphics/general/Open16.gif"));
    select.setMnemonic(KeyEvent.VK_O);
    select.setActionCommand("select");
    select.setEnabled(true);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 6;
    c.gridwidth = 1;
    c.gridheight = 1;
    select.addActionListener(this);
    pane.add(select, c);

    fileField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 6;
    c.gridwidth = 5;
    c.gridheight = 1;
    fileField.getDocument().addDocumentListener(this);
    pane.add(fileField, c);


    
    JButton helpButton = new JButton("Help");
    helpButton.setMnemonic(KeyEvent.VK_H);
    helpButton.setActionCommand("help");
    c.fill = GridBagConstraints.NONE;
    c.gridx = 5;
    c.gridy = 8;
    c.gridwidth = 1;
    c.gridheight = 1;
    pane.add(helpButton, c);
      try {
      ImageIcon help =
          new ImageIcon(this.getClass().getResource("/toolbarButtonGraphics/general/Help16.gif"));
      helpButton.setIcon(help);
    } catch (Exception ex) {
      System.out.println("Can't find help icon: " + ex);
    }
    helpButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
	      openWebpage(URI.create(session.getHelpURLPrefix() + "login"));
          }
        });
    helpButton.setSize(10, 10);
    //; helpButton.setPreferredSize(new Dimension(5, 20));
    // helpButton.setBounds(new Rectangle(
    //             getLocation(), getPreferredSize()));
    //helpButton.setMargin(new Insets(1, -40, 1, -100)); //(top, left, bottom, right)

    
    okButton = new JButton("Connect (properties database)" );
    okButton.setMnemonic(KeyEvent.VK_C);
    okButton.setActionCommand("ok");
    okButton.setEnabled(true);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 8;
    c.gridwidth = 1;
    c.gridheight = 1;
    pane.add(okButton, c);
    okButton.setEnabled(false);
    okButton.addActionListener(this);

    cancelButton = new JButton("Cancel");
    cancelButton.setMnemonic(KeyEvent.VK_C);
    cancelButton.setActionCommand("cancel");
    cancelButton.setEnabled(true);
    cancelButton.setForeground(Color.RED);
    c.gridx = 2;
    c.gridy = 8;
    pane.add(cancelButton, c);
    cancelButton.addActionListener(
        (new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            dispose();
          }
        }));

    this.getContentPane().add(pane, BorderLayout.CENTER);
    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

  /** Returns an ImageIcon, or null if the path was invalid. */
  protected static ImageIcon createImageIcon(String path) {
    java.net.URL imgURL = DialogPropertiesNotFound.class.getResource(path);
    if (imgURL != null) {
      return new ImageIcon(imgURL);
    } else {
      System.err.println("Couldn't find file: " + path);
      return null;
    }
  }

  public void actionPerformed(ActionEvent e) {
      int top_n_number = 0;

    if (e.getSource() == okButton) {
	try{
	FileInputStream fis = new FileInputStream(fileField.getText());
	session.setPropertiesFile(fis);
	session.loadProperties();
	if(session.getUserName().equals("null")){
	    new DialogLogin(session, "", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
	}else{
	    session.postLoadProperties();
	}
       
	this.dispose();
	}catch(FileNotFoundException fnfe){
	    
	}
    }
	
  if (e.getSource() == elephantsql) {
      session.setupElephantSQL();
      this.dispose();
  }
    

    if (e.getSource() == select) {
      int returnVal = fileChooser.showOpenDialog(DialogPropertiesNotFound.this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        java.io.File file = fileChooser.getSelectedFile();
        // This is where a real application would open the file.
        fileField.setText(file.toString());
      } else {
        LOGGER.info("Open command cancelled by user.\n");
      }
    }
  }

  public void insertUpdate(DocumentEvent e) {

    if ( fileField.getText().length() > 0) {
      okButton.setEnabled(true);
      elephantsql.setEnabled(false);
    } else {
      okButton.setEnabled(false);
      elephantsql.setEnabled(true);
    }
  }

  public void removeUpdate(DocumentEvent e) {
    if ( fileField.getText().length() > 0) {
      okButton.setEnabled(true);
      elephantsql.setEnabled(false);
    } else {
      okButton.setEnabled(false);
      elephantsql.setEnabled(true);
    }
  }

  public void changedUpdate(DocumentEvent e) {
    // Plain text components don't fire these events.
  }

public static boolean openWebpage(URI uri) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        try {
            desktop.browse(uri);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return false;
}

    public static boolean openWebpage(URL url) {
    try {
        return openWebpage(url.toURI());
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
    return false;
}

}
