package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserAccountServiceAsync {
  void getLoggedInUser(AsyncCallback<UserAccount> callback);
  void createLoginUrl(AsyncCallback<String> callback);
  void createLogoutUrl(AsyncCallback<String> callback);
  void save(UserAccount account, AsyncCallback<Void> callback);
}
