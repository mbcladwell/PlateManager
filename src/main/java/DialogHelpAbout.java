package pm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.util.logging.*;

public class DialogHelpAbout extends JDialog {
  static JButton button;
  static JLabel licenceKey;
  static JLabel picLabel;
  static JLabel label;

  static JButton okButton;
  final Instant instant = Instant.now();
  final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
  private static final long serialVersionUID = 1L;
 private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public DialogHelpAbout() {

    // Create and set up the window.
    // JFrame frame = new JFrame("Add Project");
    JPanel pane = new JPanel(new GridBagLayout());
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    GridBagConstraints c = new GridBagConstraints();
    BufferedImage img = null;
    try {
      img = ImageIO.read(getClass().getResource("resources/mwplate.png"));
    } catch (IOException e) {
      System.out.println("no image");
    }

    picLabel = new JLabel(new ImageIcon(img), SwingConstants.CENTER);
    pane.add(picLabel);
    // this.setIconImage(img);
    this.setTitle("About PlateManager");
    // c.gridwidth = 2;

    label = new JLabel("Date:", SwingConstants.CENTER);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(5, 5, 2, 2);
    pane.add(label, c);

    label = new JLabel(df.format(Date.from(instant)));
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    pane.add(label, c);

    label = new JLabel("Name:", SwingConstants.RIGHT);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 1;
    pane.add(label, c);

    okButton = new JButton("OK");
    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.setActionCommand("ok");
    okButton.setEnabled(true);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 2;
    c.gridy = 5;
    c.gridwidth = 1;
    c.gridheight = 1;
    okButton.addActionListener(
        (new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            dispose();
          }
        }));

    pane.add(okButton, c);

    this.getContentPane().add(pane, BorderLayout.CENTER);
    this.pack();
    this.setLocation(
        (Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2,
        (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
    this.setVisible(true);
  }

  public boolean getLitecoinTransaction(String transactionId) {

    try {
      URL url = new URL("https://api.blockcypher.com/v1/btc/main/txs/" + transactionId);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
      for (String line; (line = reader.readLine()) != null; ) {
        System.out.println(line);
      }
    } catch (Exception e) {

    }
    return true;
  }
}
