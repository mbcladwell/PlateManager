package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.swing.ListSelectionModel;
import javax.swing.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import javax.swing.table.TableModel;

public class ProjectPanel extends JPanel implements TableModelListener {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private JTable table;
  private JScrollPane scrollPane;
  private DialogMainFrame parent;
  private JPanel textPanel;
  private ListSelectionModel listSelectionModel;

  public ProjectPanel(DialogMainFrame _parent, JTable _table) {
    this.setLayout(new BorderLayout());
    parent = _parent;
    table = _table;
    listSelectionModel = table.getSelectionModel();
    listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
    table.setSelectionModel(listSelectionModel);

    scrollPane = new JScrollPane(table);
    this.add(scrollPane, BorderLayout.SOUTH);
    table.setFillsViewportHeight(true);
    this.add(new MenuBarForProject(parent, table), BorderLayout.NORTH);

    textPanel = new JPanel();
    textPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    JLabel label = new JLabel("User:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0.1;
    c.anchor = GridBagConstraints.LINE_END;
    c.insets = new Insets(5, 5, 2, 2);
    textPanel.add(label, c);

    label = new JLabel("Privileges:", SwingConstants.RIGHT);
    c.gridy = 1;
    textPanel.add(label, c);

    JLabel userLabel = new JLabel(parent.getSession().getUserName(), SwingConstants.LEFT);
    c.gridx = 1;
    c.gridy = 0;
    // c.gridwidth = 3;
    c.weightx = 0.9;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;
    textPanel.add(userLabel, c);

    JLabel privilegesLabel = new JLabel(parent.getSession().getUserType(), SwingConstants.LEFT);
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

  public void tableChanged(TableModelEvent e) {
    int row = e.getFirstRow();
    int column = e.getColumn();
    TableModel model = (TableModel) e.getSource();
    String columnName = model.getColumnName(column);
    Object data = model.getValueAt(row, column);

    model.setValueAt(false, row, 0);

    // Do something with the data...
  }

  class SharedListSelectionHandler implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      ListSelectionModel lsm = (ListSelectionModel) e.getSource();

      int firstIndex = e.getFirstIndex();
      int lastIndex = e.getLastIndex();
      boolean isAdjusting = e.getValueIsAdjusting();
      /*  LOGGER.info(
      //   "Event for indexes "
              + firstIndex
              + " - "
              + lastIndex
              + "; isAdjusting is "
              + isAdjusting
              + "; selected indexes:");
      */
      if (lsm.isSelectionEmpty()) {
        LOGGER.info(" <none>");
      } else {
        if (!isAdjusting) {
          // Find out which indexes are selected.
          int minIndex = lsm.getMinSelectionIndex();
          int maxIndex = lsm.getMaxSelectionIndex();
          for (int i = minIndex; i <= maxIndex; i++) {
            if (lsm.isSelectedIndex(i)) {
              LOGGER.info(" " + i);
              javax.swing.table.TableModel tm = ProjectPanel.this.getTable().getModel();
              if ((boolean) tm.getValueAt(i, 0)) tm.setValueAt(false, i, 0);
              else tm.setValueAt(true, i, 0);
              ProjectPanel.this.table.setModel(tm);
            }
          }
        }
      }
    }
  }
}
