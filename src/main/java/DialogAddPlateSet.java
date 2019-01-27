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

public class DialogAddPlateSet extends JDialog {
  static JButton button;
  static JLabel label;
  static JLabel Description;
  static JTextField nameField;
  static JLabel ownerLabel;
  static String owner;
  static JTextField descriptionField;
  static JTextField numberField;
  static JComboBox<Integer> formatList;
  static JComboBox<String> typeList;
  static JButton okButton;
  static JButton cancelButton;
  final Instant instant = Instant.now();
  final DialogMainFrame parent;
  final Session session;
  final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // final EntityManager em;

  public DialogAddPlateSet(DialogMainFrame _parent) {
    this.parent = _parent;
    this.session = parent.getSession();
    owner = session.getUserName();
    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
    // this.em = em;
    JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    // Image img = new
    // ImageIcon(DialogAddProject.class.getResource("../resources/mwplate.png")).getImage();
    // this.setIconImage(img);
    this.setTitle("Add a Plate Set");
    // c.gridwidth = 2;

    label = new JLabel("Date:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;

    c.insets = new Insets(5, 5, 2, 2);
    pane.add(label, c);

    label = new JLabel(df.format(Date.from(instant)));
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(label, c);

    label = new JLabel("Owner:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 1;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    label = new JLabel("Plate Set Name:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 2;
    pane.add(label, c);

    label = new JLabel("Description:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 3;
    c.gridheight = 1;
    pane.add(label, c);

    label = new JLabel("Number of plates:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 4;
    c.gridheight = 1;
    pane.add(label, c);

    ownerLabel = new JLabel(owner);
    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 5;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(ownerLabel, c);

    nameField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 2;
    c.gridheight = 1;
    pane.add(nameField, c);

    descriptionField = new JTextField(30);
    c.gridx = 1;
    c.gridy = 3;
    c.gridheight = 1;
    pane.add(descriptionField, c);

    numberField = new JTextField(4);
    c.gridx = 1;
    c.gridy = 4;
    c.gridheight = 1;
    c.gridwidth = 1;
    pane.add(numberField, c);

    label = new JLabel("Format:", SwingConstants.RIGHT);
    c.gridx = 2;
    c.gridy = 4;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    Integer[] formats = {96, 384, 1536};

    formatList = new JComboBox<Integer>(formats);
    formatList.setSelectedIndex(0);
    c.gridx = 3;
    c.gridy = 4;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(formatList, c);
    // formatList.addActionListener(this);

    label = new JLabel("Type:", SwingConstants.RIGHT);
    c.gridx = 4;
    c.gridy = 4;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_END;
    pane.add(label, c);

    String[] plateTypes = parent.getDatabaseManager().getDatabaseRetriever().getPlateTypes();

    typeList = new JComboBox<String>(plateTypes);
    formatList.setSelectedIndex(0);
    c.gridx = 5;
    c.gridy = 4;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_START;
    pane.add(typeList, c);

    okButton = new JButton("OK");
    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.setActionCommand("ok");
    okButton.setEnabled(true);
    okButton.setForeground(Color.GREEN);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 5;
    c.gridwidth = 2;
    c.gridheight = 1;
    okButton.addActionListener(
        (new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            parent
                .getDatabaseManager()
                .insertPlateSet(
                    nameField.getText(),
                    descriptionField.getText(),
                    numberField.getText(),
                    formatList.getSelectedItem().toString(),
                    typeList.getSelectedItem().toString());
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
    c.gridwidth = 1;
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

  private void addToDB() {}
}
