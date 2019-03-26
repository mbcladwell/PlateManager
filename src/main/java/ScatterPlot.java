/*
 * ScatterPlot.java
 * http://forums.devshed.com/java-help-9/java-scatterplot-121767.html
 *
 * http://zetcode.com/java/postgresql/
 */
package pm;

    import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class ScatterPlot extends JFrame {
    private JButton genHitsBtn = new JButton("Generate hit list");
    private DatabaseRetriever dbr;
    private DialogMainFrame dmf;
    private CustomTable table;
    private Set<Integer> plate_set = new HashSet<Integer>();
    private Set<Integer> well_set = new HashSet<Integer>();
    private List<Double> norm_list = new LinkedList<Double>();
    private List<Double> bkgrnd_list = new LinkedList<Double>();
        static JComboBox<ComboItem> algorithmList;
    static JComboBox<ComboItem> responseList;
    private JTextField thresholdField;
    private int format;
    private double max_response;
    private double min_response;
    private double mean_bkgrnd;
    private double stdev_bkgrnd;
    private double threshold;
    private double mean_3_sd;
    private double mean_2_sd;
    private double[][] sortedResponse;
    private int margin = 60;
    private int wth;
    private int hgt;	  
    private double originX;
    private double originY;	
    private double scaleX; 
    private double scaleY;
    private int num_hits=0;
    private DefaultTableModel dtm;
    private ResponseWrangler raw_response;
    public JLabel numHitsLabel;
    private ResponseWrangler norm_response;
    private ResponseWrangler norm_pos_response;
    private ScatterPlotSlider slider;

    private int num_plates = 0;
    private DecimalFormat df = new DecimalFormat("#####.####");
    // private DecimalFormat df2 = new DecimalFormat("##.####");
    private JPanel panel;
    private JPanel panel2;
    
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public ScatterPlot(DialogMainFrame _dmf) {
	super("ScatterPlot");
	setSize(800, 600);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	this.dmf = _dmf;
    //need the assay run id
	table = dmf.getDatabaseManager().getDatabaseRetriever().getDataForScatterPlot(10);
	LOGGER.info("row count: " + table.getRowCount());	    

	raw_response = new ResponseWrangler(table, ResponseWrangler.RAW);
	norm_response = new ResponseWrangler(table, ResponseWrangler.NORM);
	norm_pos_response = new ResponseWrangler(table, ResponseWrangler.NORM_POS);

    slider = new ScatterPlotSlider(min_response, max_response, mean_3_sd, 100, this);

	panel2 = new JPanel(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();

    JLabel label = new JLabel("Algorithm:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 0;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.insets = new Insets(5, 5, 2, 2);
    c.anchor = GridBagConstraints.LINE_END;
    panel2.add(label, c);

    ComboItem[] algorithmTypes = new ComboItem[]{ new ComboItem(3,"mean + 3SD"), new ComboItem(2,"mean + 2SD")};
    
    algorithmList = new JComboBox<ComboItem>(algorithmTypes);
    algorithmList.setSelectedIndex(0);
    c.gridx = 1;
    c.gridy = 0;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    panel2.add(algorithmList, c);
    algorithmList.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent evt) {
	    switch(((ComboItem)algorithmList.getSelectedItem()).getKey()){
	    case 3:
		setThreshold( mean_3_sd);		
		break;
	    case 2:
		setThreshold( mean_2_sd);		
		break;
	    }
        }
    });


    
     label = new JLabel("Threshold:", SwingConstants.RIGHT);
    c.gridx = 2;
    c.gridy = 0;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    panel2.add(label, c);


    thresholdField = new JTextField(Double.toString(threshold), 10);
    c.gridx = 3;
    c.gridy = 0;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    panel2.add(thresholdField, c);
    thresholdField.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent evt) {
	    setThreshold(Double.valueOf(thresholdField.getText()));	    
            
        }
    });


    
    c.gridx = 4;
    c.gridy = 0;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    panel2.add(genHitsBtn, c);
    genHitsBtn.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent evt) {
            repaint();
        }
    });

 label = new JLabel("Response:", SwingConstants.RIGHT);
    c.gridx = 0;
    c.gridy = 1;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    panel2.add(label, c);

    ComboItem[] responseTypes = new ComboItem[]{ new ComboItem(1,"raw"), new ComboItem(2,"norm"), new ComboItem(3,"norm_pos")};
    
    responseList = new JComboBox<ComboItem>(responseTypes);
    responseList.setSelectedIndex(0);
    c.gridx = 1;
    c.gridy = 1;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    panel2.add(responseList, c);
    responseList.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent evt) {
	    switch(((ComboItem)responseList.getSelectedItem()).getKey()){
	    case 1:
		updateAllVariables(raw_response);
		break;
	    case 2:
		updateAllVariables(norm_response);
		break;
	    case 3:
		updateAllVariables(norm_pos_response);
		break;
	    }
        }
    });

    
     label = new JLabel("Number of hits:", SwingConstants.RIGHT);
    c.gridx = 2;
    c.gridy = 1;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    panel2.add(label, c);


    numHitsLabel = new JLabel("");
    c.gridx = 3;
    c.gridy = 1;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    panel2.add(numHitsLabel, c);

    getContentPane().add(panel2, BorderLayout.SOUTH);


    updateAllVariables(norm_response);

    
    dtm = (DefaultTableModel)table.getModel();
    
    panel = new JPanel() {
	    
	    
      public void paintComponent(Graphics g) {
	  // Plot points below.
	Font font = new Font(null, Font.PLAIN, 12);    
	g.setFont(font);


	  wth = getWidth();
	  hgt = getHeight();
	  
           originX = margin;
	   originY = getHeight() - margin;
	
	  scaleX = (wth-margin)/format; 
	  scaleY = (hgt-margin)/max_response;  
	  
	  for (int row = 0; row < dtm.getRowCount(); row++) {
	      int plate = (int)dtm.getValueAt(row, 0);
	      plate_set.add((int)dtm.getValueAt(row, 0));
	      int well = (int)dtm.getValueAt(row, 1);
	      Double response = Double.valueOf((float)dtm.getValueAt(row, 2));	
	      Double bkgrnd = Double.valueOf((float)dtm.getValueAt(row, 3));
	      Double norm = Double.valueOf((float)dtm.getValueAt(row, 4));
	      Double normpos = Double.valueOf((float)dtm.getValueAt(row, 5));
	      int well_type_id = (int)dtm.getValueAt(row, 6);
	      String replicates = (String)dtm.getValueAt(row, 7);
	      String target = (String)dtm.getValueAt(row, 8);
	      int sample_id = 0;
	      if(dtm.getValueAt(row, 9) != null) sample_id = (int)dtm.getValueAt(row, 9);   

	      //set color based on well type
	      switch(well_type_id){
	      case 1: g.setColor(Color.black);
		  break;
	      case 2: g.setColor(Color.green);
		  break;
	      case 3: g.setColor(Color.red);
		  //LOGGER.info("color set to red");
	   	  
		  break;
	      case 4: g.setColor(Color.gray);
		  break;
	      }

	      
	      int xpt = (int)Math.round(originX + scaleX*well);
	      int ypt = (int)Math.round(originY - scaleY*norm);

	      g.drawString("X", xpt, ypt);

	      g.setColor(Color.black);
	      g.drawLine(margin, 0,  margin, hgt-margin); // y-axis
	      g.drawLine(margin, hgt-margin, wth-10, hgt-margin); // x-axis
	      g.drawString("Well", (int)Math.round(originX + (wth-margin)/2)  , (int)Math.round(originY + margin/2 + 10) );
	      
	      //draw the axes ticks and labels
	      switch(format){
	      case 96:
		  for( int j = 10; j <100; j=j+10 ){  //X- axis
		      g.drawLine( (int)Math.round(originX +scaleX*j), hgt-margin,   (int)Math.round(originX +scaleX*j), hgt-margin+10);
		      g.drawString(String.valueOf(j),  (int)Math.round(originX +scaleX*j - 10), hgt-margin+25 );
		  }
	    
		  for(int k = 1; k <6; k++){ //Y axis
		      g.drawLine( (int)Math.round(originX-10), (int)Math.round(originY-k*((hgt-margin)/6)),
				  (int)Math.round(originX), (int)Math.round(originY-k*((hgt-margin)/6)));
		      g.drawString(String.valueOf(df.format((k*((hgt-margin)/6))/scaleY)),  (int)Math.round(originX - 50),
				   (int)Math.round(originY-k*((hgt-margin)/6)) );
	   	
		  }   
		  break;
	      case 384:
		  break;    
	      case 1536:
		  break;	    
	      }

	      //draw "Response" on the Y axis
	      Graphics2D g2d = (Graphics2D) g.create();
	      AffineTransform affineTransform = new AffineTransform();
	      affineTransform.rotate(Math.toRadians(-90), 0, 0);
	      Font rotatedFont = font.deriveFont(affineTransform);
	      g2d.setFont(rotatedFont);
	      g2d.drawString("Response", Math.round(originX -40)  , Math.round(originY - hgt/2 + 40) );
	      g2d.setFont(font);
	      g2d.dispose();

 		Graphics2D g2db = (Graphics2D) g.create();
		//set the stroke of the copy, not the original 
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
		g2db.setStroke(dashed);
		g2db.drawLine(margin, (int)Math.round(originY - scaleY*threshold) , wth-10, (int)Math.round(originY - scaleY*threshold));
	      //label the threshold if it is + 2 or 3 SD
		if( threshold == mean_3_sd){
		g2db.setColor(Color.blue);
		g2db.drawString( "mean(background) + 3SD", margin+20  , (int)Math.round(originY - scaleY*threshold - 10)  );
		}
		if( threshold == mean_2_sd){
		g2db.setColor(Color.blue);
		g2db.drawString( "mean(background) + 2SD", margin+20  , (int)Math.round(originY - scaleY*threshold - 10)  );
		}
		//gets rid of the copy
		g2db.dispose();
	      
	  }

	  
      }


	};  //removed semicolon here
  
    getContentPane().add(slider, BorderLayout.EAST);
     getContentPane().add(panel, BorderLayout.CENTER);
     
    setVisible(true);
   
 
  }
    
    public void setThreshold(double _threshold){
	this.threshold = _threshold;
        thresholdField.setText(df.format(threshold));
	slider.setDoubleValue(threshold);

	this.repaint();
	
    }

    public void updateAllVariables(ResponseWrangler selected_response){
	
	format = selected_response.getFormat();
	num_plates = selected_response.getNum_plates();
	max_response = selected_response.getMax_response();
	min_response = selected_response.getMin_response();
	mean_bkgrnd = selected_response.getMean_bkgrnd();
	stdev_bkgrnd = selected_response.getStdev_bkgrnd();
	mean_3_sd = selected_response.getMean_3_sd();
	mean_2_sd = selected_response.getMean_2_sd();
	sortedResponse = selected_response.getSortedResponse();
	num_hits = selected_response.getHitsAboveThreshold(threshold);
	LOGGER.info("num_hits: " + Integer.toString(num_hits));
	LOGGER.info("num_hits: " + numHitsLabel);
	slider.setDoubleValue(threshold);
	numHitsLabel.setText(Integer.toString(num_hits));
    
    repaint();	
    }
    
}
