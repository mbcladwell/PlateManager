package pm;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

/**
 * Upon insert session gains a timestamp
 *
 * <p>Session provides user name and ID and project sys name and id
 */
public class Session {

  private int userID;
  private String userName;
  private int userTypeID;
  private String userType; // admin, superuser, user
  private int projectID;
  private String projectSysName;
  private Long sessionID;
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static final long serialVersionUID = 1L;

  public Session() {}

  public void setUserID(int _id) {
    userID = _id;
  }

  public int getUserID() {
    return userID;
  }

  public void setUserName(String _n) {
    userName = _n;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserType(String _s) {
    userType = _s;
  }

  public String getUserType() {
    return userType;
  }

  public int getUserTypeID() {
    return userTypeID;
  }

  public void setUserTypeID(int _i) {
    userTypeID = _i;
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
