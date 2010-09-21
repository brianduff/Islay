package org.dubh.islay.hub.client;

import java.util.List;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.OpenIdProvider;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side service interface to look up user information.
 * 
 * @author brianduff
 */
@RemoteServiceRelativePath("user")
public interface UserAccountService extends RemoteService {
  
  /**
   * @return a list of OpenIdProviders we support.
   */
  List<OpenIdProvider> getOpenIdProviders();
  
  /**
   * @return the currently logged in user, or {@code null} if no user is
   *    currently logged in.
   */
  UserAccount getLoggedInUser();
  
  /**
   * @return a URL that can be used to log in to this application.
   */
  String createLoginUrl();
  
  /**
   * @return a URL that can be used to log out of this application.
   */
  String createLogoutUrl();
}
