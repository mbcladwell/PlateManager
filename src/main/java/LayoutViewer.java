package pm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class LayoutViewer extends JDialog {
  static JButton button;
  static JLabel label;
  static JComboBox<Integer> formatList;
  static JComboBox<String> typeList;
  static JButton okButton;
  static JButton cancelButton;
  final DialogMainFrame dmf;
  final Session session;
    private String owner;
  private CustomTable table;
  private JScrollPane scrollPane;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // final EntityManager em;
  private static final long serialVersionUID = 1L;
  private DefaultTableModel tableModel;

    
  public LayoutViewer(DialogMainFrame _dmf) {
    this.dmf = _dmf;
    this.session = dmf.getSession();
    owner = session.getUserName();
    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
    // this.em = em;
    JPanel parentPane = new JPanel(new BorderLayout());
    parentPane.setBorder(BorderFactory.createRaisedBevelBorder());

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

    table = new CustomTable(dmf, tableModel);
        scrollPane = new JScrollPane(table);
    pane2.add(scrollPane, BorderLayout.CENTER);
    table.setFillsViewportHeight(true);



    this.getContentPane().add(parentPane, BorderLayout.CENTER);
    parentPane.add(pane1, BorderLayout.NORTH);
    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

  private void addToDB() {}
}
