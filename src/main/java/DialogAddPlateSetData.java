package pm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

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

public class DialogAddPlateSetData extends JDialog
    implements java.awt.event.ActionListener, javax.swing.event.DocumentListener {
  static JButton button;
  static JLabel label;
  static JComboBox<String> assayTypes;
  static JComboBox<String> plateLayouts;
  static JTextField fileField;
  static JTextField nameField;
  static JTextField descrField;
  static JButton okButton;

  static JButton select;
  static JButton cancelButton;
  private static final long serialVersionUID = 1L;
  private DialogMainFrame dmf;
  private DatabaseManager dbm;
  private DatabaseRetriever dbr;
  private DatabaseInserter dbi;

  private String plate_set_sys_name;
  private String plate_set_id_string;
  private String plate_set_description;
  private String format;
  private String plate_num;
  private JFileChooser fileChooser;

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public DialogAddPlateSetData(
      DialogMainFrame _dmf,
      String _plate_set_sys_name,
      String _plate_set_id_string,
      String _format,
      String _plate_num) {

    plate_set_sys_name = _plate_set_sys_name;
    plate_set_id_string = _plate_set_id_string;
    format = _format;
    plate_num = _plate_num;
    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
    this.dmf = _dmf;
    this.dbm = dmf.getDatabaseManager();
    this.dbr = dbm.getDatabaseRetriever();
    this.dbi = dbm.getDatabaseInserter();

    int plate_set_id = Integer.valueOf(plate_set_id_string);
    plate_set_description = dbr.getDescriptionForPlateSet(plate_set_sys_name);

    fileChooser = new JFileChooser();

    JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    // Image img = new
    // ImageIcon(DialogAddProject.class.getResource("../resources/mwplate.png")).getImage();
    // this.setIconImage(img);
    this.setTitle("Associate assay run data with plate set");
    // c.gridwidth = 2;

    label = new JLabel("Plate set:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;

    c.anchor = GridBagConstraints.LINE_END;
    c.insets = new Insets(5, 5, 2, 2);

    pane.add(label, c);

    label = new JLabel(plate_set_sys_name, SwingConstants.LEFT);
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

    label = new JLabel(format, SwingConstants.LEFT);
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

    label = new JLabel(plate_num, SwingConstants.LEFT);
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

    label = new JLabel("Assay run name:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 2;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    nameField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 2;
    c.gridwidth = 5;
    c.gridheight = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(nameField, c);
    nameField.getDocument().addDocumentListener(this);

    label = new JLabel("Assay run description:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 3;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    descrField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 3;
    c.gridwidth = 5;
    c.gridheight = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    // c.anchor = GridBagConstraints.LINE_START;
    pane.add(descrField, c);

    label = new JLabel("Assay type:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 5;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    assayTypes = new JComboBox<String>(dbr.getAssayTypes());
    c.gridx = 1;
    c.gridy = 5;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(assayTypes, c);

    label = new JLabel("Plate layout:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 5;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    // LOGGER.info("format: " + );
    plateLayouts = new JComboBox<String>(dbr.getPlateLayoutNames(dbr.getPlateFormatID(format)));
    c.gridx = 3;
    c.gridy = 5;
    c.gridwidth = 3;

    c.anchor = GridBagConstraints.LINE_START;
    pane.add(plateLayouts, c);

    select =
        new JButton(
            "Select data file...", createImageIcon("/toolbarButtonGraphics/general/Open16.gif"));
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

    okButton = new JButton("OK");
    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.setActionCommand("ok");
    okButton.setEnabled(true);
    okButton.setForeground(Color.GREEN);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 7;
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
    c.gridy = 7;
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

    if (e.getSource() == okButton) {
      dbi.associateDataWithPlateSet(
          nameField.getText(),
          descrField.getText(),
          plate_set_sys_name,
          format,
          (String) assayTypes.getSelectedItem(),
          (String) plateLayouts.getSelectedItem(),
          dmf.getUtilities().loadDataFile(fileField.getText()));
      dispose();
    }

    if (e.getSource() == select) {
      int returnVal = fileChooser.showOpenDialog(DialogAddPlateSetData.this);

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
