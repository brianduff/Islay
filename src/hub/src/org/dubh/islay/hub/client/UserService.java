package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side service interface to look up user information.
 * 
 * @author brianduff
 */
@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
  /**
   * @return the currently logged in user, or {@code null} if no user is
   *    currently logged in.
   */
  User getLoggedInUser();
  
  /**
   * @return a URL that can be used to log in to this application.
   */
  String createLoginUrl();
  
  /**
   * @return a URL that can be used to log out of this application.
   */
  String createLogoutUrl();
}
