package pm;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import javax.swing.*;
import javax.swing.JComponent.*;
import java.util.logging.*;

public class DialogLogin extends JDialog {
  static JButton button;
  static JLabel label;
  static JLabel password;
  static JTextField userIDField;
  static JTextField passwordField;
  static JButton okButton;
  static JButton cancelButton;
  final Instant instant = Instant.now();
  final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Session s;
  
    public DialogLogin  ( Session _s, String _string, ModalityType _m) {
	s = _s;
	
    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
      JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    // Image img = new
    // ImageIcon(DialogAddProject.class.getResource("../resources/mwplate.png")).getImage();
    // this.setIconImage(img);
    this.setTitle("Login to LIMS*Nucleus");
    // c.gridwidth = 2;

    label = new JLabel("Date:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(5, 5, 2, 2);
    pane.add(label, c);

    label = new JLabel(df.format(Date.from(instant)));
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    pane.add(label, c);

    label = new JLabel("Name:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 1;
    pane.add(label, c);

    label = new JLabel("Password:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 2;
    pane.add(label, c);

   
    userIDField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 2;
    c.gridheight = 1;
    pane.add(userIDField, c);

    passwordField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 2;
    pane.add(passwordField, c);

  

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
	      s.setUserName(userIDField.getText());
	      s.setPassword(passwordField.getText());
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
