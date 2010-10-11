package org.dubh.islay.hub.client.service;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Path;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side service interface to look up user information.
 * 
 * @author brianduff
 */
@RemoteServiceRelativePath(Path.USER)
public interface UserAccountService extends RemoteService {
  
  /**
   * @return the currently logged in user, or {@code null} if no user is
   *    currently logged in.
   */
  UserAccount getLoggedInUser();
  
  /**
   * Saves changes to the user account.
   * @param account a user account to save.
   */
  void save(UserAccount account);
}
