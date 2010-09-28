package org.dubh.islay.hub.server;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Embedded;
import javax.persistence.Id;

import org.dubh.islay.hub.model.UserAccount;
import org.dubh.islay.hub.shared.Network;

import com.google.common.collect.Lists;

/**
 * Access tokens for Oauth services. We store these in a separate table
 * to ensure we never send them back to the client along with other user
 * info.
 * 
 * @author brianduff
 */
@SuppressWarnings("serial")
public class UserTokens implements Serializable {
  /**
   * The user id these tokens belong to. This corresponds to the internalId
   * field on {@link UserAccount}.
   */
  private @Id Long userId;
  
  /**
   * OAuth tokens for this user.
   */
  private @Embedded Collection<NetworkTokens> tokens = Lists.newArrayList();
  
  /**
   * Returns the tokens for the specified network. Creates a new instance
   * if tokens are not already associated.
   */
  public NetworkTokens getTokens(Network network) {
    for (NetworkTokens t : tokens) {
      if (t.getNetwork() == network) {
        return t;
      }
    }
    NetworkTokens t = new NetworkTokens().setNetwork(network);
    tokens.add(t);
    return t;
  }
  
  public Long getUserId() {
    return userId;
  }
  
  public UserTokens setUserId(Long userId) {
    this.userId = userId;
    return this;
  }
}
