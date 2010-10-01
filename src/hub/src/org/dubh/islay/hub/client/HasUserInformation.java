package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

/**
 * Some kind of gwt component that shows user information.
 * 
 * @author brianduff
 */
public interface HasUserInformation {
  void setCurrentUser(UserAccount user);
  void setLogOutUrl(String url);
}
