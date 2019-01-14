package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class CustomTable extends JTable {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private DialogMainFrame dmf;
  private CustomTableModel customTableModel;
  private ListSelectionModel listSelectionModel;
  private SharedListSelectionHandler sharedListSelectionHandler;
  private Vector<Integer> selectedRows = new Vector<>();

  public CustomTable(DialogMainFrame _dmf, CustomTableModel _ctm) {
    super(_ctm);

    dmf = _dmf;
    customTableModel = _ctm;

    listSelectionModel = this.getSelectionModel();
    sharedListSelectionHandler = new SharedListSelectionHandler();
    listSelectionModel.addListSelectionListener(sharedListSelectionHandler);
    this.setSelectionModel(listSelectionModel);
    this.setRowSelectionAllowed(true);

    /*
    table.getColumnModel().getColumn(0).setMaxWidth(75);
    table.getColumnModel().getColumn(1).setMaxWidth(150);
    table.getColumnModel().getColumn(2).setMaxWidth(100);
    */
    this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
  }

  public CustomTableModel getCustomTableModel() {
    return this.customTableModel;
  }

  public String[][] getSelectedRowsAsStringArray() {
    int colCount = customTableModel.getColumnCount();
    int rowCount = getSelectedRows().length;
    String[][] results = new String[colCount][rowCount];
    for (int i = 0; i <= rowCount; i++) {
      for (int j = 0; j < colCount; j++) {
        results[j][i] = (String) customTableModel.getValueAt(i, j);
      }
    }
    return results;
  }

  class SharedListSelectionHandler implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      // listSelectionModel.removeListSelectionListener(sharedListSelectionHandler);
      /*
      boolean isAdjusting = e.getValueIsAdjusting();
      if (!isAdjusting) {
        selectedRows.clear();
        // Find out which indexes are selected.
        int minIndex = listSelectionModel.getMinSelectionIndex();
        int maxIndex = listSelectionModel.getMaxSelectionIndex();
        for (int i = minIndex; i <= maxIndex; i++) {
          if (listSelectionModel.isSelectedIndex(i)) {
            selectedRows.add(CustomTable.this.convertRowIndexToModel(i));
            LOGGER.info("Selected: " + CustomTable.this.convertRowIndexToModel(i));
          }
        }
      }
      */
      // listSelectionModel.addListSelectionListener(sharedListSelectionHandler);
    }
  }
}

// https://stackoverflow.com/questions/2668547/stackoverflowerror-being-caused-by-a-tablemodellistener

// https://stackoverflow.com/questions/10679425/multiple-row-selection-with-checkbox-in-jtable
