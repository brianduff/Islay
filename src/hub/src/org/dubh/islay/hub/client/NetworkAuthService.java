package org.dubh.islay.hub.client;

import java.io.IOException;

import org.dubh.islay.hub.shared.Network;
import org.dubh.islay.hub.shared.Path;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(Path.OAUTH)
public interface NetworkAuthService extends RemoteService {
  /**
   * Get a request token for the specified network.
   * 
   * @param network a network.
   * @return a request token for this network.
   */
  String getRequestTokenUrl(Network network) throws IOException;
  
  /**
   * Attempt to obtain an access token for the specified network, after a user
   * has authorized our request.
   */
  void getAccessToken(Network network, String requestToken, String verifyToken) throws IOException;
}
