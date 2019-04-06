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
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;

public class DialogImportPlateSetAccessionIDs extends JDialog
    implements java.awt.event.ActionListener, javax.swing.event.DocumentListener {
  static JButton button;
  static JLabel label;
  static JLabel nLabel;
  static JComboBox<ComboItem> assayTypes;
  static JComboBox<ComboItem> plateLayouts;
  static JComboBox<ComboItem> algorithmList;
    
  static JTextField fileField;
  static JTextField nameField;
  static JTextField descrField;
  static JTextField layoutField;
  static JTextField nField;
    
  static JButton okButton;

  static JButton select;
  static JButton cancelButton;
  private static final long serialVersionUID = 1L;
  private DialogMainFrame dmf;
  private DatabaseManager dbm;
  private DatabaseRetriever dbr;
  private DatabaseInserter dbi;

  private ComboItem plate_set;
   
  private String plate_set_description;
    private ComboItem format;
    private int plate_num;
    private ComboItem plate_layout;
  private JFileChooser fileChooser;
    private JCheckBox checkBox;
    
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public DialogImportPlateSetAccessionIDs(
      DialogMainFrame _dmf,
      String _plate_set_sys_name,
      int _plate_set_id,
      int _format_id,
      int _plate_num) {

    plate_set = new ComboItem(_plate_set_id, _plate_set_sys_name);
    format = new ComboItem(_format_id, String.valueOf(_format_id));
    plate_num = _plate_num;
   
    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
    this.dmf = _dmf;
    this.dbm = dmf.getDatabaseManager();
    this.dbr = dbm.getDatabaseRetriever();
    this.dbi = dbm.getDatabaseInserter();

    //LOGGER.info("plate_set_id: " + plate_set_id);
    plate_set_description = dbr.getDescriptionForPlateSet(_plate_set_sys_name);
    
    
    fileChooser = new JFileChooser();

    JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    // Image img = new
    // ImageIcon(DialogAddProject.class.getResource("../resources/mwplate.png")).getImage();
    // this.setIconImage(img);
    this.setTitle("Import Accession IDs for Plate Set " + plate_set.toString());
    // c.gridwidth = 2;

    label = new JLabel("Plate set:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;

    c.anchor = GridBagConstraints.LINE_END;
    c.insets = new Insets(5, 5, 2, 2);

    pane.add(label, c);

    label = new JLabel(plate_set.toString(), SwingConstants.LEFT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(label, c);

    label = new JLabel("Format:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    label = new JLabel(format.toString(), SwingConstants.LEFT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 3;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(label, c);

    label = new JLabel("# plates:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 4;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    label = new JLabel(String.valueOf(plate_num), SwingConstants.LEFT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 5;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(label, c);

    label = new JLabel("Plate set description:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 1;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    label = new JLabel(plate_set_description, SwingConstants.LEFT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 1;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(label, c);

    label = new JLabel("Expecting:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 2;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    
    select =
        new JButton(
            "Select IDs file...", createImageIcon("/toolbarButtonGraphics/general/Open16.gif"));
    select.setMnemonic(KeyEvent.VK_S);
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

    
    okButton = new JButton("OK");
    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.setActionCommand("ok");
    okButton.setEnabled(true);
    okButton.setForeground(Color.GREEN);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
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
    java.net.URL imgURL = DialogAddPlateSetData.class.getResource(path);
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
	if(((ComboItem)algorithmList.getSelectedItem()).getKey()==1){  //If Top N is the algorithm
	    try{
		top_n_number = Integer.parseInt(nField.getText());
	    }catch(NumberFormatException nfe){
		JOptionPane.showMessageDialog(dmf,
			      "Invalid number of hits.",
			      "Number Format Exception",
			      JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    
	}
	
      dbi.associateDataWithPlateSet(
          nameField.getText(),
          descrField.getText(),
          plate_set.toString(),
          format.getKey(),
          ((ComboItem)assayTypes.getSelectedItem()).getKey(),
          plate_layout.getKey(),
          dmf.getUtilities().loadDataFile(fileField.getText()),
	  checkBox.isSelected(),
	  ((ComboItem)algorithmList.getSelectedItem()).getKey(),
	  top_n_number);
      dispose();
    }

    if (e.getSource() == select) {
      int returnVal = fileChooser.showOpenDialog(DialogImportPlateSetAccessionIDs.this);

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

    if (nameField.getText().length() > 0 & fileField.getText().length() > 0) {
      okButton.setEnabled(true);
    } else {
      okButton.setEnabled(false);
    }
  }

  public void removeUpdate(DocumentEvent e) {
    if (nameField.getText().length() > 0 & fileField.getText().length() > 0) {
      okButton.setEnabled(true);
    } else {
      okButton.setEnabled(false);
    }
  }

  public void changedUpdate(DocumentEvent e) {
    // Plain text components don't fire these events.
  }
}
