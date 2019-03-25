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
import java.util.ArrayList;
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

import com.google.common.math.Stats;

public class ScatterPlot extends JFrame {
    private JButton genHitsBtn = new JButton("Generate hit list");
    private DatabaseRetriever dbr;
    private DialogMainFrame dmf;
    private CustomTable table;
    private Set<Integer> plate_set = new HashSet<Integer>();
    private Set<Integer> well_set = new HashSet<Integer>();
    private List<Float> norm_list = new LinkedList<Float>();
    private List<Float> bkgrnd_list = new LinkedList<Float>();
        static JComboBox<ComboItem> algorithmList;
    static JComboBox<ComboItem> responseList;
    private JTextField thresholdField;
    private int format;
    private Float max_response;
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
    private float originX;
    private float originY;	
    private float scaleX; 
    private float scaleY;
    private int num_hits=0;
    private ResponseWrangler raw_response;
    
    private ResponseWrangler norm_response;
    private ResponseWrangler norm_pos_response;
    

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

    raw_response = new ResponseWrangler(table, ResponseWrangler.RAW);
    norm_response = new ResponseWrangler(table, ResponseWrangler.NORM);
    norm_pos_response = new ResponseWrangler(table, ResponseWrangler.NORM_POS);
 
    
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
	  
	  for (int i = 0; i < table.size(); i++) {
	      String[] holder = table.get(i).toString().split(",");
	      int plate = Integer.parseInt(holder[1]);
	      int well = Integer.parseInt(holder[2]);
	      //LOGGER.info("well: " + well);
	      Float response = Float.parseFloat(holder[3]);
	      Float bkgrnd = Float.parseFloat(holder[4]);
	      Float norm = Float.parseFloat(holder[5]);
	      Float normpos = Float.parseFloat(holder[6]);
	      int well_type_id = Integer.parseInt(holder[7]);
	      int replicates = Integer.parseInt(holder[8]);
	      String target = holder[9];
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

	      
	      int xpt = Math.round(originX + scaleX*well);
	      int ypt = Math.round(originY - scaleY*norm);

	      g.drawString("X", xpt, ypt);

	      g.setColor(Color.black);
	      g.drawLine(margin, 0,  margin, hgt-margin); // y-axis
	      g.drawLine(margin, hgt-margin, wth-10, hgt-margin); // x-axis
	      g.drawString("Well", Math.round(originX + (wth-margin)/2)  , Math.round(originY + margin/2 + 10) );
	      
	      //draw the axes ticks and labels
	      switch(format){
	      case 96:
		  for( int j = 10; j <100; j=j+10 ){  //X- axis
		      g.drawLine( Math.round(originX +scaleX*j), hgt-margin,   Math.round(originX +scaleX*j), hgt-margin+10);
		      g.drawString(String.valueOf(j),  Math.round(originX +scaleX*j - 10), hgt-margin+25 );
		  }
	    
		  for(int k = 1; k <6; k++){ //Y axis
		      g.drawLine( Math.round(originX-10), Math.round(originY-k*((hgt-margin)/6)),
				  Math.round(originX), Math.round(originY-k*((hgt-margin)/6)));
		      g.drawString(String.valueOf(df.format((k*((hgt-margin)/6))/scaleY)),  Math.round(originX - 50),
				   Math.round(originY-k*((hgt-margin)/6)) );
	   	
		  }   
		  break;
	      case 384:
		  break;    
	      case 1536:
		  break;	    
	      }


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
		if( threshold == mean_3_sd){
		g2db.setColor(Color.blue);
		g2db.drawString( "mean(background + 3SD)", margin+20  , (int)Math.round(originY - scaleY*threshold - 10)  );
		}
		if( threshold == mean_2_sd){
		g2db.setColor(Color.blue);
		g2db.drawString( "mean(background + 2SD)", margin+20  , (int)Math.round(originY - scaleY*threshold - 10)  );
		}
		//gets rid of the copy
		g2db.dispose();


	      
	  }

	  
	  
      }


	};  //removed semicolon here
    panel.setToolTipText("");
  
     getContentPane().add(panel, BorderLayout.CENTER);
     // setContentPane(panel);

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
	    threshold = mean_3_sd;
	    thresholdField.setText(df.format(threshold));
            repaint();
		
		break;
	    case 2:
	    threshold = mean_2_sd;
            repaint();
	    thresholdField.setText(df.format(threshold));
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
	    threshold = Double.valueOf(thresholdField.getText());	    
            repaint();
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

    ComboItem[] responseTypes = new ComboItem[]{ new ComboItem(1,"norm"), new ComboItem(2,"norm_pos")};
    
    responseList = new JComboBox<ComboItem>(responseTypes);
    responseList.setSelectedIndex(0);
    c.gridx = 1;
    c.gridy = 1;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    panel2.add(responseList, c);

    
     label = new JLabel("Number of hits:", SwingConstants.RIGHT);
    c.gridx = 2;
    c.gridy = 1;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    panel2.add(label, c);


    JLabel  numHitsLabel = new JLabel(Integer.toString(num_hits), SwingConstants.RIGHT);
    c.gridx = 3;
    c.gridy = 1;
    c.gridheight = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_START;
    panel2.add(numHitsLabel, c);



    getContentPane().add(panel2, BorderLayout.SOUTH);

    ScatterPlotSlider slider = new ScatterPlotSlider(min_response, max_response, mean_3_sd, 100, this);
    getContentPane().add(slider, BorderLayout.EAST);

    setVisible(true);
   
 
  }
    /*
	    public void drawThreshold( int x1, int y1, int x2, int y2, String label){

		//creates a copy of the Graphics instance
		Graphics2D g2d = (Graphics2D) panel.getGraphics().create();

		//set the stroke of the copy, not the original 
		Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
		g2d.setStroke(dashed);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.setColor(Color.blue);
		g2d.drawString(label, x1+20  , y1-5 );

		//gets rid of the copy
		g2d.dispose();
	    }
    */
    
    public void setThreshold(double _threshold){
	this.threshold = _threshold;
        thresholdField.setText(df.format(threshold));

	this.repaint();
	//drawThreshold()
	
	
    }

    public void updateAllVariables(ResponseWrangler selected_response){

	
	format = selected_response.getFormat();
 num_plates = selected_response.getNum_plates();
    max_response = selected_response.getMax_response();
    min_response = selected_response.getMin_response();
    
    mean_bkgrnd = selected_response.getMean_bkgrnd();
    stdev_bkgrnd = Stats.of(bkgrnd_list).populationStandardDeviation();
    mean_3_sd = selected_response.getMean_3_sd();
    mean_2_sd = selected_response.getMean_2_sd();
    sortedResponse = selected_response.getSortedResponse();
    
    repaint();	
    }
    
}
