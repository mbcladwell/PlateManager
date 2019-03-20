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
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.common.math.Stats;

public class ScatterPlot extends JFrame {
    private List coords1 = new ArrayList();
    private List coords2 = new ArrayList();
    private JTextField labelsX = new JTextField("10", 6);
    private JTextField labelsY = new JTextField("10", 6);
    private JButton updateBtn = new JButton("Update");
    private DatabaseRetriever dbr;
    private DialogMainFrame dmf;
    private ArrayList table;
    private Set<Integer> plate_set = new HashSet<Integer>();
    private Set<Integer> well_set = new HashSet<Integer>();
    private List<Float> norm_list = new LinkedList<Float>();
    private List<Float> bkgrnd_list = new LinkedList<Float>();
    
    private int format;
    private Float max_response;
    private double min_response;
    private double mean_bkgrnd;
    private double stdev_bkgrnd;
    private double threshold;
    private double mean_3_sd;
    private int margin = 60;
    private int wth;
    private int hgt;	  
    private float originX;
    private float originY;	
    private float scaleX; 
    private float scaleY;  


    private int num_plates = 0;
  private DecimalFormat df = new DecimalFormat("#####.##");
    private JPanel panel;
    
      private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public ScatterPlot(DialogMainFrame _dmf) {
    super("ScatterPlot");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.dmf = _dmf;
    table = dmf.getDatabaseManager().getDatabaseRetriever().getDataForScatterPlot(9);

    // Array look like
    //  plate, well, response, bkgrnd_sub,   norm,   norm_pos ,  well_type_id,  replicates, target 
    // -,1,1,0.293825507,0.293825507,0.434007883,0.511445642,1,1,a
    // class org.postgresql.jdbc.PgArray
    // cannot retrieve first element so first element is duplicated, start extracting at [1]
    // set up some variable with data limits
    
    for (int i = 0; i < table.size(); i++) {
	String[] holder = table.get(i).toString().split(",");
	int plate = Integer.parseInt(holder[1]);
	plate_set.add(Integer.parseInt(holder[1]));
	int well = Integer.parseInt(holder[2]);
	well_set.add(Integer.parseInt(holder[2]));
	Float response = Float.parseFloat(holder[3]);
	Float bkgrnd = Float.parseFloat(holder[4]);
	bkgrnd_list.add(Float.parseFloat(holder[4]));
	
	Float norm = Float.parseFloat(holder[5]);
	norm_list.add(Float.parseFloat(holder[5]));
	Float normpos = Float.parseFloat(holder[6]);
	int well_type_id = Integer.parseInt(holder[7]);
	int replicates = Integer.parseInt(holder[8]);
	String target = holder[9];
    	}

    format = well_set.size();
    num_plates = plate_set.size();
    max_response = Collections.max(norm_list);
    min_response = Collections.min(norm_list);
    
    mean_bkgrnd = Stats.meanOf(bkgrnd_list);
    stdev_bkgrnd = Stats.of(bkgrnd_list).populationStandardDeviation();
    mean_3_sd = mean_bkgrnd + 3*(stdev_bkgrnd);
	      threshold = mean_3_sd;

    
    panel = new JPanel() {
	    
	    
	      /*
	    public String getToolTipText(MouseEvent event) {
		int xPos = event.getX();   // the x, y coords pointed to
		int yPos = event.getY();	
		return "("+(int)((xPos-originX)/scaleX)+","
		    +(int)((yPos-originY)/-scaleY)+")"; // return tooltip text
	    }
	    */
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

	  }

	  
	  
      }


	};  //removed semicolon here
    panel.setToolTipText("");
    drawThreshold( margin, (int)Math.round(originY - scaleY*threshold) , wth-10, (int)Math.round(originY - scaleY*threshold), "mean(background + 3SD)"); // hit threshold

     getContentPane().add(panel, BorderLayout.CENTER);
     // setContentPane(panel);

    JPanel lblEditPanel = new JPanel();
    lblEditPanel.add(new JLabel("Y-axis labels:"));
    lblEditPanel.add(labelsY);
    lblEditPanel.add(new JLabel("X-axis labels:"));
    lblEditPanel.add(labelsX);
    updateBtn.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent evt) {
            repaint();
        }
    });
    lblEditPanel.add(updateBtn);
    getContentPane().add(lblEditPanel, BorderLayout.SOUTH);

    ScatterPlotSlider slider = new ScatterPlotSlider(min_response, max_response, mean_3_sd, 100, this);
    getContentPane().add(slider, BorderLayout.EAST);

    setVisible(true);
   
 
  }

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
  
    
    public void setThreshold(double _threshold){
	this.threshold = _threshold;
	this.repaint();
	//drawThreshold()
	
	
    }
    
}
