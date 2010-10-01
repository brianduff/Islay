package org.dubh.islay.hub.client;

import org.dubh.islay.hub.model.UserAccount;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserAccountServiceAsync {
  void getLoggedInUser(AsyncCallback<UserAccount> callback);
  void save(UserAccount account, AsyncCallback<Void> callback);
}
