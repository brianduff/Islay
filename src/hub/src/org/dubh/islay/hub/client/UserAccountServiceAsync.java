package org.dubh.islay.hub.client;

import java.util.List;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.OpenIdProvider;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserAccountServiceAsync {
  void getLoggedInUser(AsyncCallback<UserAccount> callback);
  void createLoginUrl(AsyncCallback<String> callback);
  void createLogoutUrl(AsyncCallback<String> callback);
  void getOpenIdProviders(AsyncCallback<List<OpenIdProvider>> callback);
}
