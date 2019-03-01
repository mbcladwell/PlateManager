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
  private int userGroupID;
  private String userGroup; // admin, superuser, user
  private int projectID;
  private String projectSysName;
  private String plateSetSysName;
  private Long sessionID;
  private String workingDir;
  private String tempDir;

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

  public void setUserGroup(String _s) {
    userGroup = _s;
  }

  public String getUserGroup() {
    return userGroup;
  }

  public int getUserGroupID() {
    return userGroupID;
  }

  public void setUserGroupID(int _i) {
    userGroupID = _i;
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

  public void setPlateSetSysName(String _s) {
    plateSetSysName = _s;
  }

  public String getPlateSetSysName() {
    return plateSetSysName;
  }

  public Long getSessionID() {
    return sessionID;
  }

  public void setSessionID(Long _l) {
    sessionID = _l;
  }

  public void setTempDir(String _s) {
    tempDir = _s;
  }

  public String getTempDir() {
    return tempDir;
  }

  public void setWorkingDir(String _s) {
    workingDir = _s;
  }

  public String getWorkingDir() {
    return workingDir;
  }
}
