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
  private @Embedded TokenAndSecret request = new TokenAndSecret();
  private @Embedded TokenAndSecret access = new TokenAndSecret();
  
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
  
  public TokenAndSecret getAccessToken() {
    return access;
  }

  @Override
  public String toString() {
    return String.format("NetworkTokens[network=%s, request=%s, access=%s]", network, request, access);
  }
  
  public static final class TokenAndSecret {
    private String token;
    private String secret;
    
    public TokenAndSecret() {
      
    }
    
    public String getToken() {
      return token;
    }
    
    public String getSecret() {
      return secret;
    }
    
    public TokenAndSecret setToken(String token) {
      this.token = token;
      return this;
    }
    
    public TokenAndSecret setSecret(String secret) {
      this.secret = secret;
      return this;
    }
    
    @Override
    public String toString() {
      return String.format("[TokenAndSecret token=%s, secret=%s]", token, secret);
    }
  }
}
