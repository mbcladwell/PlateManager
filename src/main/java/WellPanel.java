package pm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JComponent.*;
import java.util.logging.*;

public class WellPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private JTable table;
  private JScrollPane scrollPane;
  private DialogMainFrame parent;
private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public WellPanel(DialogMainFrame _parent, JTable _table) {
    this.setLayout(new BorderLayout());
    parent = _parent;
    table = _table;
    scrollPane = new JScrollPane(table);
    this.add(scrollPane, BorderLayout.CENTER);
    table.setFillsViewportHeight(true);
    this.add(new MenuBarForWell(parent, table), BorderLayout.NORTH);
    System.out.println("finished well panel");
  }

  public JTable getTable() {
    return table;
  }
}
