package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.swing.table.*;

public class ProjectPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private JTable table;
  private JScrollPane scrollPane;
  private DialogMainFrame parent;
  private JPanel textPanel;

  public ProjectPanel(DialogMainFrame _parent, JTable _table) {
    this.setLayout(new BorderLayout());
    parent = _parent;
    table = _table;
    scrollPane = new JScrollPane(table);
    this.add(scrollPane, BorderLayout.SOUTH);
    table.setFillsViewportHeight(true);
    this.add(new MenuBarForProject(parent, table), BorderLayout.NORTH);

    textPanel = new JPanel();
    textPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    JLabel label = new JLabel("user:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0.1;
    c.anchor = GridBagConstraints.LINE_END;
    c.insets = new Insets(5, 5, 2, 2);
    textPanel.add(label, c);

    label = new JLabel("privileges:", SwingConstants.RIGHT);
    c.gridy = 1;
    textPanel.add(label, c);

    JLabel userLabel = new JLabel(parent.getSession().getPmuserName(), SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 0;
    // c.gridwidth = 3;
    c.weightx = 0.9;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;
    textPanel.add(userLabel, c);

    JLabel privilegesLabel =
        new JLabel(parent.getSession().getPmuserTypeString(), SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 1;
    textPanel.add(privilegesLabel, c);

    this.add(textPanel, BorderLayout.CENTER);
  }

  public JTable getTable() {
    return table;
  }

  public void updatePanel() {
    JTable table = parent.getDatabaseManager().getProjectTableData();
    TableModel model = table.getModel();
    this.table.setModel(model);
  }
}
