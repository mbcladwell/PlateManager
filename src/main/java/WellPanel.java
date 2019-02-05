package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;

public class WellPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private CustomTable table;
  private JScrollPane scrollPane;
  private DialogMainFrame dmf;
  private JPanel textPanel;
  private String plateset_sys_name;

  public WellPanel(DialogMainFrame _dmf, CustomTable _table) {
    this.setLayout(new BorderLayout());
    dmf = _dmf;
    table = _table;

    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BorderLayout());
    headerPanel.add(new MenuBarForWell(dmf, table), BorderLayout.NORTH);

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

    label = new JLabel("Project: ", SwingConstants.RIGHT);
    c.gridx = 2;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    textPanel.add(label, c);

    JLabel projectLabel = new JLabel(dmf.getSession().getProjectSysName(), SwingConstants.LEFT);
    c.gridx = 3;
    c.gridy = 0;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    textPanel.add(projectLabel, c);

    label = new JLabel("Description:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 1;
    c.anchor = GridBagConstraints.LINE_END;
    textPanel.add(label, c);

    plateset_sys_name =
        dmf.getDatabaseManager()
            .getDatabaseRetriever()
            .getPlateSetSysNameForPlateSysName((String) table.getValueAt(1, 0));
    JLabel platesetLabel = new JLabel(plateset_sys_name, SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 1;
    c.weightx = 0.9;

    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;
    textPanel.add(platesetLabel, c);

    JLabel descriptionLabel =
        new JLabel(
            dmf.getDatabaseManager()
                .getDatabaseRetriever()
                .getDescriptionForPlateSet(plateset_sys_name),
            SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 1;
    textPanel.add(descriptionLabel, c);

    headerPanel.add(textPanel, BorderLayout.CENTER);
    this.add(headerPanel, BorderLayout.NORTH);

    scrollPane = new JScrollPane(table);
    this.add(scrollPane, BorderLayout.CENTER);
    table.setFillsViewportHeight(true);
    FilterPanel fp = new FilterPanel(dmf, table);
    this.add(fp, BorderLayout.SOUTH);
  }

  public CustomTable getTable() {
    return table;
  }
}