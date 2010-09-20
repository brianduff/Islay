package org.dubh.islay.hub.server;

import org.dubh.islay.hub.client.UserService;
import org.dubh.islay.hub.model.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

@Singleton
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {
  private final com.google.appengine.api.users.UserService gaeUserService;
  private final ObjectifyFactory of;
  
  @Inject
  UserServiceImpl(com.google.appengine.api.users.UserService gaeUserService,
      ObjectifyFactory of) {
    this.gaeUserService = gaeUserService;
    this.of = of;
  }
  
  @Override
  public User getLoggedInUser() {
    com.google.appengine.api.users.User gaeUser = gaeUserService.getCurrentUser();
    if (gaeUser == null) {
      return null;
    }
    
    Objectify ofy = of.begin();
    User user = ofy.query(User.class).filter("userId", getIdentity(gaeUser)).get();
    if (user == null) {
      // Create a new datastore user and insert it into the datastore.
      user = new User().setUserId(getIdentity(gaeUser));
      ofy.put(user);
    }
    return user;
  }
  
  private String getIdentity(com.google.appengine.api.users.User user) {
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
    return gaeUserService.createLoginURL("/");
  }

  @Override
  public String createLogoutUrl() {
    return gaeUserService.createLogoutURL("/");
  }
}
