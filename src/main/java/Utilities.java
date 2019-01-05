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
import javax.swing.table.TableModel;

public class Utilities {

  private DialogMainFrame dmf;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private File file;
  private static final String newline = "\n";
  private JFileChooser fc;

  public Utilities(DialogMainFrame _dmf) {
    dmf = _dmf;
  }

  /** Expot selected rows only to TSV (TAB; extension .XLS) or CSV (COMMA; extension .CSV) */
  public void toExcel(JTable table) {
    fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnVal = fc.showOpenDialog(dmf);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = file.createNewFile(fc.getSelectedFile());

      // This is where a real application would open the file.
      LOGGER.info("Opening: " + file.getName() + "." + newline);
    } else {
      LOGGER.info("Open command cancelled by user." + newline);
    }

    try {
      TableModel model = table.getModel();
      FileWriter excel = new FileWriter(file);

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
  }
}
