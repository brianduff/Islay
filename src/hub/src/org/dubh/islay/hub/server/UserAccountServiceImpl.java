package org.dubh.islay.hub.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import net.sf.jsr107cache.Cache;

import org.dubh.islay.hub.client.service.UserAccountService;
import org.dubh.islay.hub.model.UserAccount;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.common.base.Charsets;
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
    
    Objectify ofy = of.begin();
    UserAccount user = ofy.query(UserAccount.class).filter("userId", getIdentity(gaeUser)).get();
    if (user == null) {
      // Create a new datastore user and insert it into the datastore.
      user = new UserAccount().setUserId(getIdentity(gaeUser)).setJoinDate(new Date());
      if (gaeUser.getEmail() != null) {
        user.setEmailAddress(gaeUser.getEmail());
      }
      updateEmailMD5(user);
      ofy.put(user);
    }
    
    // Sanity check, since this column didn't exist before
    if (user.getEmailMD5Sum() == null) {
      updateEmailMD5(user);
      ofy.put(user);
    }
    
    System.out.println("Got user " + user);
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
    updateEmailMD5(account);
    of.begin().put(account);
  }
  
  private void updateEmailMD5(UserAccount account) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      String digest = toHexString(
          md.digest(account.getEmailAddress().toLowerCase().getBytes(Charsets.UTF_8)));
      account.setEmailMD5Sum(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
  
  private String toHexString(byte[] bytes) {
    final String HEXES = "0123456789abcdef";
    final StringBuilder hex = new StringBuilder(2 * bytes.length);
    for (final byte b : bytes) {
      hex.append(HEXES.charAt((b & 0xF0) >> 4))
         .append(HEXES.charAt((b & 0x0F)));
    }
    return hex.toString();
  }
}
