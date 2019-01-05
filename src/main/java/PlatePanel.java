package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.swing.table.*;

public class PlatePanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private JTable table;
  private JScrollPane scrollPane;
  private DialogMainFrame parent;
  private JPanel textPanel;
  private String plateset_sys_name;

  public PlatePanel(DialogMainFrame _parent, JTable _table, String _plateset_sys_name) {
    this.setLayout(new BorderLayout());
    parent = _parent;
    table = _table;
    plateset_sys_name = _plateset_sys_name;
    this.add(new MenuBarForPlate(parent, table), BorderLayout.NORTH);

    textPanel = new JPanel();
    textPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    JLabel label = new JLabel("Plate Set:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.weightx = 0.1;
    c.insets = new Insets(5, 5, 2, 2);
    textPanel.add(label, c);

    label = new JLabel("Desciption:", SwingConstants.RIGHT);
    c.gridy = 1;
    textPanel.add(label, c);

    JLabel platesetLabel = new JLabel(plateset_sys_name, SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 3;
    c.weightx = 0.9;

    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;
    textPanel.add(platesetLabel, c);

    JLabel descriptionLabel =
        new JLabel(
            parent
                .getDatabaseManager()
                .getDatabaseRetriever()
                .getDescriptionForPlateSet(plateset_sys_name),
            SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 1;
    textPanel.add(descriptionLabel, c);

    this.add(textPanel, BorderLayout.CENTER);

    scrollPane = new JScrollPane(table);
    this.add(scrollPane, BorderLayout.SOUTH);
    table.setFillsViewportHeight(true);
  }

  public JTable getTable() {
    return table;
  }

  public void updatePanel(String _plate_set_sys_name) {
    String plate_set_sys_name = _plate_set_sys_name;
    JTable table = parent.getDatabaseManager().getPlateTableData(plate_set_sys_name);
    TableModel model = table.getModel();
    this.table.setModel(model);
  }
}
