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

  public PlatePanel(DialogMainFrame _parent, JTable _table) {
    this.setLayout(new BorderLayout());
    parent = _parent;
    table = _table;
    scrollPane = new JScrollPane(table);
    this.add(scrollPane, BorderLayout.CENTER);
    table.setFillsViewportHeight(true);
    this.add(new MenuBarForPlate(parent, table), BorderLayout.NORTH);
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
