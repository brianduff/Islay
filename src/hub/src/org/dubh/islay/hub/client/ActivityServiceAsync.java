package org.dubh.islay.hub.client;

import java.util.List;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Activity;
import org.dubh.islay.hub.shared.Network;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ActivityServiceAsync {
  void getRecentActivities(UserAccount user, Network network, AsyncCallback<List<Activity>> callback);
}
