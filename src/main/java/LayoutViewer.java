package pm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class LayoutViewer extends JDialog implements java.awt.event.ActionListener {
  static JButton button;
  static JLabel label;
  static JComboBox<Integer> formatList;
  static JComboBox<String> layoutList;
  static JButton okButton;
  static JButton cancelButton;
  final DialogMainFrame dmf;
  final Session session;
    private String owner;
  private JTable table;
  private JScrollPane scrollPane;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // final EntityManager em;
  private static final long serialVersionUID = 1L;
  private DefaultTableModel tableModel;
    private String [] layoutNames;
    
    private DefaultComboBoxModel<String> layout_names_list_model;


    
  public LayoutViewer(DialogMainFrame _dmf) {
    this.dmf = _dmf;
    this.session = dmf.getSession();
    owner = session.getUserName();
    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
    // this.em = em;
    layoutNames = dmf.getDatabaseManager().getDatabaseRetriever().getPlateLayoutNames(96);
    layout_names_list_model = new DefaultComboBoxModel<String>( layoutNames );
    
    JPanel parentPane = new JPanel(new BorderLayout());
    parentPane.setBorder(BorderFactory.createRaisedBevelBorder());

        JPanel pane1 = new JPanel(new GridBagLayout());
    pane1.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    this.setTitle("Plate Layout Viewer");
    // c.gridwidth = 2;

    label = new JLabel("Format:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.insets = new Insets(5, 5, 2, 2);
    pane1.add(label, c);
 
        Integer[] formats = {96, 384, 1536};

    formatList = new JComboBox<Integer>(formats);
    formatList.setSelectedIndex(0);
    c.gridx = 1;
    c.gridy = 0;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_START;
    formatList.addActionListener(this);
    pane1.add(formatList, c);
 

    label = new JLabel("Layout:");
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    pane1.add(label, c);

layoutList = new JComboBox<String>();
//formatList.setSelectedIndex(0);
    c.gridx = 3;
    c.gridy = 0;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_START;
    layoutList.setModel(layout_names_list_model );
 layoutList.addActionListener(this);
   
    pane1.add(layoutList, c);

    
        JPanel pane2 = new JPanel(new BorderLayout());
    pane2.setBorder(BorderFactory.createRaisedBevelBorder());

    this.refreshTable(1);    	

    scrollPane = new JScrollPane(table);
    JTable rowTable = new RowNumberTable(table);
scrollPane.setRowHeaderView(rowTable);
scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
    rowTable.getTableHeader());
    pane2.add(scrollPane, BorderLayout.CENTER);
    table.setFillsViewportHeight(true);



    this.getContentPane().add(parentPane, BorderLayout.CENTER);
    parentPane.add(pane1, BorderLayout.NORTH);
    parentPane.add(pane2, BorderLayout.CENTER);
    
    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

    private void addToDB() {}

    public void actionPerformed(ActionEvent e) {

    if (e.getSource() == formatList) {
       layoutNames = dmf.getDatabaseManager().getDatabaseRetriever().getPlateLayoutNames((int)formatList.getSelectedItem());
       layout_names_list_model = new DefaultComboBoxModel<String>( layoutNames );
       layoutList.setModel(layout_names_list_model );
       layoutList.setSelectedIndex(-1);
 
	
    }

    if (e.getSource() == layoutList) {
	String selected = (String)layoutList.getSelectedItem();
	
		int plate_layout_id  = dmf.getDatabaseManager().getDatabaseRetriever().getIDforLayoutName(selected);
		this.refreshTable(plate_layout_id); 
   
    }
  }
    public void refreshTable(int _plate_layout_id){
		CustomTable  table2 = dmf.getDatabaseManager().getDatabaseRetriever().getPlateLayout(_plate_layout_id);
		Object[][] gridData =  dmf.getUtilities().getPlateLayoutGrid(table2);
		LOGGER.info("griddata: " + gridData);
		Object[] columnNames = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
		LOGGER.info("columnnames: " + columnNames);
		DefaultTableModel tableModel = new DefaultTableModel(gridData, columnNames);
		
		LOGGER.info("tableModel: " + tableModel.getValueAt(6,11));
	table = new JTable(tableModel);
	javax.swing.table.JTableHeader header = table.getTableHeader();
      header.setBackground(java.awt.Color.DARK_GRAY);
      header.setForeground(java.awt.Color.WHITE);

		
	
		//table = dmf.getDatabaseManager().getDatabaseRetriever().getPlateLayout(1);

    }

}
