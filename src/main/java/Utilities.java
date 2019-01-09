package pm;

import java.awt.*;
import java.awt.Dialog.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import javax.swing.JFileChooser.*;
import javax.swing.table.*;

public class Utilities {

  private DialogMainFrame dmf;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private File file;
  private static final String newline = "\n";
  private JFileChooser fc;

  public Utilities(DialogMainFrame _dmf) {
    dmf = _dmf;
  }

  public Object[][] getTableDataWithHeader(JTable _table) {
    JTable table = _table;

    CustomTableModel ctm = (CustomTableModel) table.getModel();
    int nRow = ctm.getRowCount() + 1, nCol = ctm.getColumnCount();
    Object[][] tableData = new Object[nRow][nCol];

    for (int i = 0; i < nCol; i++) {
      tableData[0][i] = table.getColumnName(i);
    }

    for (int i = 1; i < nRow; i++)
      for (int j = 0; j < nCol; j++) tableData[i][j] = ctm.getValueAt(i - 1, j);
    return tableData;
  }
  /**
   * Expot selected rows only to TSV (TAB; extension .XLS) or CSV (COMMA; extension .CSV) First
   * column Select is ignored.
   */

  /*
  public String[][] exportTableWithHeader(JTable table) {
    // fc = new JWSFileChooserDemo();
    // fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    /*
    int returnVal = fc.showOpenDialog(dmf);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      //      File file = file.createNewFile(fc.getSelectedFile());
      // This is where a real application would open the file.
      LOGGER.info("Opening: " + file.getName() + "." + newline);
    } else {
      LOGGER.info("Open command cancelled by user." + newline);
    }
    */
  /*
         try {
           TableModel model = table.getModel();
           int columnCount = model.getColumnCount();
           int rowCount = model.getRowCount();
           int selectedRowCount = 0;
           for (int i = 0; i < rowCount; i++) {}

           String[][] excel = new FileWriter(file);

           for (int i = 0; i < model.getColumnCount(); i++) {
             excel.write(model.getColumnName(i) + "\t");
           }

           excel.write(newline);

           for (int i = 0; i < model.getRowCount(); i++) {
             for (int j = 0; j < model.getColumnCount(); j++) {
               excel.write(model.getValueAt(i, j).toString() + "\t");
             }
             excel.write("\n");
           }

           excel.close();

         } catch (IOException e) {
           System.out.println(e);
         }

  }  */
}
