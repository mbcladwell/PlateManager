package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

/** Upon insert session gains a timestamp */
public class Session {

  private int pmUserID;
  private String pmUserName;
  private int pmUserType; // admin, superuser, user
  private int projectID;
  private String projectSysName;
  private Long sessionID;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static final long serialVersionUID = 1L;

  public Session() {}

  public void setPmuserID(int _id) {
    pmUserID = _id;
  }

  public int getPmuserID() {
    return pmUserID;
  }

  public void setPmuserName(String _n) {
    pmUserName = _n;
  }

  public String getPmuserName() {
    return pmUserName;
  }

  public void setPmuserType(int _i) {
    pmUserType = _i;
  }

  public int getPmuserType() {
    return pmUserType;
  }

  public String getPmuserTypeString() {
    String usertype = new String();
    switch (pmUserType) {
      case 1:
        usertype = "admin";
        break;
      case 2:
        usertype = "superuser";
        break;
      case 3:
        usertype = "user";
        break;
    }
    return usertype;
  }

  public void setProjectID(int _id) {
    projectID = _id;
  }

  public int getProjectID() {
    return projectID;
  }

  public void setProjectSysName(String _s) {
    projectSysName = _s;
  }

  public String getProjectSysName() {
    return projectSysName;
  }

  public Long getSessionID() {
    return sessionID;
  }

  public void setSessionID(Long _l) {
    sessionID = _l;
  }
}
