package pm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import java.util.logging.*;

public class DialogAddPlateSetData extends JDialog {
  static JButton button;
  static JLabel label;
  static JComboBox assayTypes;
  static JTextField fileField;
  static JButton okButton;
  
  static JButton select;
  static JButton cancelButton;
  private static final long serialVersionUID = 1L;
  private DatabaseManager dbm;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public DialogAddPlateSetData() {

    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
     this.dbm = dbm;
    JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    // Image img = new
    // ImageIcon(DialogAddProject.class.getResource("../resources/mwplate.png")).getImage();
    // this.setIconImage(img);
    this.setTitle("Login to Plate Manager");
    // c.gridwidth = 2;

	 label = new JLabel("Assay name:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 2;
    pane.add(label, c);

	 label = new JLabel("Type:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 3;
    pane.add(label, c);

	assayTypes = new JComboBox();
	c.gridx = 0;
    c.gridy = 3;
    pane.add(label, c);


    label = new JLabel("File:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 4;
    pane.add(label, c);

   
    fileField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 4;
    c.gridwidth = 2;
    c.gridheight = 1;
    //pane.add(userIDField, c);

	select = new JButton("Select...");
    select.setMnemonic(KeyEvent.VK_O);
    select.setActionCommand("select");
    select.setEnabled(true);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 4;
    c.gridwidth = 1;
    c.gridheight = 1;
    select.addActionListener(
        (new ActionListener() {
          public void actionPerformed(ActionEvent e) {

  
            dispose();
          }
        }));

    pane.add(select, c);


    okButton = new JButton("OK");
    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.setActionCommand("ok");
    okButton.setEnabled(true);
    okButton.setForeground(Color.GREEN);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 5;
    c.gridwidth = 1;
    c.gridheight = 1;
    okButton.addActionListener(
        (new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            //DatabaseManager dm = new DatabaseManager();
            //dbm.persistObject(new Project(descriptionField.getText(), ownerField.getText(), nameField.getText()));

            dispose();
          }
        }));

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

  
}
