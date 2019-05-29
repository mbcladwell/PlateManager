package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Upon insert session gains a timestamp
 *
 * <p>Session provides user name and ID and project sys name and id
 */
public class Session {

  private int user_id;
  private String user_name;
    private String password;
  private int user_group_id;
  private String user_group; // admin, superuser, user
  private int project_id;
  private String project_sys_name;
  private String plate_set_sys_name;
    private int plate_set_id;
    private int plate_id;
  private Long session_id;
  private String working_dir;
  private String temp_dir;
    private String help_url_prefix;  
    private String postgres_ip;
    private String db_name;
    private DialogMainFrame dmf;
   
    
    
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static final long serialVersionUID = 1L;

  public Session( DialogMainFrame _dmf) {
      dmf = _dmf;
      //try (


    //the base folder is ./, the root of the main.properties file  

    //load all the properties from this file
    //prop.load(file);

     //InputStream input = Session.class.getClassLoader().getResourceAsStream("./limsnucleus.properties")

     //)
    try{
    String path = "./limsnucleus.properties";

    //load the file handle for main.properties
     FileInputStream file = new FileInputStream(path);

            Properties prop = new Properties();

            // load a properties file
            prop.load(file);

            // get the property value and print it out
            System.out.println(prop.getProperty("pgip"));
	    postgres_ip = prop.getProperty("pgip");
            System.out.println(prop.getProperty("db.name"));
	    db_name = prop.getProperty("db.name");
            System.out.println(prop.getProperty("base.help.url"));
	    help_url_prefix = prop.getProperty("base.help.url");
            System.out.println(prop.getProperty("password"));
	    password = prop.getProperty("password");
	    System.out.println(prop.getProperty("username"));
	    user_name = prop.getProperty("username");
	    if(user_name.equals("null")){
			new DialogLogin(this, "", Dialog.ModalityType.DOCUMENT_MODAL);
	    }
	  
	    temp_dir = new File(System.getProperty("java.io.tmpdir")).toString();
	    working_dir = new File(System.getProperty("user.dir")).toString();
	      file.close();
	 
        } catch (IOException ex) {
            ex.printStackTrace();
        }

     
  }

  public void setUserID(int _id) {
    user_id = _id;
  }

  public int getUserID() {
    return user_id;
  }

  public void setUserName(String _n) {
    user_name = _n;
  }

  public String getUserName() {
    return user_name;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String _p) {
    password= _p;
  }

  public void setUserGroup(String _s) {
    user_group = _s;
  }

  public String getUserGroup() {
    return user_group;
  }

  public int getUserGroupID() {
    return user_group_id;
  }

  public void setUserGroupID(int _i) {
    user_group_id = _i;
  }

  public void setProjectID(int _id) {
    project_id = _id;
  }

  public int getProjectID() {
    return project_id;
  }

  public void setProjectSysName(String _s) {
    project_sys_name = _s;
  }

  public String getProjectSysName() {
    return project_sys_name;
  }

  public void setPlateSetSysName(String _s) {
    plate_set_sys_name = _s;
  }

  public String getPlateSetSysName() {
    return plate_set_sys_name;
  }

      public void setPlateSetID(int _id) {
    plate_set_id = _id;
  }

  public int getPlateSetID() {
    return plate_set_id;
  }

      public void setPlateID(int _id) {
    plate_id = _id;
  }

  public int getPlateID() {
    return plate_id;
  }

    
  public Long getSessionID() {
    return session_id;
  }

  public void setSessionID(Long _l) {
    session_id = _l;
  }

  public void setTempDir(String _s) {
    temp_dir = _s;
  }

  public String getTempDir() {
    return temp_dir;
  }

  public void setWorkingDir(String _s) {
    working_dir = _s;
  }

  public String getWorkingDir() {
    return working_dir;
  }
    public String getHelpURLPrefix(){
	return help_url_prefix;
    }
}
