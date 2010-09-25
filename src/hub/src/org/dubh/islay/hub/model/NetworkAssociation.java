package org.dubh.islay.hub.model;

import java.io.Serializable;

import org.dubh.islay.hub.shared.Network;

/**
 * An association from a user to a particular social network.
 * 
 * @author brianduff
 */
@SuppressWarnings("serial")
public class NetworkAssociation implements Serializable {
  private Network network;
  private String requestToken;
  private String requestTokenSecret;
  private String accessToken;
  private String accessTokenSecret;
  
  public Network getNetwork() {
    return network;
  }

  public NetworkAssociation setNetwork(Network network) {
    this.network = network;
    return this;
  }

  public String getRequestToken() {
    return requestToken;
  }
  
  public NetworkAssociation setRequestToken(String requestToken) {
    this.requestToken = requestToken;
    return this;
  }
  
  public String getRequestTokenSecret() {
    return requestTokenSecret;
  }
  
  public NetworkAssociation setRequestTokenSecret(String requestTokenSecret) {
    this.requestTokenSecret = requestTokenSecret;
    return this;
  }
  
  public String getAccessToken() {
    return accessToken;
  }
  
  public NetworkAssociation setAccessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }
  
  public String getAccessTokenSecret() {
    return accessTokenSecret;
  }
  
  public NetworkAssociation setAccessTokenSecret(String accessTokenSecret) {
    this.accessTokenSecret = accessTokenSecret;
    return this;
  }
  
  /**
   * Returns true if we have been successfully granted an OAuth access token
   * for this service.
   * 
   * @return
   */
  public boolean isAccessTokenGranted() {
    return accessToken != null;
  }
}
