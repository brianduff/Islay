package org.dubh.islay.hub.client;

import java.util.List;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Activity;
import org.dubh.islay.hub.shared.Network;
import org.dubh.islay.hub.shared.Path;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A service for getting activities.
 * 
 * @author brianduff
 */
@RemoteServiceRelativePath(Path.ACTIVITY)
public interface ActivityService extends RemoteService {
  List<Activity> getRecentActivities(UserAccount user, Network network);
}
