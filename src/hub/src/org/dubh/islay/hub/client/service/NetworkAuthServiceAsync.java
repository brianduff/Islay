package org.dubh.islay.hub.client.service;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NetworkAuthServiceAsync {
  void getRequestTokenUrl(Network network, AsyncCallback<String> callback);

  void getAccessToken(Network network, String requestToken, String verifyToken,
      AsyncCallback<UserAccount> callback);
}
