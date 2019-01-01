package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.swing.table.*;

public class PlateSetPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private JTable table;
  private JScrollPane scrollPane;
  private DialogMainFrame parent;
  private JPanel textPanel;
  private String project_sys_name;

  public PlateSetPanel(DialogMainFrame _parent, JTable _table, String _project_sys_name) {
    this.setLayout(new BorderLayout());
    parent = _parent;
    table = _table;
    project_sys_name = _project_sys_name;
    this.add(new MenuBarForPlateSet(parent, table), BorderLayout.NORTH);

    textPanel = new JPanel();
    textPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    JLabel label = new JLabel("Project:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.insets = new Insets(5, 5, 2, 2);
    textPanel.add(label, c);

    label = new JLabel("Desciption:", SwingConstants.RIGHT);
    c.gridy = 1;
    textPanel.add(label, c);

    JLabel projectLabel = new JLabel(project_sys_name, SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 3;
    c.ipadx = 40;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;
    textPanel.add(projectLabel, c);

    JLabel descriptionLabel =
        new JLabel(
            parent.getDatabaseManager().getDescriptionForProject(project_sys_name),
            SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 1;
    textPanel.add(descriptionLabel, c);

    this.add(textPanel, BorderLayout.CENTER);

    scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true);
    this.add(scrollPane, BorderLayout.SOUTH);
  }

  public JTable getTable() {
    return table;
  }

  public void updatePanel(String _project_sys_name) {
    String project_sys_name = _project_sys_name;
    JTable table = parent.getDatabaseManager().getPlateSetTableData(project_sys_name);
    TableModel model = table.getModel();
    this.table.setModel(model);
  }
}
