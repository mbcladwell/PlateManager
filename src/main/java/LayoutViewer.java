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

public class LayoutViewer extends JDialog {
  static JButton button;
  static JLabel label;
  static JComboBox<Integer> formatList;
  static JComboBox<String> typeList;
  static JButton okButton;
  static JButton cancelButton;
  final DialogMainFrame parent;
  final Session session;
  private CustomTable table;
  private JScrollPane scrollPane;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // final EntityManager em;

  public LayoutViewer(DialogMainFrame _parent) {
    this.parent = _parent;
    this.session = parent.getSession();
    owner = session.getUserName();
    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
    // this.em = em;
    JPanel parentPane = new JPanel(new BorderLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

        JPanel pane1 = new JPanel(new GridBagLayout());
    pane1.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    this.setTitle("Plate Layout Viewer");
    // c.gridwidth = 2;

    label = new JLabel("Format:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.insets = new Insets(5, 5, 2, 2);
    pane1.add(label, c);

        Integer[] formats = {96, 384, 1536};

    formatList = new JComboBox<Integer>(formats);
    formatList.setSelectedIndex(0);
    c.gridx = 3;
    c.gridy = 4;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_START;
    pane1.add(formatList, c);



    label = new JLabel("Layout");
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    pane1.add(label, c);

        JPanel pane2 = new JPanel(new GridBagLayout());
    pane2.setBorder(BorderFactory.createRaisedBevelBorder());

    table = new CustomTable();
        scrollPane = new JScrollPane(table);
    pane2.add(scrollPane, BorderLayout.CENTER);
    table.setFillsViewportHeight(true);


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

    this.getContentPane().add(parentPane, BorderLayout.CENTER);
    parentPane.add(pane1, Borderlayout.NORTH);
    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

  private void addToDB() {}
}
