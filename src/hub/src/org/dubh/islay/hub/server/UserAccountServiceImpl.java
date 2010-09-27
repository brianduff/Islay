package org.dubh.islay.hub.server;

import java.util.Date;

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
  
  @Inject
  UserAccountServiceImpl(UserService gaeUserService, ObjectifyFactory of) {
    this.gaeUserService = gaeUserService;
    this.of = of;
  }
  
  @Override
  public UserAccount getLoggedInUser() {
    User gaeUser = gaeUserService.getCurrentUser();
    if (gaeUser == null) {
      return null;
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
  public String createLoginUrl() {
    return "/_ah/login_required";
  }

  @Override
  public String createLogoutUrl() {
    return gaeUserService.createLogoutURL("/");
  }

  @Override
  public void save(UserAccount account) {
    of.begin().put(account);
  }
}
