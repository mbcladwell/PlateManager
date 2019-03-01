package pm;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class DialogImportPlateLayout extends JDialog {
  static JButton button;
  static JLabel label;
  static JLabel Description;
  static JTextField nameField;
  static JTextField ownerField;
  static JTextField descriptionField;
    static JComboBox<ComboItem> formatList;
  static JButton okButton;
  static JButton cancelButton;
  static String projectID;
  final Instant instant = Instant.now();
  static DialogMainFrame dmf;
  private static Session session;
  private static DatabaseManager dbm;
  final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public DialogImportPlateLayout(
      DialogMainFrame _dmf) {
    dmf = _dmf;
    session = dmf.getSession();
    dbm = dmf.getDatabaseManager();
    

    JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    // Image img = new
    // ImageIcon(DialogAddProject.class.getResource("../resources/mwplate.png")).getImage();
    // this.setIconImage(img);
    this.setTitle("Import Plate Layout File");
    // c.gridwidth = 2;

    label = new JLabel("Date:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(5, 5, 2, 2);
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);


    label = new JLabel("Layout Name:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 2;
    pane.add(label, c);

    label = new JLabel("Description:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 3;
    pane.add(label, c);

        label = new JLabel("Format:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 4;
    pane.add(label, c);

    label = new JLabel(df.format(Date.from(instant)));
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(label, c);

    nameField = new JTextField(30);
    //nameField.setText(_name);
    c.gridwidth = 2;
    c.gridx = 1;
    c.gridy = 2;
    pane.add(nameField, c);

    descriptionField = new JTextField(30);
    //descriptionField.setText(_description);
    c.gridx = 1;
    c.gridy = 3;
    c.gridheight = 1;
    pane.add(descriptionField, c);


    ComboItem[] formats = dmf.getDatabaseManager().getDatabaseRetriever().getPlateFormats();

    formatList = new JComboBox<ComboItem>(formats);
    formatList.setSelectedIndex(0);
    c.gridx = 1;
    c.gridy = 4;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(formatList, c);

    
    okButton = new JButton("Select File");
    okButton.setMnemonic(KeyEvent.VK_S);
    okButton.setActionCommand("select");
    okButton.setEnabled(true);
    okButton.setForeground(Color.GREEN);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 5;
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
    c.gridy = 5;
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

  private void addToDB() {}
}
