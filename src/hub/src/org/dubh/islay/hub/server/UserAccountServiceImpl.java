package org.dubh.islay.hub.server;

import java.util.Date;

import net.sf.jsr107cache.Cache;

import org.dubh.islay.hub.client.UserAccountService;
import org.dubh.islay.hub.model.UserAccount;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

@Singleton
@SuppressWarnings("serial")
public class UserAccountServiceImpl extends RemoteServiceServlet implements UserAccountService {
  private final UserService gaeUserService;
  private final ObjectifyFactory of;
  private final Cache cache;
  
  private static final String CURRENT_USER_KEY = "currentUser";
  
  @Inject
  UserAccountServiceImpl(UserService gaeUserService, ObjectifyFactory of, Cache cache) {
    this.gaeUserService = gaeUserService;
    this.of = of;
    this.cache = cache;
  }
  
  @Override
  public UserAccount getLoggedInUser() {
    User gaeUser = gaeUserService.getCurrentUser();
    if (gaeUser == null) {
      cache.remove(CURRENT_USER_KEY);
      return null;
    }

    if (cache.get(CURRENT_USER_KEY) != null) {
      return (UserAccount) cache.get(CURRENT_USER_KEY);
    }
    
    Objectify ofy = of.begin();
    UserAccount user = ofy.query(UserAccount.class).filter("userId", getIdentity(gaeUser)).get();
    if (user == null) {
      // Create a new datastore user and insert it into the datastore.
      user = new UserAccount().setUserId(getIdentity(gaeUser)).setJoinDate(new Date());
      if (gaeUser.getEmail() != null) {
        user.setEmailAddress(gaeUser.getEmail());
      }
      ofy.put(user);
    }
    cache.put(CURRENT_USER_KEY, user);
    return user;
  }
  
  private String getIdentity(User user) {
    if (user.getFederatedIdentity() != null) {
      return user.getFederatedIdentity();
    }
    if (user.getNickname() != null) {
      return user.getNickname();
    }
    if (user.getEmail() != null) {
      return user.getEmail();
    }
    throw new IllegalStateException(user.toString());
  }

  @Override
  public void save(UserAccount account) {
    cache.put(CURRENT_USER_KEY, account);
    of.begin().put(account);
  }
}
