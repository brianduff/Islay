package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
  void getLoggedInUser(AsyncCallback<User> callback);
  void createLoginUrl(AsyncCallback<String> callback);
  void createLogoutUrl(AsyncCallback<String> callback);
}
