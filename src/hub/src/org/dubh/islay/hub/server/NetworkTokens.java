package org.dubh.islay.hub.server;

import java.io.Serializable;

import javax.persistence.Embedded;

import org.dubh.islay.hub.shared.Network;

/**
 * A set of tokens and token secrets for a particular network.
 * 
 * @author brianduff
 */
@SuppressWarnings("serial")
public class NetworkTokens implements Serializable {
  private Network network;
  private @Embedded TokenAndSecret request;
  private @Embedded TokenAndSecret access;
  
  public Network getNetwork() {
    return network;
  }

  public NetworkTokens setNetwork(Network network) {
    this.network = network;
    return this;
  }

  public TokenAndSecret getRequestToken() {
    return request;
  }
  
  public NetworkTokens setRequestToken(TokenAndSecret requestToken) {
    this.request = requestToken;
    return this;
  }
  
  public TokenAndSecret getAccessToken() {
    return access;
  }
  
  public NetworkTokens setAccessToken(TokenAndSecret accessToken) {
    this.access = accessToken;
    return this;
  }
  
  public static final class TokenAndSecret {
    private String token;
    private String secret;
    
    public TokenAndSecret() {
      
    }
    
    public TokenAndSecret(String token, String secret) {
      this.token = token;
      this.secret = secret;
    }
    
    public String getToken() {
      return token;
    }
    
    public String getSecret() {
      return secret;
    }
  }
}
