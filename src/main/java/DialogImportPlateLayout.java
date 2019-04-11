package pm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DialogImportPlateLayout extends JDialog implements ActionListener, DocumentListener {
  static JLabel label;
  static JTextField fileField;
      static JButton select;

  static JButton okButton;
  static JButton cancelButton;
  static String projectID;
  final Instant instant = Instant.now();
  static DialogMainFrame dmf;
  private static Session session;
  private static DatabaseManager dbm;
  private static DatabaseInserter dbi;
  private JFileChooser fileChooser;
    private ArrayList<String[]> imported_layout;;    
  final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public DialogImportPlateLayout(
      DialogMainFrame _dmf) {
    dmf = _dmf;
    session = dmf.getSession();
    dbm = dmf.getDatabaseManager();
    dbi = dbm.getDatabaseInserter();
    
    fileChooser = new JFileChooser();

    JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    this.setTitle("Import Plate Layout File");
      select =
        new JButton(
            "Select Layout file...", createImageIcon("/toolbarButtonGraphics/general/Open16.gif"));
    select.setMnemonic(KeyEvent.VK_S);
    select.setActionCommand("select");
    select.setEnabled(true);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 5;
    c.gridwidth = 1;
    c.gridheight = 1;
    select.addActionListener(this);
    pane.add(select, c);

    fileField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 5;
    c.gridwidth = 5;
    c.gridheight = 1;
    pane.add(fileField, c);
    fileField.getDocument().addDocumentListener(this);
    
    okButton = new JButton("OK");
    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.setActionCommand("ok");
    okButton.setEnabled(false);
    okButton.setForeground(Color.GREEN);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 6;
    c.gridwidth = 1;
    c.gridheight = 1;
    okButton.addActionListener(this);
    pane.add(okButton, c);

    cancelButton = new JButton("Cancel");
    cancelButton.setMnemonic(KeyEvent.VK_C);
    cancelButton.setActionCommand("cancel");
    cancelButton.setEnabled(true);
    cancelButton.setForeground(Color.RED);
    c.gridx = 1;
    c.gridy = 6;
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

      public void actionPerformed(ActionEvent e) {

    if (e.getSource() == okButton) {
	/*
      dbi.insertPlateLayout(
          nameField.getText(),
          descriptionField.getText(),
          //((ComboItem)formatList.getSelectedItem()).getKey(),
	  */

	
	  imported_layout =dmf.getUtilities().loadDataFile(fileField.getText());
    int lines_data = imported_layout.size() -1; //for header
	if(lines_data!= 96 && lines_data!=384 ){
	    	JOptionPane.showMessageDialog(dmf,
			      "Layout import file must contain 96 or 384 rows of data.",
			      "File format Error",
			      JOptionPane.ERROR_MESSAGE);
	
	}
	new DialogImportLayoutViewer(dmf, imported_layout);
      dispose();
    }
        if (e.getSource() == select) {
      int returnVal = fileChooser.showOpenDialog(this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        java.io.File file = fileChooser.getSelectedFile();
        // This is where a real application would open the file.
        fileField.setText(file.toString());
      } else {
        LOGGER.info("Open command cancelled by user.\n");
      }
    }

  }

    /** Returns an ImageIcon, or null if the path was invalid. */
  protected static ImageIcon createImageIcon(String path) {
    java.net.URL imgURL = DialogAddPlateSetData.class.getResource(path);
    if (imgURL != null) {
      return new ImageIcon(imgURL);
    } else {
      System.err.println("Couldn't find file: " + path);
      return null;
    }
  }

      public void insertUpdate(DocumentEvent e) {
	  if(fileField.getText().length() >0){
	      okButton.setEnabled(true);
	  }else{
	      okButton.setEnabled(false);
	  }
    }
    public void removeUpdate(DocumentEvent e) {
	  if(fileField.getText().length() >0){
	      okButton.setEnabled(true);
	  }else{
	      okButton.setEnabled(false);
	  }

    }
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }

}
