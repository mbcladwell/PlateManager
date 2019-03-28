package pm;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class AssayRunViewer extends JDialog implements java.awt.event.ActionListener {
  static JButton button;
  static JLabel label;
  static JComboBox<Integer> formatList;
  static JComboBox<String> displayList;
  static JComboBox<ComboItem> layoutList;
  static JButton okButton;
  static JButton cancelButton;
  final DialogMainFrame dmf;
  final Session session;
    private String owner;
  private JTable table;
  private JScrollPane scrollPane;
    private  JPanel parentPane;
    private  JPanel pane1;
    private  JPanel pane2;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  // final EntityManager em;
  private static final long serialVersionUID = 1L;
  private MyModel tableModel;
    private ComboItem [] layoutNames;
    private Object[][] gridData;
    
    private DefaultComboBoxModel<ComboItem> layout_names_list_model;
     private static final long serialVersionUID = 1L;


    
  public AssayRunViewer(DialogMainFrame _dmf) {
    this.setTitle("Assay Run Viewer");
    this.dmf = _dmf;
    this.session = dmf.getSession();
    owner = session.getUserName();

    parentPane = new JPanel(new BorderLayout());
    
    pane1 = new JPanel(new BorderLayout());
    pane1.setBorder(BorderFactory.createRaisedBevelBorder());
    javax.swing.border.TitledBorder pane1Border = BorderFactory.createTitledBorder("Assay Runs:");
    pane1Border.setTitlePosition(javax.swing.border.TitledBorder.TOP);
    pane1.setBorder(pane1Border);

    pane2  = new JPanel(new BorderLayout());
    pane2.setBorder(BorderFactory.createRaisedBevelBorder());
    javax.swing.border.TitledBorder pane2Border = BorderFactory.createTitledBorder("Hit Lists:");
    pane2Border.setTitlePosition(javax.swing.border.TitledBorder.TOP);
    pane2.setBorder(pane1Border);

    this.getContentPane().add(parentPane, BorderLayout.CENTER);
    parentPane.add(pane1, BorderLayout.NORTH);
    parentPane.add(pane2, BorderLayout.SOUTH);

    

    GridBagConstraints c = new GridBagConstraints();
    

  
   
   
    
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
       layout_names_list_model = new DefaultComboBoxModel<ComboItem>( layoutNames );
       layoutList.setModel(layout_names_list_model );
       layoutList.setSelectedIndex(-1);
	
    }

    if (e.getSource() == layoutList) {
	if(layoutList.getSelectedIndex() > -1){
	    int plate_layout_id  = ((ComboItem)layoutList.getSelectedItem()).getKey();
	    //  String selected = (String)layoutList.getSelectedItem();
	    // int plate_layout_id  = dmf.getDatabaseManager().getDatabaseRetriever().getIDforLayoutName(selected);
	    this.refreshTable(plate_layout_id); 
	}
    }
  }

  
}
